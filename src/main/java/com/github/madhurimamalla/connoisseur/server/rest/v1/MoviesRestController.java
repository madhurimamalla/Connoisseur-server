package com.github.madhurimamalla.connoisseur.server.rest.v1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.madhurimamalla.connoisseur.server.jobs.MovieSyncJob;
import com.github.madhurimamalla.connoisseur.server.model.Movie;
import com.github.madhurimamalla.connoisseur.server.model.SimilarityRelation;
import com.github.madhurimamalla.connoisseur.server.moviedb.client.rest.TMDBClient;
import com.github.madhurimamalla.connoisseur.server.moviedb.client.rest.model.MovieRM;
import com.github.madhurimamalla.connoisseur.server.moviedb.client.rest.model.mapper.MovieMapper;
import com.github.madhurimamalla.connoisseur.server.persistence.MovieRepository;
import com.github.madhurimamalla.connoisseur.server.persistence.SimilarityRelationRepository;
import com.github.madhurimamalla.connoisseur.server.service.MovieService;

@RestController
public class MoviesRestController {

	private static final Logger LOG = LoggerFactory.getLogger(MoviesRestController.class);

	@Autowired
	MovieRepository repositoryService;

	@Autowired
	MovieService movieService;

	@Autowired
	SimilarityRelationRepository srr;

	@Autowired
	MovieSyncJob job;

	@RequestMapping("/movies/sync")
	public String sync() {
		try {
			LOG.info("The sync job has started...");
			job.start();
		} catch (Exception e) {
			return "Sync job failed with exception" + e.getMessage();
		}
		return "Sync job succeeded";
	}

	@RequestMapping(value = "/movies/sync/updatedb", method = RequestMethod.GET)
	public String updateDBToLatestTMDBId() {

		try {
			// TODO Fix that finding of max of the ids
			long lastIdInDB = movieService.findMaxId();
			TMDBClient client = new TMDBClient();
			long latestIdOnTMDB = 1;
			try {
				latestIdOnTMDB = (long) client.getLatestMovieId();
			} catch (Exception e) {
				e.printStackTrace();
			} 
			LOG.info("The sync job has started.....");
			job.start(lastIdInDB, latestIdOnTMDB);
		} catch (Exception e) {
			return "Sync job failed with exception: " + e.getMessage();
		}
		return "Sync job succeeded";
	}

	@RequestMapping(value = "/movies", method = RequestMethod.POST)
	public Long add(@RequestParam(value = "title") String title, @RequestParam(value = "movieDbId") long movieDbId) {
		Movie m = new Movie(movieDbId, title, "", "");
		Movie saved = repositoryService.save(m);
		// return saved.getId();
		return 1l;
	}

	@RequestMapping(value = "/movies", method = RequestMethod.GET)
	public List<MovieRM> getAllMovies() {
		try {
			Iterable<Movie> movies = repositoryService.findAll();
			List<MovieRM> moviesList = new ArrayList<>();
			MovieMapper mapper = new MovieMapper();
			for (Movie m : movies) {
				moviesList.add(mapper.toMovieRMModel(m));
			}
			return moviesList;
		} catch (Exception e) {
			LOG.error("Fetching all movies failed with an exception : " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/similar", method = RequestMethod.GET)
	public List<MovieRM> getSimilarMovies(@RequestParam(value = "id") long movieId) {
		try {
			Optional<Movie> m = repositoryService.findById(movieId);
			List<SimilarityRelation> similarMovies = new ArrayList<>();
			List<MovieRM> movies = new ArrayList<>();
			if (m.get() != null) {
				similarMovies = srr.findBySource(m.get());
				Iterator<SimilarityRelation> itr = similarMovies.iterator();
				MovieMapper mapper = new MovieMapper();
				while (itr.hasNext()) {
					movies.add(mapper.toMovieRMModel(itr.next().getTarget()));
				}
			}
			return movies;
		} catch (Exception e) {
			LOG.error("Similar movies search failed with exception: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
}