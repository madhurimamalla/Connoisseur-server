package com.github.madhurimamalla.connoisseur.server.service;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.UnexpectedRollbackException;

import com.github.madhurimamalla.connoisseur.server.jobs.JobState;
import com.github.madhurimamalla.connoisseur.server.jobs.RunnableJob;
import com.github.madhurimamalla.connoisseur.server.model.JobHistory;
import com.github.madhurimamalla.connoisseur.server.model.JobParams;
import com.github.madhurimamalla.connoisseur.server.model.JobQueue;
import com.github.madhurimamalla.connoisseur.server.model.JobType;
import com.github.madhurimamalla.connoisseur.server.persistence.JobQueueRepository;
import com.github.madhurimamalla.connoisseur.server.persistence.JobRepository;

@Service
public class ConnoisseurJobService implements JobService {
	private static final Logger LOG = LoggerFactory.getLogger(ConnoisseurJobService.class);

	@Autowired
	JobRepository jobDAO;

	@Autowired
	JobQueueRepository jobQDAO;

	@Override
	@Transactional
	public JobHistory addJob(JobHistory job) throws JobTypeExistsException {
		try {
			JobQueue jobQ = new JobQueue();
			jobQ.setJobType(job.getJobType());
			jobQDAO.save(jobQ);
			jobDAO.save(job);
		} catch (DataIntegrityViolationException e) {
			if (e.getCause() instanceof ConstraintViolationException) {
				throw new JobTypeExistsException("Same job type is added to the job queue");
			}
		}
		return job;
	}

	@Override
	public void removeJob(JobHistory job) {
		/**
		 * Update specific job to FINISHED state
		 */
		updateJobStatus(job.getJobId(), JobState.FINISHED);
		/**
		 * Deleted from Job_Queue to make space for that type of incoming jobs
		 */
		jobQDAO.delete(jobQDAO.findByJobType(job.getJobType()));
	}

	@Override
	public void deleteTypeFromQ(JobHistory job) {
		jobQDAO.delete(jobQDAO.findByJobType(job.getJobType()));
	}

	@Override
	public boolean deleteJob(long jobId) {
		Optional<JobHistory> job = jobDAO.findById(jobId);
		if (job != null) {
			jobDAO.deleteById(jobId);
			return true;
		}
		return false;
	}

	@Override
	public Optional<JobHistory> findJobById(long id) {
		Optional<JobHistory> job = jobDAO.findById(id);
		if (job != null) {
			return job;
		}
		return null;
	}

	@Override
	@Transactional
	public void updateJobStatus(long jobId, JobState jobStatus) {
		Optional<JobHistory> opJob = jobDAO.findById(jobId);
		if (opJob != null) {
			JobHistory job = opJob.get();
			job.setJobStatus(jobStatus);
			jobDAO.save(job);
		} else {
			LOG.info("There's no job for this id.");
		}
	}

	@Override
	public long findMinId() {
		long id = this.jobDAO.findMinJobId();
		return id;
	}

	@Override
	public boolean deleteAll() {
		try {
			this.jobDAO.deleteAll();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public JobHistory updateJob(JobHistory job) {
		return this.jobDAO.save(job);
	}

	@Override
	public Iterable<JobHistory> findAll() {
		return this.jobDAO.findAll();
	}

	@Override
	@Transactional
	public JobHistory findNextJob() {
		JobHistory job = null;
		long id = this.jobDAO.findNextMinJobId();
		Optional<JobHistory> jobHistoryOp = this.jobDAO.findById(id);
		if (jobHistoryOp != null) {
			job = jobHistoryOp.get();
			Iterator<JobParams> itr = job.getJobParams().iterator();
			while (itr.hasNext()) {
				JobParams param = itr.next();
				if (!param.getKey().isEmpty()) {
					param.getKey();
				}
			}
		}
		return job;
	}

	@Override
	public List<JobParams> getJobParams(long jobId) {
		JobHistory job = null;
		Optional<JobHistory> jobHistoryOp = this.jobDAO.findById(jobId);
		if (jobHistoryOp != null) {
			job = jobHistoryOp.get();
			Iterator<JobParams> itr = job.getJobParams().iterator();
			while (itr.hasNext()) {
				JobParams param = itr.next();
				if (!param.getKey().isEmpty()) {
					param.getKey();
				}
			}
		}
		return job.getJobParams();
	}

	@Override
	public long countOfQueuedJobs() {
		return this.jobDAO.findNumberOfQueuedJobs();
	}

	@Override
	public void removeAllQueueJobs() {
		Iterator<JobQueue> itr = this.jobQDAO.findAll().iterator();
		while (itr.hasNext()) {
			this.jobQDAO.delete(itr.next());
		}
	}

	@Override
	public boolean existsByJobType(JobType jobType) {
		try {
			this.jobQDAO.findByJobType(jobType);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	@Transactional
	public void cleanUpJobs() {
		Iterator<JobHistory> jobsItr = this.jobDAO.findByjobStatus(JobState.RUNNING).iterator();
		while (jobsItr.hasNext()) {
			JobHistory jobHistory = jobsItr.next();
			JobQueue jq = this.jobQDAO.findByJobType(jobHistory.getJobType());
			this.jobQDAO.delete(jq);
			jobHistory.setJobStatus(JobState.FAILED);
			this.jobDAO.save(jobHistory);
		}
	}

}