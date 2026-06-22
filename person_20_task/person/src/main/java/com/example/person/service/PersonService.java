package com.example.person.service;

import com.example.person.dto.LocationDto;
import com.example.person.dto.WeatherDto;
import com.example.person.model.Person;
import com.example.person.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

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

	public Person save(Person person) {
		return repository.save(person);
	}

	public boolean existsById(int id) {
		return repository.existsById(id);
	}

	public Optional<LocationDto> getLocationForPerson(int id) {
		return repository.findById(id).flatMap(this::fetchLocation);
	}

	public Optional<WeatherDto> getWeatherForPerson(int id) {
		return repository.findById(id).flatMap(person ->
				fetchLocation(person).flatMap(this::fetchWeather));
	}

	private Optional<LocationDto> fetchLocation(Person person) {
		try {
			LocationDto location = restTemplate.getForObject(
					locationServiceUrl + "/location/name/{name}",
					LocationDto.class,
					person.getLocation());
			return Optional.ofNullable(location);
		} catch (RestClientException e) {
			return Optional.empty();
		}
	}

	private Optional<WeatherDto> fetchWeather(LocationDto location) {
		try {
			Map<?, ?> root = restTemplate.getForObject(
					weatherServiceUrl + "/weather?lat={lat}&lon={lon}",
					Map.class,
					location.getLatitude(),
					location.getLongitude());

			if (root == null) {
				return Optional.empty();
			}

			Map<String, Object> main = asMap(root.get("main"));
			if (main == null) {
				return Optional.empty();
			}

			return Optional.of(new WeatherDto(
					(String) root.get("name"),
					getDouble(main, "temp"),
					getDouble(main, "feels_like", "feelsLike"),
					getDouble(main, "temp_min", "tempMin"),
					getDouble(main, "temp_max", "tempMax"),
					getInt(main, "pressure"),
					getInt(main, "humidity"),
					getDescription(root)));
		} catch (RestClientException e) {
			return Optional.empty();
		}
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
