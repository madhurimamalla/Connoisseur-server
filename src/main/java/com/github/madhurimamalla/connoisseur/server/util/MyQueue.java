package com.github.madhurimamalla.connoisseur.server.util;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyQueue implements CBQueue {

	private Message[] array;
	private int head = 0;
	private int tail = 0;
	private boolean isEmpty = true;
	private boolean isFull = false;
	private int capacity;
	private Lock lock = new ReentrantLock();
	private Condition isFullCondition;
	private Condition isEmptyCondition;

	public MyQueue(int capacity) {
		this.array = new Message[capacity];
		this.capacity = capacity;
		isFullCondition = lock.newCondition();
		isEmptyCondition = lock.newCondition();
	}

	@Override
	public void put(Message message) throws Exception {
		try {
			lock.lock();
			while (isFull) {
				isFullCondition.await();
			}
			array[tail++] = message;
			tail = (tail % capacity);
			isEmpty = false;
			if (tail == head) {
				isFull = true;
				System.out.println("[" + Thread.currentThread().getName() + "] Queue Size : FULL");
			} else {
				int queueSize = (head < tail) ? (tail - head) : (capacity - (head - tail));
				System.out.println("[" + Thread.currentThread().getName() + "] Queue Size : " + queueSize);

			}
			isEmptyCondition.signal();
		} finally {
			lock.unlock();
		}

	}

	@Override
	public Message take() throws Exception {
		try {
			lock.lock();
			while (isEmpty) {
				isEmptyCondition.await();
			}
			Message m = array[head++];
			head = (head % capacity);
			isFull = false;
			if (tail == head) {
				isEmpty = true;
				System.out.println("[" + Thread.currentThread().getName() + "] Queue Size : EMPTY");
			} else {
				int queueSize = (head < tail) ? (tail - head) : (capacity - (head - tail));
				System.out.println("[" + Thread.currentThread().getName() + "] Queue Size : " + queueSize);
			}
			isFullCondition.signal();
			return m;
		} finally {
			lock.unlock();
		}

	}

	@Override
	public synchronized void release() throws Exception {
		this.notifyAll();
	}

}
