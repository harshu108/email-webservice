package com.harsh.emailservice.entity.mailgun;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Set;

/**
 * MailFun expects URL encoded form data
 * from=Harsh Doe<random@gmail.com>
 * to=username@emaildomain.com,username2@emaildomain.com
 * cc=username@emaildomain.com,username2@emaildomain.com
 * bcc=username@emaildomain.com,username2@emaildomain.com
 * subject=Email subject
 * text=Email body
 */
public class MailGunPayload {

    private Set<String> to;
    private Set<String> cc;
    private Set<String> bcc;
    private String subject;
    private String text;
    private String from;
    private String senderName;

    public MailGunPayload(Set<String> to, Set<String> cc, Set<String> bcc, String subject, String text, String from, String senderName) {
        this.to = to;
        this.cc = cc;
        this.bcc = bcc;
        this.subject = subject;
        this.text = text;
        this.from = from;
        this.senderName = senderName;
    }

    public MailGunPayload() {

    }

    public Set<String> getTo() {
        return to;
    }

    public void setTo(Set<String> to) {
        this.to = to;
    }

    public Set<String> getCc() {
        return cc;
    }

    public void setCc(Set<String> cc) {
        this.cc = cc;
    }

    public Set<String> getBcc() {
        return bcc;
    }

    public void setBcc(Set<String> bcc) {
        this.bcc = bcc;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    /**
     * This method returns the properties of class as a MultiValue map
     * @return - Returns properties of class as a MultiValueMap
     */
    public MultiValueMap<String, String> toMap(){
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("to", String.join(",", this.to));
        map.add("from", senderName +"<" + from + ">");
        if(this.bcc != null && !this.bcc.isEmpty()) map.add("bcc", String.join(",", this.bcc));
        if(this.cc != null && !this.cc.isEmpty()) map.add("cc", String.join(",", this.cc));
        if(this.subject !=null && !this.subject.isBlank()) map.add("subject", this.subject);
        if(this.text !=null && !this.text.isBlank()) map.add("text", this.text);
        return map;
    }
}
