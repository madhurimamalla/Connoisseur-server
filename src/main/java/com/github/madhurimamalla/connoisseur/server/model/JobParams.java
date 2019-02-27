package com.github.madhurimamalla.connoisseur.server.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@Entity
@EnableAutoConfiguration
@Table(name = "job_params")
public class JobParams {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long jobParamsId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "JOB_ID")
	public JobHistory job;

	private String key;

	private String value;

	public JobParams() {
	}

	public JobParams(JobHistory job, String key, String value) {
		this.job = job;
		this.key = key;
		this.value = value;
	}

	public JobHistory getJob() {
		return job;
	}

	public void setJob(JobHistory job) {
		this.job = job;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "JobParams [key=" + key + ", value=" + value + "]";
	}

}
