package com.github.madhurimamalla.connoisseur.server.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.github.madhurimamalla.connoisseur.server.model.Genre;
import com.github.madhurimamalla.connoisseur.server.model.Movie;

public interface MovieRepository extends CrudRepository<Movie, Long> {

	List<Movie> findByTitle(String title);
	
	List<Movie> findByGenres(Genre genre);
	
	@Query("select max(tmdbMovieID) from Movie")
	Object findMaxId();
	
}
