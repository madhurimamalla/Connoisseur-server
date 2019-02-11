package com.github.madhurimamalla.connoisseur.server.persistence;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.github.madhurimamalla.connoisseur.server.model.Genre;
import com.github.madhurimamalla.connoisseur.server.model.Movie;

public interface MovieRepository extends CrudRepository<Movie, Long> {

	List<Movie> findByTitle(String title);

	List<Movie> findByGenres(Genre genre);

}
