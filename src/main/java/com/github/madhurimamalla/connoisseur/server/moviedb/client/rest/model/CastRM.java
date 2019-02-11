package com.github.madhurimamalla.connoisseur.server.moviedb.client.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CastRM {

	@JsonProperty("cast_id")
	String castId;

	@JsonProperty("gender")
	int gender;

	@JsonProperty("name")
	String name;

	@JsonProperty("id")
	long tmdbPersonId;

	public String getCastId() {
		return castId;
	}

	public void setCastId(String castId) {
		this.castId = castId;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

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
}
