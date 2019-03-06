package com.github.madhurimamalla.connoisseur.server.jobs;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.madhurimamalla.connoisseur.server.model.JobHistory;
import com.github.madhurimamalla.connoisseur.server.model.JobParams;
import com.github.madhurimamalla.connoisseur.server.model.JobType;
import com.github.madhurimamalla.connoisseur.server.service.JobService;

public abstract class AbstractJob implements RunnableJob {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractJob.class);

	protected volatile AtomicBoolean isCancelled = new AtomicBoolean(false);

	protected JobService jobService;

	protected JobHistory job;

	AbstractJob(JobService jobService, JobHistory job) {
		this.jobService = jobService;
		this.job = job;
	}

	@Override
	public void run() {
		jobService.updateJobStatus(job.getJobId(), JobState.RUNNING);
		getLogger().write("Updating the job status to RUNNING");
		JobResult result = JobResult.SUCCESS;
		try {
			result = execute(job.getJobParams());
		} catch (Exception e) {
			Writer writer = new StringWriter();
			e.printStackTrace(new PrintWriter(writer));
			String s = writer.toString();
			getLogger().write(s);
			getLogger().write("Updating the job status to FAILED");
			jobService.updateJobStatus(job.getJobId(), JobState.FAILED);
			jobService.deleteTypeFromQ(job);
			return;
		}
		if(result == JobResult.SUCCESS) {
			jobService.updateJobStatus(job.getJobId(), JobState.FINISHED);
			getLogger().write("Updating the job status to FINISHED");
		} else {
			jobService.updateJobStatus(job.getJobId(), JobState.CANCELLED);
			getLogger().write("Updating the job status to CANCELLED");
		}
		jobService.deleteTypeFromQ(job);
	}

	protected abstract JobResult execute(List<JobParams> params) throws Exception;

	@Override
	public void cancel() {
		LOG.info("AbstractJob is setting the cancel flag to true");
		isCancelled.set(true);
		LOG.info("Cancellation request received for job [" + job.getJobId() + "].");
		getLogger().write("The job is cancelled!");
	}

	@Override
	public void updateJobType(JobType JOB_TYPE) {
		// TODO Auto-generated method stub
	}

	@Override
	public void updateJobStats(JobHistory job) {
		jobService.updateJob(job);
	}

	protected JobLog getLogger() {
		return new JobLog(job.getJobType().name(), job.getJobId());
	}

}
