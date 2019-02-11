package com.github.madhurimamalla.connoisseur.server.rest.v1;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.madhurimamalla.connoisseur.server.jobs.MovieSyncJob;
import com.github.madhurimamalla.connoisseur.server.model.Movie;
import com.github.madhurimamalla.connoisseur.server.persistence.MovieRepository;

@RestController
public class MoviesRestController {

	private static final Logger LOG = LoggerFactory.getLogger(MoviesRestController.class);

	@Autowired
	MovieRepository repositoryService;

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

	@RequestMapping(value = "/movies", method = RequestMethod.POST)
	public Long add(@RequestParam(value = "title") String title, @RequestParam(value = "movieDbId") long movieDbId) {
		Movie m = new Movie(movieDbId, title, "", "");
		Movie saved = repositoryService.save(m);
		// return saved.getId();
		return 1l;
	}

	@RequestMapping(value = "/movies", method = RequestMethod.GET)
	public List<String> getAllMovies() {
		Iterable<Movie> movies = repositoryService.findAll();
		List<String> moviesList = new ArrayList<>();
		for (Movie m : movies) {
			// moviesList.add(m.getId() + ": " + m.getTitle() + "[" +
			// m.getMovieDbId() + "]");
		}
		return moviesList;
	}
}
