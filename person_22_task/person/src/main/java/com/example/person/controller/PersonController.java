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
	public Person findById(@PathVariable int id) {
		return personService.getById(id);
	}

	@PostMapping
	public ResponseEntity<Person> save(@RequestBody Person person) {
		Person saved = personService.addPerson(person);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}

	@PutMapping("/{id}")
	public Person update(@PathVariable int id, @RequestBody Person person) {
		return personService.update(id, person);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable int id) {
		personService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}/weather")
	public Weather getWeather(@PathVariable int id) {
		return personService.getWeather(id);
	}
}
