package com.github.madhurimamalla.connoisseur.server.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
public class Movie {

	@Id
	@Column(name = "tmdb_movie_id")
	private long tmdbMovieID;

	@Column(name = "title")
	private String title;

	@Lob
	@Column(name = "tagline")
	private String tagline;

	@Column(name = "release_date")
	private String releaseDate;

	@Column(name = "tmdb_poster_path")
	private String tmdbPosterPath;

	@Column(name = "imdb_movie_id")
	private String imdbID;

	@Column(name = "popularity")
	private float popularity;

	@Column(name = "vote_average")
	private float voteAverage;

	@Column(name = "vote_count")
	private long voteCount;

	@ManyToOne(cascade = CascadeType.ALL)
	private Language originalLanguage;

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "movie_languages", joinColumns = @JoinColumn(name = "tmdb_movie_id"), inverseJoinColumns = @JoinColumn(name = "lang_id"))
	private Set<Language> languages = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "movie_genres", joinColumns = @JoinColumn(name = "tmdb_movie_id"), inverseJoinColumns = @JoinColumn(name = "tmdb_genre_id"))
	private Set<Genre> genres = new HashSet<>();

	@Lob
	private String plotSummary;

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "movie_cast", joinColumns = @JoinColumn(name = "tmdb_movie_id"), inverseJoinColumns = @JoinColumn(name = "tmdb_person_id"))
	private List<Person> cast = new ArrayList<>();

	@Fetch(FetchMode.SUBSELECT)
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "movie_keywords", joinColumns = @JoinColumn(name = "tmdb_movie_id"), inverseJoinColumns = @JoinColumn(name = "tmdb_keyword_id"))
	private List<Keyword> keywords = new ArrayList<>();

	@OneToMany(mappedBy = "movie", cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	private List<MovieCrew> crew = new ArrayList<>();

	public Movie() {
	}

	public Movie(Long movieDbId, String title, String tagline, String plotSummary) {
		this.tmdbMovieID = movieDbId;
		this.title = title;
		this.tagline = tagline;
		this.plotSummary = plotSummary;
	}

	public long getTmdbMovieID() {
		return tmdbMovieID;
	}

	public void setTmdbMovieID(long tmdbMovieID) {
		this.tmdbMovieID = tmdbMovieID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTagline() {
		return tagline;
	}

	public void setTagline(String tagline) {
		this.tagline = tagline;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getTmdbPosterPath() {
		return tmdbPosterPath;
	}

	public void setTmdbPosterPath(String tmdbPosterPath) {
		this.tmdbPosterPath = tmdbPosterPath;
	}

	public String getImdbID() {
		return imdbID;
	}

	public void setImdbID(String imdbID) {
		this.imdbID = imdbID;
	}

	public float getPopularity() {
		return popularity;
	}

	public void setPopularity(float popularity) {
		this.popularity = popularity;
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

	public String getPlotSummary() {
		return plotSummary;
	}

	public void setPlotSummary(String plotSummary) {
		this.plotSummary = plotSummary;
	}

	public Set<Language> getLanguages() {
		return languages;
	}

	public void setLanguages(Set<Language> languages) {
		this.languages = languages;
	}

	public List<Person> getCast() {
		return cast;
	}

	public void setCast(List<Person> cast) {
		this.cast = cast;
	}

	public List<MovieCrew> getCrew() {
		return crew;
	}

	public void setCrew(List<MovieCrew> crew) {
		this.crew = crew;
	}

	public Set<Genre> getGenres() {
		return genres;
	}

	public void setGenres(Set<Genre> genres) {
		this.genres = genres;
	}

	public Language getOriginalLanguage() {
		return originalLanguage;
	}

	public void setOriginalLanguage(Language originalLanguage) {
		this.originalLanguage = originalLanguage;
	}

	public List<Keyword> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<Keyword> keywords) {
		this.keywords = keywords;
	}

	@Override
	public String toString() {
		return title + " [" + tmdbMovieID + "]";
	}

}
