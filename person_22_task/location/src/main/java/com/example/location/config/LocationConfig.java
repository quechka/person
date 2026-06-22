package com.example.location.config;

import com.example.location.model.Location;
import com.example.location.repository.LocationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class LocationConfig {

	@Bean
	@LoadBalanced
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	@Bean
	CommandLineRunner initLocations(LocationRepository repository) {
		return args -> {
			if (repository.count() == 0) {
				repository.save(new Location(37.6173, 55.7558, "Москва"));
				repository.save(new Location(30.3351, 59.9343, "Санкт-Петербург"));
				repository.save(new Location(82.9357, 55.0084, "Новосибирск"));
			}
		};
	}
}
