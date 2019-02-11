package com.github.madhurimamalla.connoisseur.server.persistence;

import org.springframework.data.repository.CrudRepository;

import com.github.madhurimamalla.connoisseur.server.model.Genre;

public interface GenreRepository extends CrudRepository<Genre, Long> {

	Genre findGenreByName(String name);

}
