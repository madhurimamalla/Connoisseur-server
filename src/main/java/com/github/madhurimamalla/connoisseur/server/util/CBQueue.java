package com.github.madhurimamalla.connoisseur.server.util;

/**
 * Represents a concurrent bounded blocking queue which provides methods to add
 * and take messages in a thread safe manner.
 * 
 * @author reema
 *
 */
public interface CBQueue {

	/**
	 * Places a message in the queue; waits if the queue is not empty.
	 * 
	 * @param message
	 */
	public void put(Message message) throws Exception;

	/**
	 * Takes a message from the queue; waits if the queue is empty.
	 * 
	 * @return message
	 */
	public Message take() throws Exception;

	public void release() throws Exception;

}
