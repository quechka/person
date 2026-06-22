package com.example.location.controller;

import com.example.location.model.Location;
import com.example.location.model.Weather;
import com.example.location.model.WeatherRoot;
import com.example.location.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/location")
public class LocationController {

	@Autowired
	private LocationRepository repository;

	@Autowired
	private RestTemplate restTemplate;

	@GetMapping
	public Object getLocations(@RequestParam(required = false) String name) {
		if (name != null) {
			return repository.findByName(name).get();
		}
		List<Location> locations = new ArrayList<>();
		repository.findAll().forEach(locations::add);
		return locations;
	}

	@PostMapping
	public Location save(@RequestBody Location location) {
		return repository.save(location);
	}

	@PutMapping
	public Location update(@RequestParam String name, @RequestBody Location location) {
		Location existing = repository.findByName(name).get();
		existing.setLongitude(location.getLongitude());
		existing.setLatitude(location.getLatitude());
		existing.setName(location.getName());
		return repository.save(existing);
	}

	@DeleteMapping
	public void delete(@RequestParam String name) {
		Location location = repository.findByName(name).get();
		repository.delete(location);
	}

	@GetMapping("/weather")
	public Weather getWeather(@RequestParam String name) {
		Location location = repository.findByName(name).get();
		String url = String.format("http://weather/weather?lat=%s&lon=%s",
				location.getLatitude(), location.getLongitude());
		WeatherRoot weatherRoot = restTemplate.getForObject(url, WeatherRoot.class);
		return weatherRoot.getMain();
	}
}
