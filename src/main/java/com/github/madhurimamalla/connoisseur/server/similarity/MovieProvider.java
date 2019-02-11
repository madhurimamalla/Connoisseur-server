package com.github.madhurimamalla.connoisseur.server.similarity;

import java.util.Set;

import com.github.madhurimamalla.connoisseur.server.model.Movie;

public interface MovieProvider {

	Set<Movie> getAllMovies();

	Set<Movie> findAllMoviesByGenre(String genreName);

}
