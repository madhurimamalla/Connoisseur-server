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

	private static JobExecutor jobExecutor = null;

	private static JobScheduler instance = null;

	private Thread t1;

	synchronized public static JobScheduler getInstance(JobService jobService, MovieService movieService) {
		if (instance == null) {
			LOG.info("Scheduler instance created");
			// TODO Add this when the Application is launched
			// jobService.cleanUpJobs();
			instance = new JobScheduler(jobService, movieService);
		}
		return instance;
	}

	private JobScheduler(JobService jobService, MovieService movieService) {
		this.jobService = jobService;
		jobExecutor = getInstanceOfJobExecutor(jobService, movieService);
		t1 = new Thread(jobExecutor);
		t1.start();
	}

	private static JobExecutor getInstanceOfJobExecutor(JobService jobService, MovieService movieService) {
		if (jobExecutor == null) {
			jobExecutor = new JobExecutor(jobService, movieService);
		}
		return jobExecutor;
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
	 * 
	 * @param job
	 */
	public void cancel(JobHistory job) {
		if (job.getJobStatus().equals(JobState.RUNNING) && (jobService.existsByJobType(job.getJobType()) == true)) {
			/**
			 * Cancel a specific job
			 */
			LOG.info("JobScheduler will try to propogate the cancel call to JobExecutor");
			if (jobExecutor != null) {
				jobExecutor.cancelJob();
			}
		} else {
			/**
			 * Update the status to be cancelled
			 */
			LOG.info("This job isn't running yet so can just set the status to CANCELLED so"
					+ " it wouldn't be picked up by the JobExecutor");
			jobService.updateJobStatus(job.getJobId(), JobState.CANCELLED);
		}
	}
}
