package com.harsh.emailservice;

import com.harsh.emailservice.controllers.EmailController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest

class EmailServiceApplicationTests {

	@Autowired
	private EmailController emailController;

	@Test
	void contextLoads() {
		assertThat(emailController).isNotNull();
	}

	

}
