package com.web2h.betmates.restapp.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.web2h.betmates.restapp.model.http.ErrorResponse;

/**
 * Internal error exception. When an error occurred on the server during a
 * process a could not be identified.
 * 
 * @author web2h
 */
public class InternalErrorException extends Exception {

	public static final String PLATFORM_CREATION_IEE = "An error occured while creating the platform";
	public static final String PLATFORM_EDITION_IEE = "An error occured while editing the platform";

	private static final long serialVersionUID = 1L;

	public InternalErrorException(String message) {
		super(message);
	}

	public ResponseEntity<Object> getResponseEntity() {
		return new ResponseEntity<Object>(new ErrorResponse(getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}