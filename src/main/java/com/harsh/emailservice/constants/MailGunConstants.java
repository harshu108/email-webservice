package com.harsh.emailservice.constants;

/**
 * This class contains MailGun specific constants
 */
public class MailGunConstants {

    private MailGunConstants(){
    }

    /**
     * Authentication prefix for MailGun
     */
    public static final String AUTH_PREFIX = "Basic";

    /**
     * URI to send email for SendGrid
     */
    public static final String EMAIL_SEND_URI = "/messages";
}

