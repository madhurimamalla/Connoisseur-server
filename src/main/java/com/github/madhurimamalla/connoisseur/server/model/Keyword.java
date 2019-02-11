package com.github.madhurimamalla.connoisseur.server.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Keyword {

	@Id
	private long tmdb_keyword_id;

	private String name;

	public Keyword() {

	}

	public Keyword(long tmdb_keyword_id, String name) {
		super();
		this.tmdb_keyword_id = tmdb_keyword_id;
		this.name = name;
	}

	public long getTmdb_keyword_id() {
		return tmdb_keyword_id;
	}

	public void setTmdb_keyword_id(long tmdb_keyword_id) {
		this.tmdb_keyword_id = tmdb_keyword_id;
	}

	public String getKeyword() {
		return name;
	}

	public void setKeyword(String keyword) {
		this.name = keyword;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (int) (tmdb_keyword_id ^ (tmdb_keyword_id >>> 32));
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
		Keyword other = (Keyword) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (tmdb_keyword_id != other.tmdb_keyword_id)
			return false;
		return true;
	}

}
