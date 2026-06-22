package com.example.location.repository;

import com.example.location.model.Location;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends CrudRepository<Location, Integer> {

	Optional<Location> findByName(String name);
}
