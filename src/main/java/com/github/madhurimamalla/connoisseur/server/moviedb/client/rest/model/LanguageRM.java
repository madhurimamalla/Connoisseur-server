package com.github.madhurimamalla.connoisseur.server.moviedb.client.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LanguageRM {

	@JsonProperty("iso_639_1")
	String langCode;

	@JsonProperty("name")
	String name;

	public String getLangCode() {
		return langCode;
	}

	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
