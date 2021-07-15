package com.harunergul.permission.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(value = { ApiRequestException.class })
	public ResponseEntity<Object> handleApiRequest(ApiRequestException e) {

		ApiException apiException = new ApiException(e.getMessage(), e, HttpStatus.BAD_REQUEST, new Date());

		return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);

	}
}
