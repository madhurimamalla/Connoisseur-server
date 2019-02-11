package com.github.madhurimamalla.connoisseur.server.moviedb.client.rest.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieRM {

	@JsonProperty("id")
	long id;

	@JsonProperty("imdb_id")
	String imdbMovieId;

	@JsonProperty("title")
	String title;

	@JsonProperty("overview")
	String overview;

	@JsonProperty("release_date")
	String releaseDate;

	@JsonProperty("tagline")
	String tagline;

	@JsonProperty("vote_average")
	float voteAverage;

	@JsonProperty("vote_count")
	long voteCount;

	@JsonProperty("poster_path")
	String posterPath;

	@JsonProperty("popularity")
	float popularity;

	@JsonProperty("spoken_languages")
	List<LanguageRM> languages;

	@JsonProperty("original_language")
	String originalLanguage;

	@JsonProperty("genres")
	List<GenreRM> genres;

	public MovieRM() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getTagline() {
		return tagline;
	}

	public void setTagline(String tagline) {
		this.tagline = tagline;
	}

	public String getImdbMovieId() {
		return imdbMovieId;
	}

	public void setImdbMovieId(String imdbMovieId) {
		this.imdbMovieId = imdbMovieId;
	}

	public float getVoteAverage() {
		return voteAverage;
	}

	public void setVoteAverage(float voteAverage) {
		this.voteAverage = voteAverage;
	}

	public long getVoteCount() {
		return voteCount;
	}

	public void setVoteCount(long voteCount) {
		this.voteCount = voteCount;
	}

	public String getPosterPath() {
		return posterPath;
	}

	public void setPosterPath(String posterPath) {
		this.posterPath = posterPath;
	}

	public float getPopularity() {
		return popularity;
	}

	public void setPopularity(float popularity) {
		this.popularity = popularity;
	}

	public List<LanguageRM> getLanguages() {
		return languages;
	}

	public void setLanguages(List<LanguageRM> languages) {
		this.languages = languages;
	}

	public List<GenreRM> getGenres() {
		return genres;
	}

	public void setGenres(List<GenreRM> genres) {
		this.genres = genres;
	}

	public String getOriginalLanguage() {
		return originalLanguage;
	}

	public void setOriginalLanguage(String originalLanguage) {
		this.originalLanguage = originalLanguage;
	}

}
