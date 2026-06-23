package com.example.person.controller;

import com.example.person.dto.LocationDto;
import com.example.person.dto.WeatherDto;
import com.example.person.model.Person;
import com.example.person.service.PersonService;
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
@RequestMapping("/person")
public class PersonController {

	@Autowired
	private PersonService personService;

	@GetMapping
	public Iterable<Person> getAllPersons() {
		return personService.findAll();
	}

	@GetMapping("/{id}")
	public Person getPersonById(@PathVariable int id) {
		return personService.getById(id);
	}

	@GetMapping("/{id}/location")
	public LocationDto getPersonLocation(@PathVariable int id) {
		return personService.getLocation(id);
	}

	@GetMapping("/{id}/weather")
	public WeatherDto getPersonWeather(@PathVariable int id) {
		return personService.getWeather(id);
	}

	@PostMapping
	public ResponseEntity<Person> addPerson(@RequestBody Person person) {
		Person saved = personService.addPerson(person);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}
}
