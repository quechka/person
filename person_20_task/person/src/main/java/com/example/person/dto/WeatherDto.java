package com.example.person.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherDto {

	private String name;
	private double temp;
	private double feelsLike;
	private double tempMin;
	private double tempMax;
	private int pressure;
	private int humidity;
	private String description;
}
