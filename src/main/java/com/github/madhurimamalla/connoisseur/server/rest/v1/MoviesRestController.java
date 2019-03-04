package com.github.madhurimamalla.connoisseur.server.rest.v1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.madhurimamalla.connoisseur.server.jobs.JobLog;
import com.github.madhurimamalla.connoisseur.server.jobs.JobScheduler;
import com.github.madhurimamalla.connoisseur.server.jobs.JobState;
import com.github.madhurimamalla.connoisseur.server.model.JobHistory;
import com.github.madhurimamalla.connoisseur.server.model.JobParams;
import com.github.madhurimamalla.connoisseur.server.model.JobType;
import com.github.madhurimamalla.connoisseur.server.model.Movie;
import com.github.madhurimamalla.connoisseur.server.model.SimilarityRelation;
import com.github.madhurimamalla.connoisseur.server.moviedb.client.rest.TMDBClient;
import com.github.madhurimamalla.connoisseur.server.moviedb.client.rest.model.MovieRM;
import com.github.madhurimamalla.connoisseur.server.moviedb.client.rest.model.mapper.MovieMapper;
import com.github.madhurimamalla.connoisseur.server.persistence.MovieRepository;
import com.github.madhurimamalla.connoisseur.server.persistence.SimilarityRelationRepository;
import com.github.madhurimamalla.connoisseur.server.service.JobService;
import com.github.madhurimamalla.connoisseur.server.service.MovieService;

@RestController
public class MoviesRestController {

	private static final Logger LOG = LoggerFactory.getLogger(MoviesRestController.class);

	@Autowired
	MovieRepository repositoryService;

	@Autowired
	MovieService movieService;

	@Autowired
	JobService jobService;

	@Autowired
	SimilarityRelationRepository srr;

	@RequestMapping(value = "/movies/sync/all", method = RequestMethod.GET)
	public long sync() {
		JobHistory jobHistory = null;
		try {
			LOG.info("The sync job has started...");
			JobHistory job = new JobHistory();
			job.setJobType(JobType.MOVIES_DOWNLOAD);
			job.setJobStatus(JobState.QUEUED);
			jobHistory = JobScheduler.getInstance(jobService, movieService).schedule(job);
		} catch (Exception e) {
			return 0l;
		}
		return jobHistory.getJobId();
	}

	@RequestMapping(value = "/movies/sync", method = RequestMethod.GET)
	public long sync(@RequestParam(value = "start") long start, @RequestParam(value = "end") long end) {
		JobHistory jobHistory = null;
		try {
			LOG.info("The sync job has started.....");
			JobHistory job = new JobHistory();
			job.setJobType(JobType.MOVIES_DOWNLOAD);
			job.setJobStatus(JobState.QUEUED);
			job.getJobParams().add(new JobParams(job, "START", Long.toString(start)));
			job.getJobParams().add(new JobParams(job, "END", Long.toString(end)));
			jobHistory = JobScheduler.getInstance(jobService, movieService).schedule(job);
		} catch (Exception e) {
			LOG.error("The sync job has failed with exception: " + e.getMessage());
			e.printStackTrace();
		}
		return jobHistory.getJobId();
	}

	@RequestMapping(value = "/movies/sync/updatedb", method = RequestMethod.GET)
	public long updateDBToLatestTMDBId(@RequestParam(value = "end") long end) {
		JobHistory jobHistory = null;
		try {
			LOG.info("The sync job has started.....");
			JobHistory job = new JobHistory();
			job.setJobType(JobType.MOVIES_DOWNLOAD);
			job.setJobStatus(JobState.QUEUED);
			job.getJobParams().add(new JobParams(job, "START", Long.toString(movieService.findMaxId())));
			job.getJobParams().add(new JobParams(job, "END", Long.toString(end)));
			jobHistory = JobScheduler.getInstance(jobService, movieService).schedule(job);
		} catch (Exception e) {
			LOG.error("The sync job has failed with exception: " + e.getMessage());
			e.printStackTrace();
		}
		return jobHistory.getJobId();
	}

	@RequestMapping(value = "/movies/logs", method = RequestMethod.GET)
	public String logs(@RequestParam(value = "jobId") long jobId) {
		Optional<JobHistory> jobOp = jobService.findJobById(jobId);
		if (jobOp != null) {
			return JobLog.readLog(jobOp.get().getJobType().name(), jobId);
		}
		return "Job id might be wrong. Please try with a different id.";
	}

	@RequestMapping(value = "/movies", method = RequestMethod.POST)
	public Long add(@RequestParam(value = "title") String title, @RequestParam(value = "movieDbId") long movieDbId) {
		Movie m = new Movie(movieDbId, title, "", "");
		Movie saved = repositoryService.save(m);
		// return saved.getId();
		return 1l;
	}

	@RequestMapping(value = "/movies", method = RequestMethod.GET)
	public List<MovieRM> getAllMovies() {
		try {
			Iterable<Movie> movies = repositoryService.findAll();
			List<MovieRM> moviesList = new ArrayList<>();
			MovieMapper mapper = new MovieMapper();
			for (Movie m : movies) {
				moviesList.add(mapper.toMovieRMModel(m));
			}
			return moviesList;
		} catch (Exception e) {
			LOG.error("Fetching all movies failed with an exception : " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/movies/similar", method = RequestMethod.GET)
	public List<MovieRM> getSimilarMovies(@RequestParam(value = "id") long movieId) {
		try {
			Optional<Movie> m = repositoryService.findById(movieId);
			List<SimilarityRelation> similarMovies = new ArrayList<>();
			List<MovieRM> movies = new ArrayList<>();			
			if (m.get() != null) {
				similarMovies = srr.findBySource(m.get());
				Iterator<SimilarityRelation> itr = similarMovies.iterator();
				MovieMapper mapper = new MovieMapper();
				while (itr.hasNext()) {
					movies.add(mapper.toMovieRMModel(itr.next().getTarget()));
				}
			}
			return movies;
		} catch (Exception e) {
			LOG.error("Similar movies search failed with exception: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
		
	}
	
	@RequestMapping(value = "/movies/random", method = RequestMethod.GET)
	public List<MovieRM> getRandom(@RequestParam(value = "num") long number){
		List<MovieRM> movies = new ArrayList<>();
		long num = number;
		try{
			Iterator<Movie> itr = this.movieService.getRandom(num).iterator();
			MovieMapper mapper = new MovieMapper();
			while (itr.hasNext()) {
				movies.add(mapper.toMovieRMModel(itr.next()));
			}
			return movies;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/jobs/cancel")
	public String cancelJob(@RequestParam(value = "jobId") long jobId) {
		try {
			JobScheduler.getInstance(jobService, movieService).cancel(jobService.findJobById(jobId).get());
			return "Initiated cancel of the jobId";
		} catch (Exception e) {
			return "Cancel request on " + jobId + " failed";
		}
	}

	@RequestMapping(value = "/jobs/queue/clean")
	public String removeJobsFromJobQueue() {
		try {
			JobScheduler.getInstance(jobService, movieService).removeJobsFromQueue();
			return "Removed jobs from queue";
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	@RequestMapping(value = "/jobs/similarityInference", method = RequestMethod.GET)
	public long computeSimilarityInference() {
		JobHistory jobHistory = null;
		try {
			LOG.info("The sync job has started...");
			JobHistory job = new JobHistory();
			job.setJobType(JobType.SIMILARITY_INFERENCE);
			job.setJobStatus(JobState.QUEUED);
			jobHistory = JobScheduler.getInstance(jobService, movieService).schedule(job);
		} catch (Exception e) {
			return 0l;
		}
		return jobHistory.getJobId();
	}
}