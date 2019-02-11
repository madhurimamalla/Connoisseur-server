package com.github.madhurimamalla.connoisseur.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Language {

	@Id
	@Column(name = "lang_id")
	private String langID;

	@Column(name = "lang_name")
	private String name;

	public Language() {

	}

	public Language(String langID, String name) {
		super();
		this.langID = langID;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLangID() {
		return langID;
	}

	public void setLangID(String langID) {
		this.langID = langID;
	}

}
