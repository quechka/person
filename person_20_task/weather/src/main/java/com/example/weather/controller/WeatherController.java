package com.example.weather.controller;

import com.example.weather.model.Root;
import com.example.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import java.util.Map;

@RestController
public class WeatherController {

	@Autowired
	private WeatherService weatherService;

	@GetMapping("/weather")
	public ResponseEntity<?> getWeather(@RequestParam double lat, @RequestParam double lon) {
		try {
			Root root = weatherService.getWeather(lat, lon);
			if (root == null) {
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(root);
		} catch (HttpClientErrorException.Unauthorized e) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(Map.of(
					"error", "Invalid API key",
					"message", "Check appId in application.properties. New keys activate within 2 hours after registration."));
		} catch (RestClientException e) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(Map.of(
					"error", "Weather API error",
					"message", e.getMessage()));
		}
	}
}
