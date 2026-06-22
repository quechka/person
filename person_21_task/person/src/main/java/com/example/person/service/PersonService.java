package com.example.person.service;

import com.example.person.model.Person;
import com.example.person.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonService {

	@Autowired
	private PersonRepository repository;

	public Iterable<Person> findAll() {
		return repository.findAll();
	}

	public Optional<Person> findById(int id) {
		return repository.findById(id);
	}

	public Person save(Person person) {
		return repository.save(person);
	}

	public boolean existsById(int id) {
		return repository.existsById(id);
	}
}
