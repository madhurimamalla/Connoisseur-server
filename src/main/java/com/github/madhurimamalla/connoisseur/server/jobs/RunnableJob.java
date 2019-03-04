package com.github.madhurimamalla.connoisseur.server.jobs;

import com.github.madhurimamalla.connoisseur.server.model.JobHistory;
import com.github.madhurimamalla.connoisseur.server.model.JobType;

public interface RunnableJob extends Runnable {
	
	public static enum JobResult {
		SUCCESS, FAILURE, CANCELLED
	}
	
	public void cancel();
	
	public void updateJobType(JobType JOB_TYPE);

	public void updateJobStats(JobHistory job);

}