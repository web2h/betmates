package com.web2h.betmates.restapp.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.web2h.betmates.restapp.model.http.ErrorResponse;

/**
 * Not found exception. When an element could not be found.
 * 
 * @author web2h
 */
public class NotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public NotFoundException(String message) {
		super(message);
	}

	public ResponseEntity<Object> getResponseEntity() {
		return new ResponseEntity<Object>(new ErrorResponse(getMessage()), HttpStatus.NOT_FOUND);
	}
}
