package com.github.madhurimamalla.connoisseur.server.moviedb.client.rest.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreditsRM {

	@JsonProperty("cast")
	List<CastRM> cast;

	@JsonProperty("crew")
	List<CrewRM> crew;

	public List<CastRM> getCast() {
		return cast;
	}

	public void setCast(List<CastRM> cast) {
		this.cast = cast;
	}

	public List<CrewRM> getCrew() {
		return crew;
	}

	public void setCrew(List<CrewRM> crew) {
		this.crew = crew;
	}

}
