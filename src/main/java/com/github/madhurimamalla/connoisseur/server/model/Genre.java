package com.github.madhurimamalla.connoisseur.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Genre {

	@Id
	@Column(name = "tmdb_genre_id")
	private long tmdbGenreId;

	@Column(name = "name")
	private String name;

	public Genre() {

	}

	public Genre(long tmdbGenreId, String name) {
		super();
		this.tmdbGenreId = tmdbGenreId;
		this.name = name;
	}

	public long getTmdbGenreId() {
		return tmdbGenreId;
	}

	public void setTmdbGenreId(long tmdbGenreId) {
		this.tmdbGenreId = tmdbGenreId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
