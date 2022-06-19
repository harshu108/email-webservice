package com.harsh.emailservice.services.handlers;

import com.harsh.emailservice.dto.EmailRequest;
import com.harsh.emailservice.dto.EmailResponse;
import com.harsh.emailservice.enums.ClientStatus;
import com.harsh.emailservice.enums.ClientType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * This interface determines the contract for implementation of EmailClientHandler.
 * The handler for each email client must fulfill below contract to work with the service
 */
@Component
public abstract class EmailClientHandler {

    protected ClientStatus status;
    protected ClientType clientType;

    public abstract Mono<EmailResponse> sendEmail(EmailRequest emailRequest);
    public ClientStatus getStatus() {
        return status;
    }

    public void setStatus(ClientStatus status) {
        this.status = status;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }
}
