package com.github.madhurimamalla.connoisseur.server.jobs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.madhurimamalla.connoisseur.server.model.CrewType;
import com.github.madhurimamalla.connoisseur.server.model.Genre;
import com.github.madhurimamalla.connoisseur.server.model.Keyword;
import com.github.madhurimamalla.connoisseur.server.model.Language;
import com.github.madhurimamalla.connoisseur.server.model.Movie;
import com.github.madhurimamalla.connoisseur.server.model.MovieCrew;
import com.github.madhurimamalla.connoisseur.server.model.Person;
import com.github.madhurimamalla.connoisseur.server.moviedb.client.MovieDBClient;
import com.github.madhurimamalla.connoisseur.server.moviedb.client.MovieNotFoundException;
import com.github.madhurimamalla.connoisseur.server.moviedb.client.rest.TMDBClient;
import com.github.madhurimamalla.connoisseur.server.moviedb.client.rest.model.CastRM;
import com.github.madhurimamalla.connoisseur.server.moviedb.client.rest.model.CreditsRM;
import com.github.madhurimamalla.connoisseur.server.moviedb.client.rest.model.CrewRM;
import com.github.madhurimamalla.connoisseur.server.moviedb.client.rest.model.GenreRM;
import com.github.madhurimamalla.connoisseur.server.moviedb.client.rest.model.KeywordRM;
import com.github.madhurimamalla.connoisseur.server.moviedb.client.rest.model.KeywordsRM;
import com.github.madhurimamalla.connoisseur.server.moviedb.client.rest.model.LanguageRM;
import com.github.madhurimamalla.connoisseur.server.moviedb.client.rest.model.MovieRM;
import com.github.madhurimamalla.connoisseur.server.moviedb.client.rest.model.PersonRM;
import com.github.madhurimamalla.connoisseur.server.service.MovieService;
import com.github.madhurimamalla.connoisseur.server.util.Broker;
import com.github.madhurimamalla.connoisseur.server.util.Message;

/**
 * Movie Downloader is the consumer thread which decodes the message and
 * downloads the movie. It also persists the movie object in the database
 * 
 * @author reema
 *
 */
public class MovieDownloader implements Runnable {

	private static final Logger LOG = LoggerFactory.getLogger(MovieDownloader.class);

	MovieService movieService;

	Broker<Long> broker;

	private String name;

	MovieDBClient client = new TMDBClient();

	Map<Long, Person> perMoviePersonMap = new HashMap<>();

	JobLog logger;

	public MovieDownloader(JobLog logger, String name, Broker<Long> broker, MovieService movieService) {
		this.logger = logger;
		this.name = name;
		this.broker = broker;
		this.movieService = movieService;
	}

	@Override
	public void run() {
		Long id = null;
		Thread.currentThread().setName(name);
		while (true) {
			try {
				Message<Long> message = broker.take();
				if (message.isPoisonPill()) {
					break;
				}
				id = message.getPayload();
				Movie m = fetchMovie(id);
				movieService.addMovie(m);
			} catch (MovieNotFoundException e) {
				logger.write("Movie with id [" + id + "] not found.");
			} catch (InterruptedException e) {
				logger.write("Movie download thread interrupted.");
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
		logger.write("Downloader thread finished.");
	}

	private Movie fetchMovie(long id) throws Exception {
		perMoviePersonMap.clear();

		MovieRM rm = null;
		rm = client.getMovieById(id);

		Objects.requireNonNull(rm);

		Movie movie = toDomainModel(rm);

		LOG.info("---------------------" + rm.getTitle() + "[" + id + "]--------------------");
		logger.write("Downloaded movie : " + rm.getTitle() + "[" + id + "]");
		CreditsRM creditsRM = null;
		try {
			creditsRM = client.getMovieCredits(id);
		} catch (MovieNotFoundException e) {
			LOG.info("Movie credits for movie with id [" + id + "] not found.");
			return null;
		}

		Objects.requireNonNull(creditsRM);
		Objects.requireNonNull(creditsRM.getCast());

		int i = 0;
		for (CastRM castRM : creditsRM.getCast()) {
			if (i >= 5) {
				break;
			}

			Person person = null;

			Optional<Person> cachedPerson = getCachedPerson(castRM.getTmdbPersonId());

			if (!cachedPerson.isPresent()) {
				/*
				 * Person not found in cache or database. Fetch person details.
				 */
				PersonRM personRM = client.getPersonById(castRM.getTmdbPersonId());

				Objects.requireNonNull(personRM);
				if (personRM.getName() == null) {
					logger.write("*******************************************");
					logger.write("Failed to fetch person with id: " + castRM.getTmdbPersonId());
					logger.write("*******************************************");
				}
				person = toDomainModel(personRM);
				cachePerson(person);
			} else {
				person = cachedPerson.get();
			}

			LOG.info("Adding cast. Name: " + person.getName() + " Person id: " + person.getTmdbPersonId());
			movie.getCast().add(person);
			i++;
		}

		List<CrewRM> crewRMs = creditsRM.getCrew();
		for (CrewRM crewRM : crewRMs) {
			if (crewRM.getJob().equalsIgnoreCase("director") || crewRM.getJob().equalsIgnoreCase("writer")) {

				Person person = null;

				Optional<Person> cachedPerson = getCachedPerson(crewRM.getTmdbPersonId());

				if (!cachedPerson.isPresent()) {
					/*
					 * Person not found in cache or database. Fetch person
					 * details.
					 */
					PersonRM personRM = client.getPersonById(crewRM.getTmdbPersonId());

					Objects.requireNonNull(personRM);
					if (personRM.getName() == null) {
						LOG.error("*******************************************");
						LOG.error("Failed to fetch person with id: " + crewRM.getTmdbPersonId());
						LOG.error("*******************************************");
					}
					person = toDomainModel(personRM);
					cachePerson(person);
				} else {
					person = cachedPerson.get();
				}

				LOG.info("Adding crew. Name: " + person.getName() + " Person id: " + person.getTmdbPersonId()
						+ " with CrewType: " + crewRM.getJob().toString());
				MovieCrew movieCrew = new MovieCrew(movie, person, CrewType.valueOf(crewRM.getJob().toUpperCase()));
				movie.getCrew().add(movieCrew);
			}
		}

		KeywordsRM keywordsRM = null;
		try {
			keywordsRM = client.getKeywordsByMovieId(id);
		} catch (Exception e) {
			LOG.info("Movie keywords for movie with id [" + id + "] not found.");
			return null;
		}

		Objects.requireNonNull(keywordsRM);
		Objects.requireNonNull(keywordsRM.getKeywords());
		List<Keyword> keywords = new ArrayList<>();
		for (KeywordRM keywordRM : keywordsRM.getKeywords()) {
			keywords.add(toDomainModel(keywordRM));
		}

		movie.setKeywords(keywords);

		return movie;
	}

	private void cachePerson(Person person) {
		perMoviePersonMap.put(person.getTmdbPersonId(), person);
	}

	private Optional<Person> getCachedPerson(long tmdbPersonId) {
		if (perMoviePersonMap.containsKey(tmdbPersonId)) {
			/*
			 * Person already created in a previous iteration.
			 */
			Person person = perMoviePersonMap.get(tmdbPersonId);
			return Optional.of(person);
		} else {
			/*
			 * Check if this person already exists in the database.
			 */
			Optional<Person> existingPerson = movieService.findPersonById(tmdbPersonId);
			if (existingPerson.isPresent()) {
				/*
				 * Cache the existing person.
				 */
				perMoviePersonMap.put(tmdbPersonId, existingPerson.get());
				LOG.info("Person with id [" + tmdbPersonId + "] already exists in database.");
			}
			return existingPerson;
		}
	}

	private Movie toDomainModel(MovieRM rm) {

		Movie m = new Movie();
		m.setTmdbMovieID(rm.getId());
		m.setTitle(rm.getTitle());
		m.setImdbID(rm.getImdbMovieId());
		m.setPlotSummary(rm.getOverview());
		m.setPopularity(rm.getPopularity());
		m.setTmdbPosterPath(rm.getPosterPath());
		m.setReleaseDate(rm.getReleaseDate());
		m.setTagline(rm.getTagline());
		m.setVoteAverage(rm.getVoteAverage());
		m.setVoteCount(rm.getVoteCount());

		List<GenreRM> genresRM = rm.getGenres();
		Set<Genre> genres = new HashSet<>();
		for (GenreRM g : genresRM) {
			genres.add(new Genre(g.getId(), g.getName().trim().toLowerCase()));
		}
		m.setGenres(genres);

		List<LanguageRM> langRMs = rm.getLanguages();
		Set<Language> langs = new HashSet<>();
		for (LanguageRM langRM : langRMs) {
			Language l = new Language(langRM.getLangCode(), langRM.getName());
			if (langRM.getLangCode().equals(rm.getOriginalLanguage())) {
				m.setOriginalLanguage(l);
			}
			langs.add(l);
		}
		m.setLanguages(langs);

		return m;
	}

	private Person toDomainModel(PersonRM rm) {
		Person p = new Person(rm.getTmdbPersonId(), rm.getImdbPersonId(), rm.getName(), rm.getGender());
		p.setPopularity(rm.getPopularity());
		p.setBirthday(rm.getBirthday());
		return p;
	}

	private Keyword toDomainModel(KeywordRM rm) {
		Keyword k = new Keyword(rm.getTmdbKeywordId(), rm.getName());
		return k;
	}

}
