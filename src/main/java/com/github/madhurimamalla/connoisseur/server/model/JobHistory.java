package com.github.madhurimamalla.connoisseur.server.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import com.github.madhurimamalla.connoisseur.server.jobs.JobState;

@Entity
@EnableAutoConfiguration
@Table(name = "job_history")
public class JobHistory {

	@Id
	@SequenceGenerator(name = "job_seq", sequenceName = "job_id_sequence")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "job_seq")
	@Column(name = "jobId", updatable = false, nullable = false)
	private long jobId;

	private String jobName;

	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private JobState jobStatus;

	@Enumerated(EnumType.STRING)
	@Column(length = 30)
	private JobType jobType;

	@OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<JobStats> jobStats = new ArrayList<JobStats>();

	@OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<JobParams> jobParams = new ArrayList<JobParams>();

	public JobHistory() {
	}

	public JobHistory(String jobName, JobState jobStatus, JobType jobType, List<JobStats> jobStats) {
		super();
		this.jobName = jobName;
		this.jobStatus = jobStatus;
		this.jobType = jobType;
		this.jobStats = jobStats;
	}

	public long getJobId() {
		return jobId;
	}

	public JobState getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(JobState jobStatus) {
		this.jobStatus = jobStatus;
	}

	public JobType getJobType() {
		return jobType;
	}

	public void setJobType(JobType jobType) {
		this.jobType = jobType;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public List<JobStats> getJobStats() {
		return jobStats;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (jobId ^ (jobId >>> 32));
		result = prime * result + ((jobType == null) ? 0 : jobType.hashCode());
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
		JobHistory other = (JobHistory) obj;
		if (jobId != other.jobId)
			return false;
		if (jobType == null) {
			if (other.jobType != null)
				return false;
		} else if (!jobType.equals(other.jobType))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Job [jobId=" + jobId + ", jobName=" + jobName + ", jobStatus=" + jobStatus + ", jobType=" + jobType
				+ "]";
	}

	public List<JobParams> getJobParams() {
		return jobParams;
	}

}
