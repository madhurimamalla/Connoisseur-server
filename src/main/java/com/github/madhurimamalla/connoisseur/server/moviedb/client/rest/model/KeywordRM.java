package com.github.madhurimamalla.connoisseur.server.moviedb.client.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KeywordRM {

	@JsonProperty("name")
	String name;

	@JsonProperty("id")
	long tmdbKeywordId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getTmdbKeywordId() {
		return tmdbKeywordId;
	}

	public void setTmdbKeywordId(long tmdbKeywordId) {
		this.tmdbKeywordId = tmdbKeywordId;
	}

}
