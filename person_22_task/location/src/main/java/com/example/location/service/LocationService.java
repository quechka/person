package com.example.location.service;

import com.example.location.model.Location;
import com.example.location.model.Weather;
import com.example.location.model.WeatherRoot;
import com.example.location.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocationService {

	@Autowired
	private LocationRepository repository;

	@Autowired
	private RestTemplate restTemplate;

	public List<Location> findAll() {
		List<Location> locations = new ArrayList<>();
		repository.findAll().forEach(locations::add);
		return locations;
	}

	public Location findByName(String name) {
		return repository.findByName(name)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found: " + name));
	}

	public Location save(Location location) {
		return repository.save(location);
	}

	public Location update(String name, Location location) {
		Location existing = findByName(name);
		existing.setLongitude(location.getLongitude());
		existing.setLatitude(location.getLatitude());
		existing.setName(location.getName());
		return repository.save(existing);
	}

	public void delete(String name) {
		Location location = findByName(name);
		repository.delete(location);
	}

	public Weather getWeather(String name) {
		Location location = findByName(name);
		String url = String.format("http://weather/weather?lat=%s&lon=%s",
				location.getLatitude(), location.getLongitude());
		WeatherRoot weatherRoot = restTemplate.getForObject(url, WeatherRoot.class);
		return weatherRoot.getMain();
	}
}
