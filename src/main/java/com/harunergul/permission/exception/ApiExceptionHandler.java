package com.harunergul.permission.exception;

import java.util.Date;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

	private final MessageSource messageSource;

	public ApiExceptionHandler(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@ExceptionHandler(value = { ApiRequestException.class })
	public ResponseEntity<Object> handleApiRequest(ApiRequestException e, Locale locale) {
		String message = messageSource.getMessage(e.getLocalizedMessage(), e.getArgs(), locale);
		if(message==null) {
			message = e.getLocalizedMessage();
		}

		ApiException apiException = new ApiException(message, e, HttpStatus.BAD_REQUEST, new Date());

		return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);

	}
}
