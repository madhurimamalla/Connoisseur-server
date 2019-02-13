package com.github.madhurimamalla.connoisseur.server;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.madhurimamalla.connoisseur.server.jobs.MovieSyncJob;
import com.github.madhurimamalla.connoisseur.server.jobs.SimilarityInferenceJob;
import com.github.madhurimamalla.connoisseur.server.moviedb.client.rest.TMDBClient;
import com.github.madhurimamalla.connoisseur.server.persistence.MovieRepository;
import com.github.madhurimamalla.connoisseur.server.service.MovieService;

@RunWith(SpringRunner.class)
@Configuration
@TestPropertySource("classpath:application.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ConnoisseurApplicationTests {

	private static final Logger LOG = LoggerFactory.getLogger(ConnoisseurApplicationTests.class);

	@Autowired
	MovieSyncJob movieSyncJob;

	@Autowired
	MovieRepository movieRepository;

	@Autowired
	MovieService movieService;

	@Autowired
	SimilarityInferenceJob sij;

	@Test()
	public void testDownloadJob() throws Exception {
		movieSyncJob.start();
	}

	@Test
	public void testSimilarityInferenceJob() {
		sij.run();
	}

	@Test
	public void testTMDBLatestMovieAPI() throws Exception {
		TMDBClient client = new TMDBClient();
		long id = client.getLatestMovieId();
		LOG.info("Latest movie id is: " + id);
		assertNotNull(id);
	}

}