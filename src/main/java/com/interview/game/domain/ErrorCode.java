package com.interview.game.domain;

import lombok.Getter;

@Getter
public enum ErrorCode {
	PLAYER_NOT_FOUND("The player does not exist in the system"),
	EMAIL_ALREADY_EXISTS("The email address is already registered"),
	INVALID_USERNAME("The username is invalid or does not meet requirements"),
	ACTION_NOT_ALLOWED("The requested action is not allowed at this time");

	private final String message;

	ErrorCode(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ErrorCode{" +
				", message='" + message + '\'' +
				'}';
	}
}
