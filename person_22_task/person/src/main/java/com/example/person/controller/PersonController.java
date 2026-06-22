package com.example.person.controller;

import com.example.person.model.Person;
import com.example.person.model.Weather;
import com.example.person.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {

	@Autowired
	private PersonRepository repository;

	@Autowired
	private RestTemplate restTemplate;

	@GetMapping
	public List<Person> findAll() {
		List<Person> persons = new ArrayList<>();
		repository.findAll().forEach(persons::add);
		return persons;
	}

	@GetMapping("/{id}")
	public ResponseEntity<Person> findById(@PathVariable int id) {
		return repository.findById(id)
				.map(person -> new ResponseEntity<>(person, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@PostMapping
	public ResponseEntity<Person> save(@RequestBody Person person) {
		if (person.getId() != 0 && repository.existsById(person.getId())) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(repository.save(person), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Person> update(@PathVariable int id, @RequestBody Person person) {
		if (!repository.existsById(id)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		person.setId(id);
		return new ResponseEntity<>(repository.save(person), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable int id) {
		if (!repository.existsById(id)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		repository.deleteById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/{id}/weather")
	public ResponseEntity<Weather> getWeather(@PathVariable int id) {
		if (!repository.existsById(id)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		String location = repository.findById(id).get().getLocation();
		Weather weather = restTemplate.getForObject(
				"http://location/location/weather?name=" + location,
				Weather.class);
		return new ResponseEntity<>(weather, HttpStatus.OK);
	}
}
