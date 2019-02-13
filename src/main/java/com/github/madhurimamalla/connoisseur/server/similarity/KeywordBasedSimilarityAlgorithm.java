package com.github.madhurimamalla.connoisseur.server.similarity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.madhurimamalla.connoisseur.server.model.Movie;
import com.github.madhurimamalla.connoisseur.server.similarity.SimilarityResult.SimilarMovie;

public class KeywordBasedSimilarityAlgorithm implements SimilarityAlgorithm {

	private static final Logger LOG = LoggerFactory.getLogger(KeywordBasedSimilarityAlgorithm.class);

	@Override
	public void run(MovieProvider movieProvider, SimilarityResultPublisher publisher) {

		Set<Movie> allMovies = movieProvider.getAllMovies();

		List<Movie> moviesList = new ArrayList<>(allMovies);
		long count = 0;
		for (int i = 0; i < moviesList.size(); i++) {
			Movie outerMovie = moviesList.get(i);
			if (outerMovie.getKeywords().isEmpty() || outerMovie.getKeywords() == null) {
				continue;
			}
			List<SimilarMovie> similarMovies = new ArrayList<SimilarMovie>();
			for (int j = i + 1; j < moviesList.size(); j++) {
				float similarityScore = 0;
				Movie innerMovie = moviesList.get(j);
				if (innerMovie.getKeywords().isEmpty() || innerMovie.getKeywords() == null) {
					similarityScore = 0;
				} else {
					similarityScore = JaccardSimilarity.compute(new HashSet<>(innerMovie.getKeywords()),
							new HashSet<>(outerMovie.getKeywords()));
				}
				if (similarityScore < 0.1f) {
					continue;
				}
				count++;
				similarMovies.add(new SimilarMovie(innerMovie, similarityScore));
			}
			if (similarMovies.size() > 0) {
				publisher.saveResult(new SimilarityResult(outerMovie, similarMovies));
			}
		}
		LOG.info("Total similarity results found: " + count);
	}

}
