package com.github.madhurimamalla.connoisseur.server.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "movie_crew")
public class MovieCrew {

	@Embeddable
	public static class Id implements Serializable {

		@Column(name = "tmdb_movie_id")
		protected long tmdbMovieId;

		@Column(name = "tmdb_person_id")
		protected long tmdbPersonId;

		@Enumerated(EnumType.STRING)
		@Column(name = "crew_type", length = 40)
		private CrewType crewType;

		public Id() {

		}

		public Id(long tmdbMovieId, long tmdbPersonId) {
			super();
			this.tmdbMovieId = tmdbMovieId;
			this.tmdbPersonId = tmdbPersonId;
		}

		public long getTmdbMovieId() {
			return tmdbMovieId;
		}

		public void setTmdbMovieId(long tmdbMovieId) {
			this.tmdbMovieId = tmdbMovieId;
		}

		public long getTmdbPersonId() {
			return tmdbPersonId;
		}

		public void setTmdbPersonId(long tmdbPersonId) {
			this.tmdbPersonId = tmdbPersonId;
		}

		public CrewType getCrewType() {
			return crewType;
		}

		public void setCrewType(CrewType crewType) {
			this.crewType = crewType;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((crewType == null) ? 0 : crewType.hashCode());
			result = prime * result + (int) (tmdbMovieId ^ (tmdbMovieId >>> 32));
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
			Id other = (Id) obj;
			if (crewType != other.crewType)
				return false;
			if (tmdbMovieId != other.tmdbMovieId)
				return false;
			if (tmdbPersonId != other.tmdbPersonId)
				return false;
			return true;
		}

	}

	public MovieCrew() {

	}

	public MovieCrew(Movie movie, Person person, CrewType crewType) {
		super();
		this.movie = movie;
		this.person = person;
		this.id.crewType = crewType;
		this.id.tmdbMovieId = movie.getTmdbMovieID();
		this.id.tmdbPersonId = person.getTmdbPersonId();
	}

	@EmbeddedId
	protected Id id = new Id();

	@ManyToOne
	@JoinColumn(name = "tmdb_movie_id", insertable = false, updatable = false)
	protected Movie movie;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "tmdb_person_id", insertable = false, updatable = false)
	protected Person person;

	public CrewType getCrewType() {
		return id.crewType;
	}

}
