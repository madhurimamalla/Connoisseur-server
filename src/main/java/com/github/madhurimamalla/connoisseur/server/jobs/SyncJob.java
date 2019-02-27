package com.github.madhurimamalla.connoisseur.server.jobs;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.madhurimamalla.connoisseur.server.model.JobHistory;
import com.github.madhurimamalla.connoisseur.server.model.JobParams;
import com.github.madhurimamalla.connoisseur.server.service.JobService;
import com.github.madhurimamalla.connoisseur.server.service.MovieService;
import com.github.madhurimamalla.connoisseur.server.util.Broker;
import com.github.madhurimamalla.connoisseur.server.util.Message;

public final class SyncJob extends AbstractJob {

	private static final Logger LOG = LoggerFactory.getLogger(SyncJob.class);

	private MovieService movieService;

	private long startIndex = 0;
	private long endIndex = 0;

	public SyncJob(JobService jobService, JobHistory job, MovieService service) {
		super(jobService, job);
		this.movieService = service;
	}

	@Override
	protected void execute(List<JobParams> params) throws Exception {
		getLogger().write("Execute on SyncJob is called...");
		Iterator<JobParams> itr = params.iterator();
		while (itr.hasNext()) {
			JobParams param = itr.next();
			if (param.getKey().equals("START")) {
				startIndex = Long.parseLong(param.getValue());
			} else if (param.getKey().equals("END")) {
				endIndex = Long.parseLong(param.getValue());
			}
		}

		getLogger().write("Start index: " + startIndex + " End Index: " + endIndex);
		LOG.info("Start index: " + startIndex + " End Index: " + endIndex);

		Broker<Long> broker = new Broker<>();

		if (startIndex < endIndex) {
			ExecutorService threadPool = Executors.newFixedThreadPool(6);

			threadPool.execute(new MovieDownloader(getLogger(), "C1", broker, movieService));

			/*
			 * TODO This will need a better solution as we are hitting a 429
			 * error with so many consumers
			 */
			// threadPool.execute(new MovieDownloader(jobLog, "C3",
			// broker, movieService));
			// threadPool.execute(new MovieDownloader(getLogger(), "C2", broker,
			// movieService));
			// threadPool.execute(new MovieDownloader(jobLog, "C4",
			// broker, movieService));
			// threadPool.execute(new MovieDownloader(jobLog, "C5",
			// broker, movieService));

			/**
			 * Initiate a producer thread to populate the shared queue with
			 * movie ids.
			 */
			Future<?> producerStatus = threadPool.submit(new Runnable() {

				@Override
				public void run() {
					for (long i = startIndex; i <= endIndex; i++) {
						try {
							/**
							 * FIXME: Add rate limiter instead.
							 */
							Thread.sleep(5000);
							broker.put(new Message<>(i));
						} catch (InterruptedException e) {
							e.printStackTrace();
							getLogger().write("Producer thread interrupted.");
						}
					}
				}

			});

			producerStatus.get();

			getLogger().write("Producer completed.");

			getLogger().write("Shutting down downloader threads.");
			broker.shutdown(5);

			threadPool.shutdown();
			try {
				if (!threadPool.awaitTermination(600, TimeUnit.SECONDS)) {
					threadPool.shutdownNow();
				}
			} catch (InterruptedException ex) {
				threadPool.shutdownNow();
			}
		}
	}

}
