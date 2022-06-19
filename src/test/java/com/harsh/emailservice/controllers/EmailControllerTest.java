package com.harsh.emailservice.controllers;

import com.harsh.emailservice.dto.EmailRequest;
import com.harsh.emailservice.dto.EmailResponse;
import com.harsh.emailservice.services.SendEmailServiceImpl;
import com.harsh.emailservice.services.interfaces.SendEmailService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import reactor.core.publisher.Mono;

import java.net.URL;
import java.util.*;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebFluxTest(EmailController.class)
@ContextConfiguration(classes={SendEmailServiceImpl.class, EmailController.class})
class EmailControllerTest {

    @MockBean
    private SendEmailService sendEmailService;

    @Autowired
    private WebTestClient webClient;



//    @BeforeEach
//    void setMockOutput(){
//        EmailRequest emailRequest = new EmailRequest("Harsh", null,null, null, null, null );
//        Mockito.when(emailService.sendEmail(null)).thenReturn(Mono.just(new EmailResponse("Hello")));
//    }

    @DisplayName("Invalid Request Body - Missing params")
    @Test
    void shouldFailWithErrorBadRequest() {
        EmailRequest emailRequest = new EmailRequest("Harsh", null,null, null, null, null );
        Mockito.when(sendEmailService.sendEmail(null)).thenReturn(Mono.just(new EmailResponse("Accepted")));
//        assertEquals("Hello", emailController.sendEmail(null).flatMap());
        webClient.post().uri("/v1/email/send").bodyValue(emailRequest).exchange().expectStatus().isBadRequest();
    }

    @DisplayName("Invalid Request Body - Max Limit Constraint Violation")
    @Test
    void shouldSendEmailSuccessfully() {
        Set<String> toList = new HashSet<>();
        Set<String> ccList = new HashSet<>();
        Set<String> bccList = new HashSet<>();
        for(int i=0; i<350; i++){
            String to = "to"+i+"@test.com";
            String cc = "cc"+i+"@test.com";
            String bcc = "bcc"+i+"@test.com";
            toList.add(to);
            ccList.add(cc);
            bccList.add(bcc);
        }
        EmailRequest emailRequest = new EmailRequest("Harsh", toList,ccList, bccList, "Test Subject", "Test body" );
        Mockito.when(sendEmailService.sendEmail(null)).thenReturn(Mono.just(new EmailResponse("Accepted")));
        webClient.post().uri("/v1/email/send").bodyValue(emailRequest).exchange().expectStatus().isBadRequest().expectBody().toString().contains("The max limit for recipients across to, cc, bcc is 1000");
    }
}
