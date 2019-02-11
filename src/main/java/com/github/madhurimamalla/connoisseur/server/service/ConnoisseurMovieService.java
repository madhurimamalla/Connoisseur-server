package com.github.madhurimamalla.connoisseur.server.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.madhurimamalla.connoisseur.server.model.Genre;
import com.github.madhurimamalla.connoisseur.server.model.Movie;
import com.github.madhurimamalla.connoisseur.server.model.Person;
import com.github.madhurimamalla.connoisseur.server.persistence.GenreRepository;
import com.github.madhurimamalla.connoisseur.server.persistence.MovieRepository;
import com.github.madhurimamalla.connoisseur.server.persistence.PersonRepository;

@Service
public class ConnoisseurMovieService implements MovieService {

	private static final Logger LOG = LoggerFactory.getLogger(ConnoisseurMovieService.class);

	@Autowired
	MovieRepository repository;

	@Autowired
	PersonRepository personDAO;

	@Autowired
	GenreRepository genreDAO;

	@Autowired
	EntityManagerFactory emf;

	@Override
	public synchronized Movie addMovie(Movie movie) {
		return repository.save(movie);
	}

	public Optional<Person> findPersonById(long tmdbPersonId) {
		return personDAO.findById(tmdbPersonId);
	}

	@Override
	@Transactional
	public List<Movie> getMoviesByGenre(String name) {
		Genre genre = genreDAO.findGenreByName(name);
		List<Movie> listOfMovies = new ArrayList<>();
		Iterator<Movie> itr = repository.findByGenres(genre).iterator();
		while (itr.hasNext()) {
			Movie movie = itr.next();
			if (!movie.getKeywords().isEmpty()) {
				movie.getKeywords().get(0);
			}
			listOfMovies.add(movie);
		}
		return listOfMovies;
	}

	@Override
	@Transactional
	public List<Movie> getAllMovies() {
		Iterator<Movie> iterator = repository.findAll().iterator();
		List<Movie> listOfMovies = new ArrayList<>();
		while (iterator.hasNext()) {
			Movie movie = iterator.next();
			if (!movie.getKeywords().isEmpty()) {
				movie.getKeywords().get(0);
			}
			listOfMovies.add(movie);
		}
		return listOfMovies;
	}
}
