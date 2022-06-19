package com.harsh.emailservice.services.handlers;

import com.harsh.emailservice.config.SendGridProperties;
import com.harsh.emailservice.dto.EmailRequest;
import com.harsh.emailservice.dto.EmailResponse;
import com.harsh.emailservice.entity.sendgrid.SendGridPayload;
import com.harsh.emailservice.entity.sendgrid.mapper.EmailRequestDtoToSendGridModelMapper;
import com.harsh.emailservice.enums.ClientStatus;
import com.harsh.emailservice.enums.ClientType;
import com.harsh.emailservice.exceptions.EmailClientException;
import com.harsh.emailservice.exceptions.EmailClientFatalException;
import com.harsh.emailservice.clients.SendGridEmailClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeoutException;

/**
 * This class handles the interactions with the SendGrid. It converts the request from service as per
 *  SendGrid's expectations and relays the response back to the service layer.
 */
@Component
@ConditionalOnProperty(value = "sendgrid.enabled", havingValue = "true")
public class SendGridClientHandler extends EmailClientHandler{

    @Autowired
    private SendGridProperties sendGridProperties;

    @Autowired
    private SendGridEmailClient sendGridEmailClient;
    private static final Logger logger = LoggerFactory.getLogger(SendGridClientHandler.class);

    public SendGridClientHandler() {
        this.status = ClientStatus.HEALTHY;
        this.clientType = ClientType.SENDGRID;
    }

    /**
     * This method converts the request from service and sends the same to SendGrid.
     * In case of success it wraps the response from endpoint to the client relevant message
     * @param emailRequest
     * @return
     */
    @Override
    public Mono<EmailResponse> sendEmail(EmailRequest emailRequest) {
        SendGridPayload payload = convertRequestToSendGridPayload(emailRequest);

        Mono<EmailResponse> emailResponse = sendGridEmailClient.sendEmail(payload, this);

        return emailResponse.doOnError(exception -> {
            if(exception instanceof TimeoutException || exception instanceof EmailClientFatalException){
                //Slow endpoint or any other server error
                this.status = ClientStatus.ERRORED;
            }else {
                //In case endpoint is unreachable or cannot service request
                this.status = ClientStatus.FAILED_INIT;
            }
        }).onErrorMap(exception ->{
            if(exception instanceof TimeoutException || exception instanceof EmailClientFatalException){
                return new EmailClientFatalException("Request Timed out", "Endpoint either unresponsive or very slow");
            }else if((exception instanceof EmailClientException) || (exception instanceof EmailClientFatalException)){
                return exception;
            }else{
                //handle any other exception/error in invoking MailGun
                return new EmailClientFatalException("Failed to Initialise the client", "Unknown error occurred");
            }
        });
    }

    /**
     * This method handles the response sent by SendGrid endpoint.
     * @param response
     * @return - Returns Mono with EmailResponse in case of success, and Mono with error in case of Exception
     */
    public Mono<EmailResponse> handleResponse(ClientResponse response){
        if (response.statusCode().equals(HttpStatus.ACCEPTED)) {
            return Mono.just(new EmailResponse("Email Send request accepted successfully"));
        } else if(response.statusCode().equals(HttpStatus.TOO_MANY_REQUESTS) || response.statusCode().is5xxServerError()){
            //Fail-over scenario
            return response.bodyToMono(String.class).flatMap(error ->{
                logger.error("Request to Sendgrid erred with status code:{} {} ", response.statusCode(), error);
                EmailClientException exception = new EmailClientException(error);
                logger.error(exception.getMessage(), exception);
                return Mono.error(exception);
            });
        }else{
            //Failed initialization, mark status as client not valid
            return response.bodyToMono(String.class).flatMap(error ->{
                logger.error("Request to Sendgrid erred with status code:{} {} ", response.statusCode(), error);
                EmailClientFatalException exception = new EmailClientFatalException("Error occurred initializing Sendgrid Client", error);
                logger.error(exception.getEndpointMessage(), exception);
                return Mono.error(exception);
            });
        }

    }
    private SendGridPayload convertRequestToSendGridPayload(EmailRequest emailRequest){
        EmailRequestDtoToSendGridModelMapper sendGridMapper = new EmailRequestDtoToSendGridModelMapper(emailRequest);
        SendGridPayload payload = sendGridMapper.getPayload();
        String fromEmail = sendGridProperties.getFromEmail();
        payload.getFrom().setEmail(fromEmail);
        return payload;

    }
}
