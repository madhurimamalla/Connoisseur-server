package com.github.madhurimamalla.connoisseur.server.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.madhurimamalla.connoisseur.server.model.Genre;
import com.github.madhurimamalla.connoisseur.server.model.Movie;
import com.github.madhurimamalla.connoisseur.server.model.Person;
import com.github.madhurimamalla.connoisseur.server.model.SimilarityRelation;
import com.github.madhurimamalla.connoisseur.server.persistence.GenreRepository;
import com.github.madhurimamalla.connoisseur.server.persistence.MovieRepository;
import com.github.madhurimamalla.connoisseur.server.persistence.PersonRepository;
import com.github.madhurimamalla.connoisseur.server.persistence.SimilarityRelationRepository;

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
	SimilarityRelationRepository srDAO;

	@Autowired
	EntityManagerFactory emf;

	/**
	 * Thread safe method to add a movie to the database
	 * 
	 * @return Movie
	 */
	@Override
	public synchronized Movie addMovie(Movie movie) {
		return repository.save(movie);
	}

	/**
	 * Finds and returns Person by it's id
	 * 
	 * @return Optional<Person>
	 */
	public Optional<Person> findPersonById(long tmdbPersonId) {
		return personDAO.findById(tmdbPersonId);
	}

	/**
	 * Gets all the movies by Genre
	 * 
	 * @return List<Movie>
	 */
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

	/**
	 * Gets all the movies from the database Also, loads the keywords and
	 * similar movies for every movie
	 * 
	 * @return List<Movie>
	 */
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
			if (!movie.getSimilarMovies().isEmpty()) {
				movie.getSimilarMovies().get(0);
			}
			listOfMovies.add(movie);
		}
		return listOfMovies;
	}

	/**
	 * Adds a similar relation to the table SIMILAR_RELATION
	 */
	@Override
	@Transactional
	public SimilarityRelation addSimilarityRelation(SimilarityRelation sr) {
		return srDAO.save(sr);
	}

	@Override
	public long findMaxId() {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("select max (tmdbMovieID) from Movie");
		Object id =  query.getFirstResult();
		return (long) id;
	}
}
