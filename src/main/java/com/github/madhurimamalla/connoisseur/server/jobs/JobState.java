package com.github.madhurimamalla.connoisseur.server.jobs;

public enum JobState {

	/*
	 * Different states of a Job
	 * 
	 * Queued --> Running --> Finished; Queued --> Canceled; Queued --> Running -->
	 * Canceled
	 * 
	 */

	QUEUED, RUNNING, FAILED, CANCELED, FINISHED

}
