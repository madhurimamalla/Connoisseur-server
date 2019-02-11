package com.github.madhurimamalla.connoisseur.server.moviedb.client;

public class MovieNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long movieId;

	public MovieNotFoundException(Throwable cause) {
		super(cause);
	}

	public MovieNotFoundException(String message) {
		super(message);
	}

	public MovieNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public MovieNotFoundException(String message, long id) {
		super(message);
		this.movieId = id;
	}

	public long getMovieId() {
		return movieId;
	}
}
