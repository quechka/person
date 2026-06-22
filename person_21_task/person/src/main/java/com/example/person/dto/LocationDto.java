package com.example.person.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {

	private int id;
	private String name;
	private double latitude;
	private double longitude;
}
