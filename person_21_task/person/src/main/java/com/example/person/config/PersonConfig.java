package com.example.person.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class PersonConfig {

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
