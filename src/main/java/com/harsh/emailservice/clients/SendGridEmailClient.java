package com.harsh.emailservice.clients;

import com.harsh.emailservice.config.SendGridProperties;
import com.harsh.emailservice.constants.Constants;
import com.harsh.emailservice.constants.SendGridConstants;
import com.harsh.emailservice.dto.EmailResponse;
import com.harsh.emailservice.entity.sendgrid.SendGridPayload;
import com.harsh.emailservice.enums.ClientStatus;
import com.harsh.emailservice.filters.WebClientFilter;
import com.harsh.emailservice.services.handlers.SendGridClientHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static com.harsh.emailservice.constants.SendGridConstants.AUTHORIZATION_PREFIX;

/**
 * This class instantiates WebClient to make API calls to SendGrid
 */
@Component
@ConditionalOnProperty(value = "sendgrid.enabled", havingValue = "true")
public class SendGridEmailClient {

    private final WebClient webClient;

    @Autowired
    public SendGridEmailClient(SendGridProperties sendGridProperties) {
        this.webClient = WebClient.builder()
                .baseUrl(sendGridProperties.getBaseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.USER_AGENT, Constants.USER_AGENT)
                .defaultHeader(HttpHeaders.AUTHORIZATION, AUTHORIZATION_PREFIX + sendGridProperties.getApiKey())
                .filter(WebClientFilter.logRequest())
                .build();
    }

    /**
     * This method makes an API call to MailGun to send email
     * @param payload - SendGrid Payload to make request to SendGrid to send email
     * @param sendGridClientHandler - The Sendgrid email client handler object
     * @return - Returns Mono of response sent by SendGrid.
     */
    public Mono<EmailResponse> sendEmail(SendGridPayload payload, SendGridClientHandler sendGridClientHandler) {
        return webClient.post()
                .uri(SendGridConstants.EMAIL_SEND_URI)
                .bodyValue(payload)
                .exchangeToMono(sendGridClientHandler::handleResponse)
                .timeout(Duration.ofSeconds(10));
    }

}
