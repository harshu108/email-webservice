package com.harsh.emailservice.services.handlers;

import com.harsh.emailservice.config.MailGunProperties;
import com.harsh.emailservice.dto.EmailRequest;
import com.harsh.emailservice.dto.EmailResponse;
import com.harsh.emailservice.entity.mailgun.MailGunPayload;
import com.harsh.emailservice.entity.mailgun.mapper.EmailRequestDtoToMailGunModelMapper;
import com.harsh.emailservice.enums.ClientStatus;
import com.harsh.emailservice.enums.ClientType;
import com.harsh.emailservice.exceptions.EmailClientException;
import com.harsh.emailservice.exceptions.EmailClientFatalException;
import com.harsh.emailservice.clients.MailGunEmailClient;
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
 * This class handles the interactions with the MailGun. It converts the request from service as per
 * Mailgun's expectations and relays the response back to the service layer.
 */
@Component
@ConditionalOnProperty(value = "mailgun.enabled", havingValue = "true")
public class MailGunClientHandler extends EmailClientHandler{

    @Autowired
    private MailGunProperties mailgunProperties;

    @Autowired
    private MailGunEmailClient mailGunEmailClient;
    private static final Logger logger = LoggerFactory.getLogger(MailGunClientHandler.class);

    public MailGunClientHandler() {
        this.status = ClientStatus.HEALTHY;
        this.clientType = ClientType.MAILGUN;
    }

    /**
     * This method converts the request from service and sends the same to MailGun.
     * In case of success it wraps the response from endpoint to the client relevant message
     * @param emailRequest - Email request to forward to MailGun
     * @return It returns Mono of EmailResponse in case of success. In case of error, it returns Mono with the exception
     */
    @Override
    public Mono<EmailResponse> sendEmail(EmailRequest emailRequest) {
        MailGunPayload payload = convertRequestToMailGunPayload(emailRequest);
        Mono<EmailResponse> emailResponse = mailGunEmailClient.sendEmail(payload, this);
        return emailResponse.doOnError(exception -> {
            if(exception instanceof TimeoutException || exception instanceof EmailClientFatalException){
                //Slow endpoint or any other server error
                this.status = ClientStatus.ERRORED;
            }else {
                //In case endpoint is unreachable or cannot service request
                this.status = ClientStatus.FAILED_INIT;
            }
        }).onErrorMap(error ->{
            if(error instanceof TimeoutException){
                return new EmailClientFatalException("Request Timed out", "Endpoint either unresponsive or very slow");
            }else if((error instanceof EmailClientException) || (error instanceof EmailClientFatalException)){
                return error;
            }else{
                //handle any other exception/error in invoking MailGun
                return new EmailClientFatalException("Failed to Initialise the client", "Unknown error occurred");
            }
        });
    }

    /**
     * This method handles the response sent by MailGun endpoint.
     * @param response
     * @return - Returns Mono with EmailResponse in case of success, and Mono with error in case of Exception
     */
    public Mono<EmailResponse> handleResponse(ClientResponse response){
        if (response.statusCode().equals(HttpStatus.OK)) {
            return Mono.just(new EmailResponse("Email Send request accepted successfully"));
        } else if(response.statusCode().equals(HttpStatus.TOO_MANY_REQUESTS) || response.statusCode().is5xxServerError()){
            //Rate limit, or internal server errors at the endpoint, which means that it's a temporary error
            return response.bodyToMono(String.class).flatMap(error ->{
                logger.error("Request to MailGun erred with status code: {}{}", response.statusCode(), error);
                EmailClientException exception = new EmailClientException(error);
                logger.error(exception.getMessage(), exception);
                return Mono.error(exception);
            });
        }else{
            //Failed initialization, mark status as client not valid
            return response.bodyToMono(String.class).flatMap(error ->{
                logger.error("Request to MailGun erred with status code:{} {}", response.statusCode(), error);
                EmailClientFatalException exception = new EmailClientFatalException("Error occurred initializing MailGun Client", error);
                logger.error(exception.getEndpointMessage(), exception);
                return Mono.error(exception);
            });
        }

    }
    private MailGunPayload convertRequestToMailGunPayload(EmailRequest emailRequest){
        EmailRequestDtoToMailGunModelMapper mailgunModelMapper = new EmailRequestDtoToMailGunModelMapper(emailRequest);
        MailGunPayload payload = mailgunModelMapper.getPayload();
        String fromEmail = this.mailgunProperties.getFromEmail();
        payload.setFrom(fromEmail);

        return payload;

    }
}
