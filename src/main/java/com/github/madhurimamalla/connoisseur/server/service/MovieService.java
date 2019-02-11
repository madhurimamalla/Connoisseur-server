package com.github.madhurimamalla.connoisseur.server.service;

import java.util.List;
import java.util.Optional;

import com.github.madhurimamalla.connoisseur.server.model.Movie;
import com.github.madhurimamalla.connoisseur.server.model.Person;


public interface MovieService {

	Movie addMovie(Movie m);

	Optional<Person> findPersonById(long tmdbPersonId);

	List<Movie> getAllMovies();

	List<Movie> getMoviesByGenre(String genreName);

}