package com.github.madhurimamalla.connoisseur.server.jobs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
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
	protected JobResult execute(List<JobParams> params) throws Exception {
		JobResult result = JobResult.SUCCESS;
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

			List<Future<Boolean>> downloaderFutures = new ArrayList<>();
			downloaderFutures.add(threadPool.submit(new MovieDownloader(getLogger(), "C1", broker, movieService)));

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

			/*
			 * Monitoring loop.
			 */
			while (true) {
				if (isCancelled.get()) {
					producerStatus.cancel(true);
					result = JobResult.CANCELLED;
					break;
				}
				if (producerStatus.isDone()) {
					break;
				}

				for (Future<Boolean> f : downloaderFutures) {
					if (f.isDone()) {
						try {
							f.get();
						} catch (ExecutionException e) {
							threadPool.shutdownNow();
							throw e;
						}
					}
				}
				Thread.sleep(5000);
			}

			/*
			 * Producer completed or cancelled. Signal the downloaders and shut
			 * down the thread pool gracefully.
			 */
			getLogger().write("Sending shutdown signal to downloader threads.");
			broker.shutdown(downloaderFutures.size());

			threadPool.shutdown();
			try {
				if (!threadPool.awaitTermination(600, TimeUnit.SECONDS)) {
					getLogger().write("Downloaders did not finish. Forcing thread pool shutdown.");
					threadPool.shutdownNow();
				}
			} catch (InterruptedException ex) {
				threadPool.shutdownNow();
			}
		}
		return result;
	}

}
