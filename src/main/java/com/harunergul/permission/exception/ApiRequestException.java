package com.harunergul.permission.exception;

public class ApiRequestException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private Object[] args;

	public ApiRequestException(String message) {
		super(message);
	}

	public ApiRequestException(String message, Object... args) {
		super(message);
		this.args = args;
	}

	public ApiRequestException(String message, Throwable cause) {
		super(message, cause);
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

}
