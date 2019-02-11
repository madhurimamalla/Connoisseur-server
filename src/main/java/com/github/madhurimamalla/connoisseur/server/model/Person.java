package com.github.madhurimamalla.connoisseur.server.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Person {

	@Id
	private long tmdbPersonId;

	private String imdbID;

	private String name;

	private String gender;

	private float popularity;

	private String birthday;

	public Person() {
		super();
	}

	public Person(long personID, String imdbID, String name, String gender) {
		super();
		this.tmdbPersonId = personID;
		this.imdbID = imdbID;
		this.name = name;
		this.gender = gender;
	}

	public String getImdbID() {
		return imdbID;
	}

	public void setImdbID(String imdbID) {
		this.imdbID = imdbID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public float getPopularity() {
		return popularity;
	}

	public void setPopularity(float popularity) {
		this.popularity = popularity;
	}

	public long getTmdbPersonId() {
		return tmdbPersonId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (tmdbPersonId ^ (tmdbPersonId >>> 32));
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
		Person other = (Person) obj;
		if (tmdbPersonId != other.tmdbPersonId)
			return false;
		return true;
	}

}
