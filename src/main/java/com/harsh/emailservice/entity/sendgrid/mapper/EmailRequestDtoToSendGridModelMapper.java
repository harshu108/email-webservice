package com.harsh.emailservice.entity.sendgrid.mapper;

import com.harsh.emailservice.dto.EmailRequest;
import com.harsh.emailservice.entity.sendgrid.Personalization;
import com.harsh.emailservice.entity.sendgrid.SendGridPayload;

import java.util.*;
/**
 * This mapper class converts the request received to the expected format by SendGrid
 */
public class EmailRequestDtoToSendGridModelMapper {

    private EmailRequest emailRequest;
    public EmailRequestDtoToSendGridModelMapper(EmailRequest emailRequest) {
        this.emailRequest = emailRequest;
    }

    /**
     * This method converts EmailRequest object to expected SendGrid Payload
     * @return - SendGrid payload
     */
    public SendGridPayload getPayload(){
        String emailBody = emailRequest.getEmailBody();
        Set<String> ccEmailList = emailRequest.getCc();
        String emailSubject = emailRequest.getEmailSubject();
        Set<String> toEmailList = emailRequest.getTo();
        Set<String> bccEmailList = emailRequest.getBcc();
        String senderName = emailRequest.getSenderName();

        Personalization personalization = new Personalization();

        List<Personalization.Reciever> to = new ArrayList<>();
        setEmailRecipients(toEmailList, to);
        personalization.setTo(to);

        if(ccEmailList!=null && !ccEmailList.isEmpty()){
            List<Personalization.Reciever> cc = new ArrayList<>();
            setEmailRecipients(ccEmailList, cc);
            personalization.setCc(cc);
        }
        if(bccEmailList!= null && !bccEmailList.isEmpty()){
            List<Personalization.Reciever> bcc = new ArrayList<>();
            setEmailRecipients(bccEmailList, bcc);
            personalization.setBcc(bcc);
        }

        SendGridPayload payload = new SendGridPayload();
        payload.setSubject(emailSubject);

        SendGridPayload.From from = new SendGridPayload.From();
        if(senderName != null && !senderName.isBlank()){
            from.setName(senderName);
        }
        payload.setFrom(from);

        List<Personalization> personalizationList = new ArrayList<>();
        personalizationList.add(personalization);
        payload.setPersonalizations(personalizationList);


        if(emailBody!=null){
            SendGridPayload.Content content = new SendGridPayload.Content();
            content.setValue(emailBody);

            payload.setContent(Arrays.asList(content));
        }

        return payload;

    }

    private void setEmailRecipients(Set<String> recipientList, List<Personalization.Reciever> recieversList){

        recipientList.forEach(recipient->{
            Personalization.Reciever receiver = new Personalization.Reciever();
            receiver.setEmail(recipient);
            recieversList.add(receiver);
        });
    }


}
