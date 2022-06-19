package com.harsh.emailservice.entity.mailgun.mapper;

import com.harsh.emailservice.dto.EmailRequest;
import com.harsh.emailservice.entity.mailgun.MailGunPayload;

import java.util.Set;

/**
 * This mapper class converts the request received to the expected format by MailGun
 */
public class EmailRequestDtoToMailGunModelMapper {

    private EmailRequest emailRequest;

    public EmailRequestDtoToMailGunModelMapper(EmailRequest emailRequest) {
        this.emailRequest = emailRequest;
    }

    /**
     * This method converts EmailRequest object to expected MailGun Payload
     * @return - MailGun Payload
     */
    public MailGunPayload getPayload(){

        MailGunPayload payload = new MailGunPayload();

        String emailBody = emailRequest.getEmailBody();
        payload.setText(emailBody);

        Set<String> ccEmailList = emailRequest.getCc();
        payload.setCc(ccEmailList);

        String emailSubject = emailRequest.getEmailSubject();
        payload.setSubject(emailSubject);

        Set<String> toEmailList = emailRequest.getTo();
        payload.setTo(toEmailList);

        Set<String> bccEmailList = emailRequest.getBcc();
        payload.setBcc(bccEmailList);

        String senderName = emailRequest.getSenderName();
        payload.setSenderName(senderName);

        return payload;
    }

}
