package com.github.madhurimamalla.connoisseur.server.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@Entity
@EnableAutoConfiguration
@Table(name = "similarity_relation")
public class SimilarityRelation {

	@Embeddable
	public static class Id implements Serializable {

		@Column(name = "source_tmdb_movie_id")
		protected long sourceMovieId;

		@Column(name = "target_tmdb_movie_id")
		protected long targetMovieId;

		public Id() {
		}

		public Id(long sourceMovieId, long targetMovieId) {
			super();
			this.sourceMovieId = sourceMovieId;
			this.targetMovieId = targetMovieId;
		}

		public long getSourceMovieId() {
			return sourceMovieId;
		}

		public void setSourceMovieId(long sourceMovieId) {
			this.sourceMovieId = sourceMovieId;
		}

		public long getTargetMovieId() {
			return targetMovieId;
		}

		public void setTargetMovieId(long targetMovieId) {
			this.targetMovieId = targetMovieId;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (int) (sourceMovieId ^ (sourceMovieId >>> 32));
			result = prime * result + (int) (targetMovieId ^ (targetMovieId >>> 32));
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
			if (sourceMovieId != other.sourceMovieId)
				return false;
			if (targetMovieId != other.targetMovieId)
				return false;
			return true;
		}

	}

	public SimilarityRelation() {

	}

	public SimilarityRelation(Movie source, Movie target, float similarityScore) {
		this.source = source;
		this.target = target;
		this.id.sourceMovieId = source.getTmdbMovieID();
		this.id.targetMovieId = target.getTmdbMovieID();
		this.similarityScore = similarityScore;
	}

	@EmbeddedId
	protected Id id = new Id();

	@ManyToOne
	@JoinColumn(name = "source_tmdb_movie_id", insertable = false, updatable = false)
	protected Movie source;

	@ManyToOne
	@JoinColumn(insertable = false, updatable = false)
	protected Movie target;

	@Column(name = "similarity_score")
	protected float similarityScore;

	public float getSimilarityScore() {
		return similarityScore;
	}

	public Movie getSource() {
		return source;
	}

	public Movie getTarget() {
		return target;
	}

}
