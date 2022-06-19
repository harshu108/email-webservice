package com.harsh.emailservice.controllers;

import com.harsh.emailservice.dto.EmailRequest;
import com.harsh.emailservice.dto.EmailResponse;
import com.harsh.emailservice.services.interfaces.SendEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


import javax.validation.Valid;


@RestController
@Validated
@RequestMapping(value="/v1/email")
public class EmailController {

    @Autowired
    private SendEmailService sendEmailService;


    @PostMapping(value = "/send",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<EmailResponse> sendEmail(@RequestBody @Valid EmailRequest emailRequest) {
        return sendEmailService.sendEmail(emailRequest);

    }
}
