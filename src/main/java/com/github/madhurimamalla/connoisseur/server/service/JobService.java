package com.github.madhurimamalla.connoisseur.server.service;

import java.util.List;
import java.util.Optional;

import com.github.madhurimamalla.connoisseur.server.jobs.JobState;
import com.github.madhurimamalla.connoisseur.server.model.JobHistory;
import com.github.madhurimamalla.connoisseur.server.model.JobParams;
import com.github.madhurimamalla.connoisseur.server.model.JobQueue;
import com.github.madhurimamalla.connoisseur.server.model.JobType;

public interface JobService {

	JobHistory addJob(JobHistory job) throws JobTypeExistsException;

	Optional<JobHistory> findJobById(long id);

	long findMinId();

	Iterable<JobHistory> findAll();

	JobHistory findNextJob();

	JobHistory updateJob(JobHistory job);

	void updateJobStatus(long jobId, JobState jobStatus);

	boolean deleteAll();

	void removeJob(JobHistory job);

	boolean deleteJob(long jobId);

	void deleteTypeFromQ(JobHistory job);
	
	long countOfQueuedJobs();

	List<JobParams> getJobParams(long jobId);

	void removeAllQueueJobs();

	void cleanUpJobs();

	boolean existsByJobType(JobType jobType);

}
