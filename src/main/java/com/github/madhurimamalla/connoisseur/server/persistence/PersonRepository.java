package com.github.madhurimamalla.connoisseur.server.persistence;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.github.madhurimamalla.connoisseur.server.model.Person;

public interface PersonRepository extends CrudRepository<Person, Long> {

	List<Person> findByName(String name);

}
