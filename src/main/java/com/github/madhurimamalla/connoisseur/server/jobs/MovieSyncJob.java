package com.github.madhurimamalla.connoisseur.server.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.madhurimamalla.connoisseur.server.service.MovieService;
import com.github.madhurimamalla.connoisseur.server.util.Broker;
import com.github.madhurimamalla.connoisseur.server.util.Message;

@Service
public class MovieSyncJob {

	private static final Logger LOG = LoggerFactory.getLogger(MovieSyncJob.class);

	@Autowired
	MovieService movieService;

	public void start() throws InterruptedException {

		Broker<Long> broker = new Broker<>();

		Thread pt = new Thread(new Runnable() {

			@Override
			public void run() {
				for (long i = 1; i <= 4; i++) {
					try {
						broker.put(new Message<>(i));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		}, "PT1");

		MovieDownloader d1 = new MovieDownloader(broker, movieService);
		MovieDownloader d2 = new MovieDownloader(broker, movieService);
		MovieDownloader d3 = new MovieDownloader(broker, movieService);
		MovieDownloader d4 = new MovieDownloader(broker, movieService);
		MovieDownloader d5 = new MovieDownloader(broker, movieService);

		Thread t1 = new Thread(d1, "T1");
		Thread t2 = new Thread(d2, "T2");
		Thread t3 = new Thread(d3, "T3");
		Thread t4 = new Thread(d4, "T4");
		Thread t5 = new Thread(d5, "T5");

		pt.start();

		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();

		pt.join();
		broker.shutdown(5);

		t1.join();
		t2.join();
		t3.join();
		t4.join();
		t5.join();

	}
}
