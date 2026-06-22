package com.example.person.service;

import com.example.person.model.Person;
import com.example.person.model.Weather;
import com.example.person.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService {

	@Autowired
	private PersonRepository repository;

	@Autowired
	private RestTemplate restTemplate;

	public List<Person> findAll() {
		List<Person> persons = new ArrayList<>();
		repository.findAll().forEach(persons::add);
		return persons;
	}

	public Person getById(int id) {
		return repository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found: " + id));
	}

	public Person save(Person person) {
		return repository.save(person);
	}

	public boolean existsById(int id) {
		return repository.existsById(id);
	}

	public Person update(int id, Person person) {
		if (!repository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found: " + id);
		}
		person.setId(id);
		return repository.save(person);
	}

	public void delete(int id) {
		if (!repository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found: " + id);
		}
		repository.deleteById(id);
	}

	public Weather getWeather(int id) {
		Person person = getById(id);
		return restTemplate.getForObject(
				"http://location/location/weather?name={name}",
				Weather.class,
				person.getLocation());
	}
}
