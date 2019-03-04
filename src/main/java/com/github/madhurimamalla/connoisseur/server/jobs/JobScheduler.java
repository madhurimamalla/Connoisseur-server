package com.github.madhurimamalla.connoisseur.server.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import com.github.madhurimamalla.connoisseur.server.model.JobHistory;
import com.github.madhurimamalla.connoisseur.server.service.JobService;
import com.github.madhurimamalla.connoisseur.server.service.JobTypeExistsException;
import com.github.madhurimamalla.connoisseur.server.service.MovieService;

@Service
public class JobScheduler {

	private static final Logger LOG = LoggerFactory.getLogger(JobScheduler.class);

	JobService jobService;

	private JobExecutor jobExecutor;

	private static JobScheduler instance = null;

	private Thread t1;

	public static JobScheduler getInstance(JobService jobService, MovieService movieService) {
		if (instance == null) {
			jobService.cleanUpJobs();
			instance = new JobScheduler(jobService, movieService);
		}
		return instance;

	}

	private JobScheduler(JobService jobService, MovieService movieService) {
		this.jobService = jobService;
		jobExecutor = new JobExecutor(jobService, movieService);
		t1 = new Thread(jobExecutor);
		t1.start();
	}

	/**
	 * Schedules/Adds an entry into the JOBS_HISTORY & JOB_QUEUE
	 * 
	 * @param jobType
	 * @return JobHistory
	 */
	public JobHistory schedule(JobHistory jobToSchedule) {
		try {
			JobHistory job = jobService.addJob(jobToSchedule);
			LOG.info("Added a job in the JOB_HISTORY table with job id " + job.getJobId());
			return job;
		} catch (JobTypeExistsException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Removes all entries from the JOB_QUEUE
	 */
	public void removeJobsFromQueue() {
		try {
			jobService.removeAllQueueJobs();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void cancelAllJobs() {
		/**
		 * Query the JobHistory table and cancel all the jobs in it
		 */

	}

	/**
	 * This will attempt to cancel a job
	 * @param job
	 */
	public void cancel(JobHistory job) {
		if (job.getJobStatus().equals(JobState.RUNNING) && (jobService.existsByJobType(job.getJobType()) == true)) {
			/**
			 * Cancel a specific job
			 */
			if (jobExecutor != null) {
				jobExecutor.cancelJob();
			}
		} else {
			/**
			 * Update the status to be cancelled
			 */
			jobService.updateJobStatus(job.getJobId(), JobState.CANCELLED);
		}
	}
}
