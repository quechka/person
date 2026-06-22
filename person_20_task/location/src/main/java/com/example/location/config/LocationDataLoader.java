package com.example.location.config;

import com.example.location.model.Location;
import com.example.location.service.LocationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LocationDataLoader {

	@Bean
	CommandLineRunner initLocations(LocationService locationService) {
		return args -> {
			if (locationService.count() == 0) {
				locationService.save(new Location("Москва", 55.7558, 37.6173));
				locationService.save(new Location("Санкт-Петербург", 59.9343, 30.3351));
				locationService.save(new Location("Новосибирск", 55.0084, 82.9357));
			}
		};
	}
}
