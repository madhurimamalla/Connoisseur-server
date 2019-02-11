package com.github.madhurimamalla.connoisseur.server.moviedb.client.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonRM {

	@JsonProperty("name")
	String name;

	@JsonProperty("id")
	long tmdbPersonId;

	@JsonProperty("imdb_id")
	String imdbPersonId;

	@JsonProperty("popularity")
	float popularity;

	@JsonProperty("birthday")
	String birthday;

	@JsonProperty("gender")
	String gender;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getTmdbPersonId() {
		return tmdbPersonId;
	}

	public void setTmdbPersonId(long tmdbPersonId) {
		this.tmdbPersonId = tmdbPersonId;
	}

	public String getImdbPersonId() {
		return imdbPersonId;
	}

	public void setImdbPersonId(String imdbPersonId) {
		this.imdbPersonId = imdbPersonId;
	}

	public float getPopularity() {
		return popularity;
	}

	public void setPopularity(float popularity) {
		this.popularity = popularity;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

}
