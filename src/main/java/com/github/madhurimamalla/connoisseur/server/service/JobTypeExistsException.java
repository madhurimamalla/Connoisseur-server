package com.github.madhurimamalla.connoisseur.server.service;

public class JobTypeExistsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JobTypeExistsException(Throwable cause) {
		super(cause);
	}

	public JobTypeExistsException(String message) {
		super(message);
	}

	public JobTypeExistsException(String message, Throwable cause) {
		super(message, cause);
	}

}
