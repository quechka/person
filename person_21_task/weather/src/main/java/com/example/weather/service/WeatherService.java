package com.example.weather.service;

import com.example.weather.model.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WeatherService {

	private static final Logger log = LoggerFactory.getLogger(WeatherService.class);
	private static final Duration CACHE_TTL = Duration.ofMinutes(1);

	private final ConcurrentHashMap<String, CachedWeather> cache = new ConcurrentHashMap<>();

	@Value("${appId}")
	private String appId;

	@Value("${url.weather}")
	private String weatherUrl;

	public Root getWeather(double lat, double lon, RestTemplate restTemplate) {
		String key = lat + ":" + lon;
		CachedWeather cached = cache.get(key);

		if (cached != null && cached.expiresAt.isAfter(Instant.now())) {
			return cached.root;
		}

		String requestUrl = String.format("%s?lat=%s&lon=%s&units=metric&appid=%s",
				weatherUrl, lat, lon, appId);

		try {
			Root root = restTemplate.getForObject(requestUrl, Root.class);
			if (root != null) {
				cache.put(key, new CachedWeather(root, Instant.now().plus(CACHE_TTL)));
			}
			return root;
		} catch (HttpClientErrorException.Unauthorized e) {
			log.error("OpenWeatherMap rejected API key. Activate the key at openweathermap.org and wait up to 2 hours.");
			throw e;
		} catch (RestClientException e) {
			log.error("OpenWeatherMap request failed for lat={}, lon={}: {}", lat, lon, e.getMessage());
			throw e;
		}
	}

	private record CachedWeather(Root root, Instant expiresAt) {
	}
}
