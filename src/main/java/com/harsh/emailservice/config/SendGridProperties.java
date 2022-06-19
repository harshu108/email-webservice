package com.harsh.emailservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * This class consists of properties required to communicate with SendGrid
 */
@ConfigurationProperties(prefix = "sendgrid")
@ConstructorBinding
@ConfigurationPropertiesScan
public class SendGridProperties {
    private final String apiKey;
    private final String host;
    private final String apiVersion;
    private final String fromEmail;
    private String baseUrl;

    public SendGridProperties(String apiKey, String host, String apiVersion, String fromEmail) {
        this.apiKey = apiKey;
        this.host = host;
        this.apiVersion = apiVersion;
        this.fromEmail = fromEmail;
        this.baseUrl = host + "/" + apiVersion;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getHost() {
        return host;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public String getBaseUrl() {
        return baseUrl;
    }


}
