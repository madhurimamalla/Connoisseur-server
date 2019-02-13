package com.github.madhurimamalla.connoisseur.server.similarity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.github.madhurimamalla.connoisseur.server.model.Movie;
import com.github.madhurimamalla.connoisseur.server.model.SimilarityRelation;
import com.github.madhurimamalla.connoisseur.server.service.MovieService;
import com.github.madhurimamalla.connoisseur.server.similarity.SimilarityResult.SimilarMovie;

public class SimilarityResultPublisherImpl implements SimilarityResultPublisher {

	private MovieService movieService;

	public SimilarityResultPublisherImpl(MovieService movieService) {
		this.movieService = movieService;
	}

	/**
	 * Method to take the SimilarityResult object and convert it to a list of
	 * SimilarRelations so it can persisted in the database
	 */
	@Override
	public void saveResult(SimilarityResult sr) {
		List<SimilarityRelation> list = new ArrayList<SimilarityRelation>();
		Movie m = sr.getMovie();
		Iterator<SimilarMovie> similarMovieItr = sr.getSimilarMovies().iterator();
		while (similarMovieItr.hasNext()) {
			SimilarMovie sm = similarMovieItr.next();
			list.add(new SimilarityRelation(m, sm.getMovie(), sm.getSimilarityScore()));
			list.add(new SimilarityRelation(sm.getMovie(), m, sm.getSimilarityScore()));
		}
		persist(list);

	}

	private void persist(List<SimilarityRelation> listOfSimilarRelations) {
		Iterator<SimilarityRelation> itr = listOfSimilarRelations.iterator();
		while (itr.hasNext()) {
			movieService.addSimilarityRelation(itr.next());
		}
	}

}
