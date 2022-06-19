package com.harsh.emailservice.entity.sendgrid;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Schema for request to SendGrid
 * {
 *   "personalizations": [
 *     {
 *       "to": [{
 *           "email": "usernme@emaildomain.com",
 *           "name": "John Doe"
 *         }],
 *       "cc":[{
 *           "email": "usernme@emaildomain.com",
 *           "name": "John Doe"
 *         }],
 *       "bcc":[{
 *           "email": "username@emaildomain.com",
 *           "name": "John Doe"
 *         }]
 *     }
 *   ],
 *   "from": {
 *     "email": "username@emaildomain.com",
 *     "name" : "John Doe"
 *   },
 *   "subject": "Sending with SendGrid is Fun",
 *   "content": [
 *     {
 *       "type": "text/plain",
 *       "value": "This is email content"
 *     }
 *   ]
 * }
 *
 */
@Component
@Scope("prototype")
public class SendGridPayload {
    @Autowired
    private List<Personalization> personalizations;
    @Autowired
    private From from;
    private String subject;
    @Autowired
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Content> content;

    public List<Personalization> getPersonalizations() {
        return personalizations;
    }

    public void setPersonalizations(List<Personalization> personalizations) {
        this.personalizations = personalizations;
    }

    public From getFrom() {
        return from;
    }

    public void setFrom(From from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<Content> getContent() {
        return content;
    }

    public void setContent(List<Content> content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "SendGridPayload{" +
                "personalizations=" + personalizations +
                ", from=" + from +
                ", subject='" + subject + '\'' +
                ", content=" + content +
                '}';
    }

    @Component
    public static class From {
        private String email;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String name;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @Component
    public static class Content {
        @JsonProperty("type")
        private static final String TYPE="text/plain" ;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String value;

        public String getType() {
            return TYPE;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
