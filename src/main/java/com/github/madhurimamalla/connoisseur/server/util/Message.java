package com.github.madhurimamalla.connoisseur.server.util;

import java.util.Objects;

public final class Message<T> {

	private T payload;

	private boolean isPoisonPill = false;

	public Message(T object) {
		Objects.requireNonNull(object);
		this.payload = object;
	}

	private Message() {
		this.isPoisonPill = true;
	}

	public static <T> Message<T> createPoisonPill() {
		return new Message<T>();
	}

	public T getPayload() {
		return payload;
	}

	public boolean isPoisonPill() {
		return isPoisonPill;
	}

}
