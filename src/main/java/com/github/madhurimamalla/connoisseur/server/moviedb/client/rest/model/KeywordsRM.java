package com.github.madhurimamalla.connoisseur.server.moviedb.client.rest.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KeywordsRM {

	@JsonProperty("keywords")
	private List<KeywordRM> keywords;

	public List<KeywordRM> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<KeywordRM> keywords) {
		this.keywords = keywords;
	}

}
