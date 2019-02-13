package com.github.madhurimamalla.connoisseur.server.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Broker<T> {

	private BlockingQueue<Message<T>> queue = new ArrayBlockingQueue<>(10);

	public void put(Message<T> message) throws InterruptedException {
		queue.put(message);
	}

	public Message<T> take() throws InterruptedException {
		return queue.take();
	}

	public void shutdown(int numConsumers) throws InterruptedException {
		for (int i = 0; i < numConsumers; i++) {
			queue.put(Message.createPoisonPill());
		}
	}
}