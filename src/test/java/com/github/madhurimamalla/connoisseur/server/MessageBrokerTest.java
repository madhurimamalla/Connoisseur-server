package com.github.madhurimamalla.connoisseur.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.madhurimamalla.connoisseur.server.util.Broker;
import com.github.madhurimamalla.connoisseur.server.util.Message;

@RunWith(SpringRunner.class)
public class MessageBrokerTest {

	public class Producer implements Runnable {

		private Broker<Integer> broker;
		private int start;
		private int end;

		public Producer(Broker<Integer> broker, int start, int end) {
			super();
			this.broker = broker;
			this.start = start;
			this.end = end;
		}

		@Override
		public void run() {
			for (int i = start; i <= end; i++) {
				try {
					broker.put(new Message<Integer>(i));
					// System.out.println(Thread.currentThread().getName() + " :
					// Added " + i);
					Thread.sleep(10);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	public class Consumer implements Runnable {

		private Broker<Integer> broker;
		private List<Integer> result = new ArrayList<>();

		public Consumer(Broker<Integer> broker) {
			super();
			this.broker = broker;
		}

		@Override
		public void run() {
			while (true) {
				try {
					Message<Integer> m = broker.take();
					if (m.isPoisonPill()) {
						break;
					}
					// System.out.println(Thread.currentThread().getName() + " :
					// Took " + m.getObject().toString());
					result.add(m.getPayload());
					Thread.sleep(20);
				} catch (Exception e) {
					e.printStackTrace();
					Thread.currentThread().interrupt();
				}
			}
		}

		public List<Integer> getResult() {
			return result;
		}

	}

	@Test
	public void testMessageBroker() throws Exception {

		Broker<Integer> broker = new Broker<>();

		Producer p1 = new Producer(broker, 1, 50);
		Producer p2 = new Producer(broker, 51, 100);

		Consumer c1 = new Consumer(broker);
		Consumer c2 = new Consumer(broker);
		Consumer c3 = new Consumer(broker);

		Thread pt1 = new Thread(p1, "P1");
		Thread pt2 = new Thread(p2, "P2");
		Thread ct1 = new Thread(c1, "C1");
		Thread ct2 = new Thread(c2, "C2");
		Thread ct3 = new Thread(c3, "C3");

		ct1.start();
		ct2.start();
		ct3.start();

		pt1.start();
		pt2.start();

		pt1.join();
		pt2.join();

		broker.shutdown(3);

		ct1.join();
		ct2.join();
		ct3.join();

		List<Integer> r1 = c1.getResult();
		List<Integer> r2 = c2.getResult();
		List<Integer> r3 = c3.getResult();

		Assert.assertFalse(r1.isEmpty());
		Assert.assertFalse(r2.isEmpty());
		Assert.assertFalse(r3.isEmpty());

		List<Integer> results = new ArrayList<>();
		results.addAll(r1);
		results.addAll(r2);
		results.addAll(r3);

		Collections.sort(results);

		Assert.assertTrue(results.size() == 100);

		for (int i = 1; i <= 100; i++) {
			Assert.assertTrue(results.get(i - 1) == i);
		}
	}

}
