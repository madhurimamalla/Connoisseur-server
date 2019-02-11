package com.github.madhurimamalla.connoisseur.server.moviedb.client.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CrewRM {

	@JsonProperty("gender")
	int gender;

	@JsonProperty("name")
	String name;

	@JsonProperty("id")
	long tmdbPersonId;

	@JsonProperty("job")
	String job;

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

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}
}
