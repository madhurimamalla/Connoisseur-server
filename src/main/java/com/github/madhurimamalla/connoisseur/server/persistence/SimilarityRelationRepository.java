package com.github.madhurimamalla.connoisseur.server.persistence;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.github.madhurimamalla.connoisseur.server.model.Movie;
import com.github.madhurimamalla.connoisseur.server.model.SimilarityRelation;

public interface SimilarityRelationRepository extends CrudRepository<SimilarityRelation, SimilarityRelation.Id> {
	
	List<SimilarityRelation> findBySource(Movie movie);

}
