package com.example.location.service;

import com.example.location.model.Location;
import com.example.location.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LocationService {

	@Autowired
	private LocationRepository repository;

	public Iterable<Location> findAll() {
		return repository.findAll();
	}

	public Optional<Location> findById(int id) {
		return repository.findById(id);
	}

	public Optional<Location> findByName(String name) {
		return repository.findByName(name);
	}

	public Location save(Location location) {
		return repository.save(location);
	}

	public boolean existsById(int id) {
		return repository.existsById(id);
	}

	public long count() {
		return repository.count();
	}
}
