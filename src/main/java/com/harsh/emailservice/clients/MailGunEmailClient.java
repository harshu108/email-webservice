package com.harsh.emailservice.clients;

import com.harsh.emailservice.config.MailGunProperties;
import com.harsh.emailservice.constants.MailGunConstants;
import com.harsh.emailservice.dto.EmailResponse;
import com.harsh.emailservice.entity.mailgun.MailGunPayload;
import com.harsh.emailservice.filters.WebClientFilter;
import com.harsh.emailservice.services.handlers.MailGunClientHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

import static com.harsh.emailservice.constants.MailGunConstants.AUTH_PREFIX;
import static com.harsh.emailservice.constants.Constants.USER_AGENT;

/**
 * This class instantiates WebClient to make API calls to MailGun
 */
@Component
@ConditionalOnProperty(value = "mailgun.enabled", havingValue = "true")
public class MailGunEmailClient {
    private final WebClient webClient;
    @Autowired
    public MailGunEmailClient(MailGunProperties mailgunProperties){
        this.webClient = WebClient.builder()
                .baseUrl(mailgunProperties.getBaseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_FORM_URLENCODED))
                .defaultHeader(HttpHeaders.USER_AGENT, USER_AGENT)
                .defaultHeader(HttpHeaders.AUTHORIZATION, AUTH_PREFIX + Base64Utils.encodeToString(("api:"+mailgunProperties.getApikey()).getBytes(StandardCharsets.UTF_8)))
                .filter(WebClientFilter.logRequest())
                .build();
    }

    /**
     * This method makes an API call to MailGun to send email
     * @param payload - MailGun Payload to make request to SendGrid to send email
     * @param mailgunClientHandler - The MailGun email client handler object
     * @return - Returns Mono of response sent by MailGun.
     */
    public Mono<EmailResponse> sendEmail(MailGunPayload payload, MailGunClientHandler mailgunClientHandler){
        return webClient.post()
                .uri(MailGunConstants.EMAIL_SEND_URI)
                .body(BodyInserters.fromFormData(payload.toMap()))
                .exchangeToMono(mailgunClientHandler::handleResponse)
                .timeout(Duration.ofSeconds(5));

    }
}
