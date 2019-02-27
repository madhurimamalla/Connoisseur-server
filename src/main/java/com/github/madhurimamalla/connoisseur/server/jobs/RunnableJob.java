package com.github.madhurimamalla.connoisseur.server.jobs;

import com.github.madhurimamalla.connoisseur.server.model.JobHistory;
import com.github.madhurimamalla.connoisseur.server.model.JobType;
import com.github.madhurimamalla.connoisseur.server.service.JobService;

public interface RunnableJob extends Runnable {
	
	public void cancel();
	
	public void updateJobType(JobType JOB_TYPE);

	public void updateJobStats(JobHistory job);

}