package com.github.madhurimamalla.connoisseur.server.moviedb.client;

import com.github.madhurimamalla.connoisseur.server.moviedb.client.rest.model.CreditsRM;
import com.github.madhurimamalla.connoisseur.server.moviedb.client.rest.model.KeywordsRM;
import com.github.madhurimamalla.connoisseur.server.moviedb.client.rest.model.MovieRM;
import com.github.madhurimamalla.connoisseur.server.moviedb.client.rest.model.PersonRM;

public interface MovieDBClient {

	MovieRM getMovieById(long id) throws Exception;

	long getLatestMovieId() throws Exception;

	CreditsRM getMovieCredits(long movieId) throws Exception;

	PersonRM getPersonById(long id) throws Exception;

	KeywordsRM getKeywordsByMovieId(long id) throws Exception;

}
