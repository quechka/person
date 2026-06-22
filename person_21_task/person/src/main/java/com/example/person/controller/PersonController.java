package com.example.person.controller;

import com.example.person.dto.LocationDto;
import com.example.person.dto.WeatherDto;
import com.example.person.model.Person;
import com.example.person.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/person")
public class PersonController {

	@Autowired
	private PersonService personService;

	private RestTemplate restTemplate = new RestTemplate();

	@Value("${location.service.url}")
	private String locationServiceUrl;

	@Value("${weather.service.url}")
	private String weatherServiceUrl;

	@GetMapping
	public Iterable<Person> getAllPersons() {
		return personService.findAll();
	}

	@GetMapping("/{id}")
	public Person getPersonById(@PathVariable int id) {
		return personService.findById(id).get();
	}

	@GetMapping("/{id}/location")
	public LocationDto getPersonLocation(@PathVariable int id) {
		Person person = personService.findById(id).get();
		return restTemplate.getForObject(
				locationServiceUrl + "/location?name={name}",
				LocationDto.class,
				person.getLocation());
	}

	@GetMapping("/{id}/weather")
	public WeatherDto getPersonWeather(@PathVariable int id) {
		Person person = personService.findById(id).get();
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

	@PostMapping
	public ResponseEntity<Person> addPerson(@RequestBody Person person) {
		if (person.getId() != 0 && personService.existsById(person.getId())) {
			return ResponseEntity.badRequest().build();
		}
		Person saved = personService.save(person);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
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
