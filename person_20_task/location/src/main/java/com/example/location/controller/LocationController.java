package com.example.location.controller;

import com.example.location.model.Location;
import com.example.location.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/location")
public class LocationController {

	@Autowired
	private LocationService locationService;

	@GetMapping
	public Iterable<Location> getAllLocations() {
		return locationService.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Location> getLocationById(@PathVariable int id) {
		return locationService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/name/{name}")
	public ResponseEntity<Location> getLocationByName(@PathVariable String name) {
		return locationService.findByName(name)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Location> addLocation(@RequestBody Location location) {
		if (location.getId() != 0 && locationService.existsById(location.getId())) {
			return ResponseEntity.badRequest().build();
		}
		Location saved = locationService.save(location);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}
}
