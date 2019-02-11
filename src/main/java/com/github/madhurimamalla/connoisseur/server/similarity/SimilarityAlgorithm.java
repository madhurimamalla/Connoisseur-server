package com.github.madhurimamalla.connoisseur.server.similarity;

import java.util.Iterator;

public interface SimilarityAlgorithm {

	Iterator<SimilarityResult> run(MovieProvider movieProvider, String genreName);

	Iterator<SimilarityResult> run(MovieProvider movieProvider);

}
