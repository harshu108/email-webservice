package com.harsh.emailservice.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * This class consists of properties required to communicate with MailGun
 */
@ConditionalOnProperty(value = "mailgun.enabled", havingValue = "true")
@ConfigurationProperties(prefix = "mailgun")
@ConstructorBinding
@ConfigurationPropertiesScan
public class MailGunProperties {

    private final String apikey;
    private final String host;
    private final String apiVersion;
    private final String domain;
    private final String fromEmail;

    private final String baseUrl;

    public MailGunProperties(String apikey, String host, String apiVersion, String domain, String fromEmail) {
        this.apikey = apikey;
        this.host = host;
        this.apiVersion = apiVersion;
        this.domain = domain;
        this.fromEmail = fromEmail;

        this.baseUrl = host + "/" + apiVersion + "/" + domain;
    }
    public String getApikey() {
        return apikey;
    }

    public String getHost() {
        return host;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public String getDomain() {
        return domain;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
