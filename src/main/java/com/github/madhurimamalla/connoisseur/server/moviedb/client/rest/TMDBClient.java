package com.github.madhurimamalla.connoisseur.server.moviedb.client.rest;

import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.madhurimamalla.connoisseur.server.config.ReadConfig;
import com.github.madhurimamalla.connoisseur.server.moviedb.client.MovieDBClient;
import com.github.madhurimamalla.connoisseur.server.moviedb.client.MovieNotFoundException;
import com.github.madhurimamalla.connoisseur.server.moviedb.client.rest.model.CreditsRM;
import com.github.madhurimamalla.connoisseur.server.moviedb.client.rest.model.KeywordsRM;
import com.github.madhurimamalla.connoisseur.server.moviedb.client.rest.model.MovieRM;
import com.github.madhurimamalla.connoisseur.server.moviedb.client.rest.model.PersonRM;

public class TMDBClient implements MovieDBClient {

	private static final Logger LOG = LoggerFactory.getLogger(TMDBClient.class);

	private static final String BASE_URL = "api.themoviedb.org";

	private static final String CREDITS = "credits";

	private static final String KEYWORDS = "keywords";

	private static final String LATEST = "latest";

	private final static String QUERY_PARAM = "api_key";

	private final static String LANG_PARAM = "language";

	private final static String PAGE_PARAM = "page";

	private final static String LANG_VALUE = "en_US";

	private final static String PROPS_FILE = "local.properties";

	private static String API_KEY;

	public TMDBClient() {
		/**
		 * The API key is loaded from a properties file
		 */
		Properties prop = new ReadConfig().getPropValues(PROPS_FILE);
		LOG.info("Successfully fetched the properties from the local.properties file");
		API_KEY = prop.getProperty("api.token");
	}

	@Override
	public MovieRM getMovieById(long id) throws Exception {
		HttpClient client = HttpClientBuilder.create().build();

		URIBuilder builder = new URIBuilder();
		builder.setScheme("https").setHost(BASE_URL).setPath("/3/movie/" + id).setParameter(QUERY_PARAM, API_KEY)
				.setParameter(LANG_PARAM, LANG_VALUE).build();

		HttpResponse response = client.execute(new HttpGet(builder.build()));
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode == HttpStatus.SC_NOT_FOUND) {
			throw new MovieNotFoundException("Movie not found.");
		}
		if (statusCode != HttpStatus.SC_OK) {
			throw new Exception("Failed..." + statusCode);
		}
		ObjectMapper objectMapper = new ObjectMapper();
		MovieRM movie = objectMapper.readValue(response.getEntity().getContent(), MovieRM.class);
		return movie;
	}

	@Override
	public long getLatestMovieId() throws Exception {
		HttpClient client = HttpClientBuilder.create().build();

		URIBuilder builder = new URIBuilder();
		builder.setScheme("https").setHost(BASE_URL).setPath("/3/movie/" + LATEST).setParameter(QUERY_PARAM, API_KEY)
				.setParameter(LANG_PARAM, LANG_VALUE).build();

		HttpResponse response = client.execute(new HttpGet(builder.build()));
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode == HttpStatus.SC_NOT_FOUND) {
			throw new MovieNotFoundException("Movie not found.");
		}
		if (statusCode != HttpStatus.SC_OK) {
			throw new Exception("Failed..." + statusCode);
		}
		ObjectMapper objectMapper = new ObjectMapper();
		MovieRM movie = objectMapper.readValue(response.getEntity().getContent(), MovieRM.class);
		return movie.getId();
	}

	@Override
	public CreditsRM getMovieCredits(long movieId) throws Exception {

		HttpClient client = HttpClientBuilder.create().build();

		URIBuilder builder = new URIBuilder();
		builder.setScheme("https").setHost(BASE_URL).setPath("/3/movie/" + movieId + "/" + CREDITS)
				.setParameter(QUERY_PARAM, API_KEY).setParameter(LANG_PARAM, LANG_VALUE).build();

		HttpResponse response = client.execute(new HttpGet(builder.build()));
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode == HttpStatus.SC_NOT_FOUND) {
			throw new MovieNotFoundException("Credits not found for the movie.. ", movieId);
		}
		if (statusCode != HttpStatus.SC_OK) {
			throw new Exception("Failed..." + statusCode);
		}
		ObjectMapper objectMapper = new ObjectMapper();
		CreditsRM credits = objectMapper.readValue(response.getEntity().getContent(), CreditsRM.class);
		return credits;
	}

	@Override
	public PersonRM getPersonById(long personId) throws Exception {
		HttpClient client = HttpClientBuilder.create().build();

		URIBuilder builder = new URIBuilder();
		builder.setScheme("https").setHost(BASE_URL).setPath("/3/person/" + personId).setParameter(QUERY_PARAM, API_KEY)
				.setParameter(LANG_PARAM, LANG_VALUE).build();

		HttpResponse response = client.execute(new HttpGet(builder.build()));
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode == HttpStatus.SC_NOT_FOUND) {
			throw new Exception("Failed to find person: " + personId);
		}

		ObjectMapper objectMapper = new ObjectMapper();
		PersonRM person = objectMapper.readValue(response.getEntity().getContent(), PersonRM.class);
		return person;
	}

	@Override
	public KeywordsRM getKeywordsByMovieId(long movieId) throws Exception {
		HttpClient client = HttpClientBuilder.create().build();

		URIBuilder builder = new URIBuilder();
		builder.setScheme("https").setHost(BASE_URL).setPath("/3/movie/" + movieId + "/" + KEYWORDS)
				.setParameter(QUERY_PARAM, API_KEY).setParameter(LANG_PARAM, LANG_VALUE).build();

		HttpResponse response = client.execute(new HttpGet(builder.build()));
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode == HttpStatus.SC_NOT_FOUND) {
			throw new Exception("Failed to find keywords for movie :" + movieId);
		}
		ObjectMapper objectMapper = new ObjectMapper();
		KeywordsRM keywords = objectMapper.readValue(response.getEntity().getContent(), KeywordsRM.class);

		return keywords;
	}

}
