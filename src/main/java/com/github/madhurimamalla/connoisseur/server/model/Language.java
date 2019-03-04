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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((langID == null) ? 0 : langID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Language other = (Language) obj;
		if (langID == null) {
			if (other.langID != null)
				return false;
		} else if (!langID.equals(other.langID))
			return false;
		return true;
	}

}
