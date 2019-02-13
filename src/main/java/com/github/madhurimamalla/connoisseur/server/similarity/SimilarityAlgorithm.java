package com.github.madhurimamalla.connoisseur.server.similarity;

public interface SimilarityAlgorithm {

	/**
	 * Runs similarity inference algorithm on a given set of movies.
	 * 
	 * @param movieProvider 
	 * @param publisher
	 */
	void run(MovieProvider movieProvider, SimilarityResultPublisher publisher);

}
