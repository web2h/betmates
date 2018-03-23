package com.web2h.betmates.restapp.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.web2h.betmates.restapp.model.http.ErrorResponse;
import com.web2h.betmates.restapp.model.validation.ErrorCode;
import com.web2h.betmates.restapp.model.validation.Field;

/**
 * Already exists exception. When an element is being added for a second time.
 * 
 * @author web2h
 */
public class AlreadyExistsException extends Exception {

	private static final long serialVersionUID = 1L;

	/** FIELD - Field that already exists. */
	private Field field;

	public AlreadyExistsException(String message, Field field) {
		super(message);
		this.field = field;
	}

	public ResponseEntity<Object> getResponseEntity() {
		ErrorResponse response = new ErrorResponse(getMessage(), field, ErrorCode.ALREADY_EXISTS);
		return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
	}
}