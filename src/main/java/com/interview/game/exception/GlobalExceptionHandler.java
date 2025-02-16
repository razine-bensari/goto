package com.interview.game.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


	@ExceptionHandler(InvalidDeckOperation.class)
	public ResponseEntity<String> handleUserNotFound(InvalidDeckOperation ex) {
		return ResponseEntity
				.status(HttpStatus.FORBIDDEN)
				.body(ex.getMessage());
	}
}
