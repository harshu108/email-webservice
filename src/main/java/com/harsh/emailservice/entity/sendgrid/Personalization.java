package com.harsh.emailservice.entity.sendgrid;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.stereotype.Component;

import java.util.List;


public class Personalization {

    private List<Reciever> to;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Reciever> cc;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Reciever> bcc;

    public List<Reciever> getTo() {
        return to;
    }

    public void setTo(List<Reciever> to) {
        this.to = to;
    }

    public List<Reciever> getCc() {
        return cc;
    }

    public void setCc(List<Reciever> cc) {
        this.cc = cc;
    }

    public List<Reciever> getBcc() {
        return bcc;
    }

    public void setBcc(List<Reciever> bcc) {
        this.bcc = bcc;
    }

    @Component
    public static class Reciever {
        String email;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String name;

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


}

