package com.github.madhurimamalla.connoisseur.server.jobs;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.madhurimamalla.connoisseur.server.moviedb.client.rest.TMDBClient;
import com.github.madhurimamalla.connoisseur.server.service.MovieService;
import com.github.madhurimamalla.connoisseur.server.util.Broker;
import com.github.madhurimamalla.connoisseur.server.util.Message;

@Service
public class MovieSyncJob {

	private static final Logger LOG = LoggerFactory.getLogger(MovieSyncJob.class);

	@Autowired
	MovieService movieService;

	/*
	 * Runs the job with startIndex = 1; endIndex = latestMovieId on TMDB
	 */
	public void start() throws InterruptedException {
		TMDBClient client = new TMDBClient();
		long endId = 1;
		try {
			endId = (long) client.getLatestMovieId();
		} catch (Exception e) {
			e.printStackTrace();
		}
		LOG.info("Starting the job with starting id : 1 and ending with " + endId);
		runWithIndexes(1, endId);
	}

	/*
	 * Runs the job with startIndex = startId ; endIndex = endId
	 */
	public void start(long startId, long endId) throws InterruptedException {
		LOG.info("Starting the job with starting id: " + startId + " and fetching till endId: " + endId);
		runWithIndexes(startId, endId);
	}

	private void runWithIndexes(long startIndex, long endIndex) throws InterruptedException {
		Broker<Long> broker = new Broker<>();

		if (startIndex < endIndex) {
			try {
				ExecutorService threadPool = Executors.newFixedThreadPool(6);

				threadPool.execute(new MovieDownloader("C1", broker, movieService));
				threadPool.execute(new MovieDownloader("C2", broker, movieService));
				threadPool.execute(new MovieDownloader("C3", broker, movieService));
				threadPool.execute(new MovieDownloader("C4", broker, movieService));
				threadPool.execute(new MovieDownloader("C5", broker, movieService));

				/**
				 * Initiate a producer thread to populate the shared queue with
				 * movie ids.
				 */
				Future<?> producerStatus = threadPool.submit(new Runnable() {

					@Override
					public void run() {
						for (long i = startIndex; i <= endIndex; i++) {
							try {
								broker.put(new Message<>(i));
							} catch (InterruptedException e) {
								e.printStackTrace();
								LOG.error("Producer thread interrupted.");
							}
						}
					}

				});

				producerStatus.get();

				broker.shutdown(5);

				threadPool.shutdown();
				try {
					if (!threadPool.awaitTermination(600, TimeUnit.SECONDS)) {
						threadPool.shutdownNow();
					}
				} catch (InterruptedException ex) {
					threadPool.shutdownNow();
					Thread.currentThread().interrupt();
				}

			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		} else {
			LOG.error("Wrong limit indexes found at the job run...");
		}
	}
}
