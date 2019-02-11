package com.github.madhurimamalla.connoisseur.server.similarity;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.madhurimamalla.connoisseur.server.model.Movie;
import com.github.madhurimamalla.connoisseur.server.service.MovieService;

public class MovieProviderImpl implements MovieProvider {

	private static final Logger LOG = LoggerFactory.getLogger(MovieProviderImpl.class);

	private MovieService svc;

	public MovieProviderImpl(MovieService svc) {
		this.svc = svc;
	}

	@Override
	public Set<Movie> getAllMovies() {
		Set<Movie> allMovies = new HashSet<Movie>(svc.getAllMovies());
		return allMovies;
	}

	@Override
	public Set<Movie> findAllMoviesByGenre(String genreName) {
		Set<Movie> moviesByGenre = new HashSet<Movie>(svc.getMoviesByGenre(genreName));
		return moviesByGenre;
	}

}