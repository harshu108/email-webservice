package com.harsh.emailservice.dto;

import com.harsh.emailservice.constraints.MaxEmailRecipientsLimit;
import com.harsh.emailservice.constraints.UniqueEmailRecipients;

import javax.validation.constraints.*;
import java.util.Set;

/**
 * This class defines the API request format for the "Send Email" api, along with the constraints
 */
@UniqueEmailRecipients(message = "The email addresses across to, cc, bcc must be unique")
@MaxEmailRecipientsLimit(message = "The max limit for recipients across to, cc, bcc is 1000")
public class EmailRequest {

    private final String senderName;
    @Size(min=1, message="The 'to' list cannot be empty")
    @NotNull(message = "The 'to' list is missing from the request")
    private final Set<@Email String> to;
    private final Set<@Email String> cc;
    private final Set<@Email String> bcc;
    @NotNull(message = "Email Subject cannot be null")
    @NotBlank(message = "Email Subject cannot be empty")
    private final String emailSubject;
    @NotNull(message = "Email Body cannot be null")
    @NotBlank(message = "Email Body cannot be empty")
    private final String emailBody;

    public EmailRequest(String senderName, Set<String> toEmailAddresses, Set<String> ccEmailAddressesList, Set<String> bccEmailAddressesList, String emailSubject, String emailBody) {
        this.senderName = senderName;
        this.to = toEmailAddresses;
        this.cc = ccEmailAddressesList;
        this.bcc = bccEmailAddressesList;
        this.emailSubject = emailSubject;
        this.emailBody = emailBody;
    }


    public Set<String> getTo() {
        return to;
    }

    public Set<String> getCc() {
        return cc;
    }

    public Set<String> getBcc() {
        return bcc;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public String getEmailBody() {
        return emailBody;
    }

    public String getSenderName() {
        return senderName;
    }

    @Override
    public String toString() {
        return "EmailRequest{" +
                "senderName='" + senderName + '\'' +
                ", to=" + to +
                ", cc=" + cc +
                ", bcc=" + bcc +
                ", emailSubject='" + emailSubject + '\'' +
                ", emailBody='" + emailBody + '\'' +
                '}';
    }
}
