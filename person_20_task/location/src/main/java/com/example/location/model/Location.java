package com.example.location.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Location {

	@Id
	@GeneratedValue
	private int id;

	@NonNull
	private String name;

	@NonNull
	private double latitude;

	@NonNull
	private double longitude;

	public Location(@NonNull String name, double latitude, double longitude) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}
}
