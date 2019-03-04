package com.github.madhurimamalla.connoisseur.server.service;

import java.util.List;
import java.util.Optional;

import com.github.madhurimamalla.connoisseur.server.model.Movie;
import com.github.madhurimamalla.connoisseur.server.model.Person;
import com.github.madhurimamalla.connoisseur.server.model.SimilarityRelation;

public interface MovieService {

	Movie addMovie(Movie m);

	Optional<Person> findPersonById(long tmdbPersonId);

	List<Movie> getAllMovies();

	List<Movie> getMoviesByGenre(String genreName);

	SimilarityRelation addSimilarityRelation(SimilarityRelation sr);

	long findMaxId();

	List<Movie> getRandom(long number);
}