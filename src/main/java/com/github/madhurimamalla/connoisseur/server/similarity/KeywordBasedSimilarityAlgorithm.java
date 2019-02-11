package com.github.madhurimamalla.connoisseur.server.similarity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.madhurimamalla.connoisseur.server.model.Movie;
import com.github.madhurimamalla.connoisseur.server.similarity.SimilarityResult.SimilarMovie;

public class KeywordBasedSimilarityAlgorithm implements SimilarityAlgorithm {

	private static final Logger LOG = LoggerFactory.getLogger(KeywordBasedSimilarityAlgorithm.class);

	@Override
	public Iterator<SimilarityResult> run(MovieProvider movieProvider, String genreName) {

		Set<Movie> allMovies = movieProvider.findAllMoviesByGenre(genreName);
		return findSimilarityResults(allMovies);
	}

	@Override
	public Iterator<SimilarityResult> run(MovieProvider movieProvider) {

		Set<Movie> allMovies = movieProvider.getAllMovies();
		return findSimilarityResults(allMovies);
	}

	private Iterator<SimilarityResult> findSimilarityResults(Set<Movie> moviesList) {

		Set<SimilarityResult> results = new HashSet<>();
		long count = 0;
		for (Movie outerM : moviesList) {
			List<SimilarMovie> similarMovies = new ArrayList<SimilarMovie>();
			if (outerM.getKeywords().isEmpty() || outerM.getKeywords() == null) {
				continue;
			}
			for (Movie innerM : moviesList) {
				float similarityScore = 0;
				if (innerM.equals(outerM)) {
					continue;
				}
				else if (innerM.getKeywords().isEmpty() || innerM.getKeywords() == null) {
					similarityScore = 0;
				} else {
					similarityScore = JaccardSimilarity.compute(new HashSet<>(innerM.getKeywords()),
							new HashSet<>(outerM.getKeywords()));
				}
				if (similarityScore < 0.1f) {
					continue;
				}
				count++;
				SimilarMovie similarMovie = new SimilarMovie(innerM, similarityScore);
				similarMovies.add(similarMovie);
			}
			// TODO Revisit this because we aren't saving similarity if there's
			// no similarity found for a movie
			if (similarMovies.size() > 0) {
				SimilarityResult simResult = new SimilarityResult(outerM, similarMovies);
				results.add(simResult);
			}
		}
		LOG.info("Total similarity results found: " + (count / 2));
		return results.iterator();
	}

}
