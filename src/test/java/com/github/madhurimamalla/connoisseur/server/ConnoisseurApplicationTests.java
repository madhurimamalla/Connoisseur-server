package com.github.madhurimamalla.connoisseur.server;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.madhurimamalla.connoisseur.server.jobs.JobState;
//import com.github.madhurimamalla.connoisseur.server.jobs.MovieSyncJob;
import com.github.madhurimamalla.connoisseur.server.jobs.SimilarityInferenceJob;
import com.github.madhurimamalla.connoisseur.server.model.JobHistory;
import com.github.madhurimamalla.connoisseur.server.model.JobParams;
import com.github.madhurimamalla.connoisseur.server.model.JobStats;
import com.github.madhurimamalla.connoisseur.server.model.JobType;
import com.github.madhurimamalla.connoisseur.server.persistence.MovieRepository;
import com.github.madhurimamalla.connoisseur.server.service.JobService;
import com.github.madhurimamalla.connoisseur.server.service.JobTypeExistsException;
import com.github.madhurimamalla.connoisseur.server.service.MovieService;

@RunWith(SpringRunner.class)
@Configuration
@TestPropertySource("classpath:application.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ConnoisseurApplicationTests {

	private static final Logger LOG = LoggerFactory.getLogger(ConnoisseurApplicationTests.class);

//	@Autowired
//	MovieSyncJob movieSyncJob;

	@Autowired
	MovieRepository movieRepository;

	@Autowired
	MovieService movieService;

	@Autowired
	JobService jobService;

	@Autowired
	SimilarityInferenceJob sij;

//	@Test()
//	public void testDownloadJob() throws Exception {
//		movieSyncJob.start();
//	}

	@Test
	public void findNextJob() throws Exception {
		JobHistory job = jobService.findNextJob();
		System.out.println(job.getJobId());
	}

	@Test
	public void createJobEntry() throws Exception {
		JobHistory job = new JobHistory();
		job.setJobName("DownloadMovies4");
		job.setJobType(JobType.MOVIES_DOWNLOAD);
		job.setJobStatus(JobState.QUEUED);
		job.getJobParams().add(new JobParams(job, "START", "100"));
		job.getJobParams().add(new JobParams(job, "END", "1000"));
		// job.getJobStats().add(new JobStats(job, "Movies_Downloaded", ""));
		job = jobService.addJob(job);
		if (job != null) {
			System.out.println(job.getJobId());
		}
	}

	@Test
	public void findMinJobId() throws Exception {
		long id = jobService.findMinId();
		System.out.println(id);
	}

	@Test
	public void deleteAllJobs() throws Exception {
		if (jobService.deleteAll()) {
			System.out.println("All jobs are deleted!");
		}
	}

	@Test
	public void createAndUpdateJob() throws Exception {
		JobHistory job = new JobHistory();
		job.setJobName("MovieDownload_7");
		job.setJobType(JobType.MOVIES_DOWNLOAD);
		job.setJobStatus(JobState.QUEUED);
		JobStats stats = new JobStats(job, "Movie downloaded", "4");
		try {
			job = jobService.addJob(job);
		} catch (JobTypeExistsException e) {
			e.printStackTrace();
		}
		System.out.println(job.getJobId());
	}

	@Test
	public void createAndRemoveJob() throws Exception {
		JobHistory job = new JobHistory();
		job.setJobName("Publish_Firebase_1");
		job.setJobStatus(JobState.QUEUED);
		job.setJobType(JobType.FIREBASE_PUBLISH);
		try {
			job = jobService.addJob(job);
		} catch (JobTypeExistsException e) {
			e.printStackTrace();
		}
		jobService.removeJob(job);
		System.out.println(jobService.findJobById(job.getJobId()));
	}

	@Test
	public void testSimilarityInferenceJob() {
		sij.run();
	}

}