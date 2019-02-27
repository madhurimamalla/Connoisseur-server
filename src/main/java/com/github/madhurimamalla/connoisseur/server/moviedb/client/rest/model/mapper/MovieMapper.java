package com.github.madhurimamalla.connoisseur.server.moviedb.client.rest.model.mapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.github.madhurimamalla.connoisseur.server.model.Genre;
import com.github.madhurimamalla.connoisseur.server.model.Language;
import com.github.madhurimamalla.connoisseur.server.model.Movie;
import com.github.madhurimamalla.connoisseur.server.moviedb.client.rest.model.GenreRM;
import com.github.madhurimamalla.connoisseur.server.moviedb.client.rest.model.LanguageRM;
import com.github.madhurimamalla.connoisseur.server.moviedb.client.rest.model.MovieRM;

/**
 * Maps the objects to its RM objects
 * 
 * @author reema
 *
 */
public class MovieMapper {

	public MovieMapper() {

	}

	public List<MovieRM> mapMoviesToMovieRMs(List<Movie> moviesList) {
		List<MovieRM> listOfMovieRMs = new ArrayList<>();
		Iterator<Movie> itr = moviesList.iterator();
		while (itr.hasNext()) {
			listOfMovieRMs.add(toMovieRMModel(itr.next()));
		}
		return listOfMovieRMs;
	}

	public MovieRM toMovieRMModel(Movie movie) {
		MovieRM movieRM = new MovieRM();
		movieRM.setTitle(movie.getTitle());
		movieRM.setId(movie.getTmdbMovieID());
		if (!movie.getGenres().isEmpty()) {
			List<Genre> genreList = new ArrayList<>(movie.getGenres());
			Iterator<Genre> itr = genreList.iterator();
			List<GenreRM> genreRMList = new ArrayList<>();
			while (itr.hasNext()) {
				genreRMList.add(toGenreRMModel(itr.next()));
			}
			movieRM.setGenres(genreRMList);
		}
		movieRM.setImdbMovieId(movie.getImdbID());
		if(movie.getOriginalLanguage()!=null){
			movieRM.setOriginalLanguage(movie.getOriginalLanguage().getName());	
		}
		if (!movie.getLanguages().isEmpty()) {
			List<Language> langsList = new ArrayList<>(movie.getLanguages());
			Iterator<Language> langItr = langsList.iterator();
			List<LanguageRM> langRMList = new ArrayList<>();
			while (langItr.hasNext()) {
				langRMList.add(toLanguageRMModel(langItr.next()));
			}
			movieRM.setLanguages(langRMList);
		}
		movieRM.setOverview(movie.getPlotSummary());
		movieRM.setPopularity(movie.getPopularity());
		movieRM.setVoteAverage(movie.getVoteAverage());
		movieRM.setVoteCount(movie.getVoteCount());
		movieRM.setPosterPath(movie.getTmdbPosterPath());
		movieRM.setTagline(movie.getTagline());
		movieRM.setReleaseDate(movie.getReleaseDate());
		return movieRM;
	}

	public LanguageRM toLanguageRMModel(Language lang) {
		LanguageRM langRM = new LanguageRM();
		langRM.setLangCode(lang.getLangID());
		langRM.setName(lang.getName());
		return langRM;
	}

	public GenreRM toGenreRMModel(Genre genre) {
		GenreRM genreRM = new GenreRM();
		genreRM.setId(genre.getTmdbGenreId());
		genreRM.setName(genre.getName());
		return genreRM;
	}

}
