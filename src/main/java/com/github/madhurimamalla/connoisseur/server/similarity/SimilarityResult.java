package com.github.madhurimamalla.connoisseur.server.similarity;

import java.util.List;

import com.github.madhurimamalla.connoisseur.server.model.Movie;

public class SimilarityResult {

	public static class SimilarMovie {

		private final Movie movie;

		private final float similarityScore;

		public SimilarMovie(Movie movie, float similarityScore) {
			this.movie = movie;
			this.similarityScore = similarityScore;
		}

		public Movie getMovie() {
			return movie;
		}

		public float getSimilarityScore() {
			return similarityScore;
		}

		@Override
		public String toString() {
			return "SimilarMovie [movie=" + movie + ", similarityScore=" + similarityScore + "]";
		}

	}

	private final Movie movie;

	private final List<SimilarMovie> similarMovies;

	public SimilarityResult(Movie movie, List<SimilarMovie> similarMovie) {
		this.movie = movie;
		this.similarMovies = similarMovie;
	}

	public Movie getMovie() {
		return movie;
	}

	@Override
	public String toString() {
		return "SimilarityResult [movie=" + movie + ", similarMovie=" + similarMovies + "]";
	}

	public List<SimilarMovie> getSimilarMovies() {
		return similarMovies;
	}

}
