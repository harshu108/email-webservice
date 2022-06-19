package com.harsh.emailservice.services.interfaces;

import com.harsh.emailservice.dto.EmailRequest;
import com.harsh.emailservice.dto.EmailResponse;
import reactor.core.publisher.Mono;

/**
 * This interface defines the contract Service layer must fulfill to service email send request.
 */
public interface SendEmailService {
    public Mono<EmailResponse> sendEmail(EmailRequest emailRequest);
}
