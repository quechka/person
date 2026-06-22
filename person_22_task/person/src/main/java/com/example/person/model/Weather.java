package com.example.person.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Weather {

	private double temp;

	@JsonProperty("feels_like")
	private double feelsLike;

	@JsonProperty("temp_min")
	private double tempMin;

	@JsonProperty("temp_max")
	private double tempMax;

	private int pressure;
	private int humidity;

	@JsonProperty("sea_level")
	private Integer seaLevel;

	@JsonProperty("grnd_level")
	private Integer grndLevel;
}
