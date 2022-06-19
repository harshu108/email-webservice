package com.harsh.emailservice.services;

import com.harsh.emailservice.services.handlers.EmailClientHandler;

public class EmailClientHandlerState {

    private EmailClientHandler emailClientHandler;
    private boolean used;

    public EmailClientHandlerState() {
    }

    public EmailClientHandlerState(EmailClientHandler emailClientHandler, boolean used) {
        this.emailClientHandler = emailClientHandler;
        this.used = used;
    }

    public EmailClientHandler getEmailClientHandler() {
        return emailClientHandler;
    }

    public void setEmailClientHandler(EmailClientHandler emailClientHandler) {
        this.emailClientHandler = emailClientHandler;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
