package com.harsh.emailservice.constants;

/**
 * This class contains SendGrid specific constants
 */
public class SendGridConstants {

    private SendGridConstants(){
    }
    /**
     * URI to send email for SendGrid
     */
    public static final String EMAIL_SEND_URI = "/mail/send";
    /**
     * Authorization prefix for SendGrid
     */
    public static final String AUTHORIZATION_PREFIX = "Bearer ";

}
