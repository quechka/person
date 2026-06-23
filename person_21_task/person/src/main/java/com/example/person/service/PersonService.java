package com.example.person.service;

import com.example.person.dto.LocationDto;
import com.example.person.dto.WeatherDto;
import com.example.person.model.Person;
import com.example.person.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;

@Service
public class PersonService {

	@Autowired
	private PersonRepository repository;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${location.service.url}")
	private String locationServiceUrl;

	@Value("${weather.service.url}")
	private String weatherServiceUrl;

	public Iterable<Person> findAll() {
		return repository.findAll();
	}

	public Optional<Person> findById(int id) {
		return repository.findById(id);
	}

	public Person getById(int id) {
		return repository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found: " + id));
	}

	public Person save(Person person) {
		return repository.save(person);
	}

	public Person addPerson(Person person) {
		if (person.getId() != 0 && repository.existsById(person.getId())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Person with id already exists: " + person.getId());
		}
		return repository.save(person);
	}

	public boolean existsById(int id) {
		return repository.existsById(id);
	}

	public LocationDto getLocation(int id) {
		Person person = getById(id);
		return restTemplate.getForObject(
				locationServiceUrl + "/location?name={name}",
				LocationDto.class,
				person.getLocation());
	}

	public WeatherDto getWeather(int id) {
		Person person = getById(id);
		LocationDto location = restTemplate.getForObject(
				locationServiceUrl + "/location?name={name}",
				LocationDto.class,
				person.getLocation());

		Map<?, ?> root = restTemplate.getForObject(
				weatherServiceUrl + "/weather?lat={lat}&lon={lon}",
				Map.class,
				location.getLatitude(),
				location.getLongitude());

		Map<String, Object> main = asMap(root.get("main"));
		return new WeatherDto(
				(String) root.get("name"),
				getDouble(main, "temp"),
				getDouble(main, "feels_like", "feelsLike"),
				getDouble(main, "temp_min", "tempMin"),
				getDouble(main, "temp_max", "tempMax"),
				getInt(main, "pressure"),
				getInt(main, "humidity"),
				getDescription(root));
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> asMap(Object value) {
		if (value instanceof Map<?, ?> map) {
			return (Map<String, Object>) map;
		}
		return null;
	}

	private String getDescription(Map<?, ?> root) {
		if (!(root.get("weather") instanceof java.util.List<?> weatherList) || weatherList.isEmpty()) {
			return null;
		}
		if (weatherList.get(0) instanceof Map<?, ?> weatherItem) {
			return (String) weatherItem.get("description");
		}
		return null;
	}

	private double getDouble(Map<String, Object> map, String... keys) {
		for (String key : keys) {
			Object value = map.get(key);
			if (value instanceof Number number) {
				return number.doubleValue();
			}
		}
		return 0;
	}

	private int getInt(Map<String, Object> map, String key) {
		Object value = map.get(key);
		if (value instanceof Number number) {
			return number.intValue();
		}
		return 0;
	}
}
