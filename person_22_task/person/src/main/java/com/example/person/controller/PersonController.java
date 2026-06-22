package com.example.person.controller;

import com.example.person.model.Person;
import com.example.person.model.Weather;
import com.example.person.service.PersonService;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {

	@Autowired
	private PersonService personService;

	@GetMapping
	public List<Person> findAll() {
		return personService.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Person> findById(@PathVariable int id) {
		try {
			return new ResponseEntity<>(personService.getById(id), HttpStatus.OK);
		} catch (ResponseStatusException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping
	public ResponseEntity<Person> save(@RequestBody Person person) {
		if (person.getId() != 0 && personService.existsById(person.getId())) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(personService.save(person), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Person> update(@PathVariable int id, @RequestBody Person person) {
		try {
			return new ResponseEntity<>(personService.update(id, person), HttpStatus.OK);
		} catch (ResponseStatusException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable int id) {
		try {
			personService.delete(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (ResponseStatusException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/{id}/weather")
	public ResponseEntity<Weather> getWeather(@PathVariable int id) {
		try {
			return new ResponseEntity<>(personService.getWeather(id), HttpStatus.OK);
		} catch (ResponseStatusException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
