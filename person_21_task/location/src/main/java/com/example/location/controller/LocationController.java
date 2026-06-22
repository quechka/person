package com.example.location.controller;

import com.example.location.model.Location;
import com.example.location.model.Weather;
import com.example.location.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/location")
public class LocationController {

	@Autowired
	private LocationService locationService;

	@GetMapping
	public Object getLocations(@RequestParam(required = false) String name) {
		if (name != null) {
			return locationService.findByName(name);
		}
		return locationService.findAll();
	}

	@PostMapping
	public Location save(@RequestBody Location location) {
		return locationService.save(location);
	}

	@PutMapping
	public Location update(@RequestParam String name, @RequestBody Location location) {
		return locationService.update(name, location);
	}

	@DeleteMapping
	public void delete(@RequestParam String name) {
		locationService.delete(name);
	}

	@GetMapping("/weather")
	public Weather getWeather(@RequestParam String name) {
		return locationService.getWeather(name);
	}
}
