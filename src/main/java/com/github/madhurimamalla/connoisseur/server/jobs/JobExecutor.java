package com.github.madhurimamalla.connoisseur.server.jobs;

import com.github.madhurimamalla.connoisseur.server.model.JobHistory;

import com.github.madhurimamalla.connoisseur.server.service.JobService;
import com.github.madhurimamalla.connoisseur.server.service.MovieService;

public final class JobExecutor implements Runnable {

	private JobService jobService;
	private MovieService movieService;

	public JobExecutor(JobService jobService, MovieService movieService) {
		this.jobService = jobService;
		this.movieService = movieService;
	}

	public RunnableJob getJobInstance(JobHistory job) {
		switch (job.getJobType()) {
		case DEFAULT:
			break;
		case FIREBASE_PUBLISH:
			break;
		case MOVIES_DOWNLOAD:
			return new SyncJob(jobService, job, movieService);
		case SIMILARITY_INFERENCE:
			break;
		default:
			break;

		}
		return null;
	}

	@Override
	public void run() {
		/**
		 * Check if there's any jobs pending/queued in the DB and run that and
		 * wait for it to complete
		 */
		while (true) {
			if(jobService.countOfQueuedJobs() > 0) {
				JobHistory job = jobService.findNextJob();
				if (job != null) {
					RunnableJob runnableJob = getJobInstance(job);
					if (runnableJob != null) {
						runnableJob.run();
					}
				}
			}
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

}
