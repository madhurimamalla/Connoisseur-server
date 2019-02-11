package com.github.madhurimamalla.connoisseur.server.jobs;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.madhurimamalla.connoisseur.server.service.MovieService;
import com.github.madhurimamalla.connoisseur.server.similarity.KeywordBasedSimilarityAlgorithm;
import com.github.madhurimamalla.connoisseur.server.similarity.MovieProvider;
import com.github.madhurimamalla.connoisseur.server.similarity.MovieProviderImpl;
import com.github.madhurimamalla.connoisseur.server.similarity.SimilarityAlgorithm;
import com.github.madhurimamalla.connoisseur.server.similarity.SimilarityResult;

@Service
public class SimilarityInferenceJob {

	private static final Logger LOG = LoggerFactory.getLogger(SimilarityInferenceJob.class);

	@Autowired
	MovieService movieService;

	public Iterator<SimilarityResult> run() {

		MovieProvider mp = new MovieProviderImpl(movieService);
		SimilarityAlgorithm simAlgo = new KeywordBasedSimilarityAlgorithm();
		return simAlgo.run(mp);
	}

	public Iterator<SimilarityResult> run(String genreName) {
		
		MovieProvider mp = new MovieProviderImpl(movieService);
		SimilarityAlgorithm simAlgo = new KeywordBasedSimilarityAlgorithm();
		return simAlgo.run(mp, genreName);
	}
}