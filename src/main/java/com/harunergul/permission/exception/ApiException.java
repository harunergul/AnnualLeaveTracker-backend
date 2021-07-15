package com.harunergul.permission.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;

public class ApiException {

	private final String message;
	private final Throwable throwable;
	private final HttpStatus httpStatus;
	private final Date timestamp;

	public ApiException(String message, Throwable throwable, HttpStatus status, Date timestamp) {
		this.message = message;
		this.timestamp = timestamp;
		this.throwable = throwable;
		this.httpStatus = status;
	}

	public String getMessage() {
		return message;
	}

	public Throwable getThrowable() {
		return throwable;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public Date getTimestamp() {
		return timestamp;
	}
	
	

}
