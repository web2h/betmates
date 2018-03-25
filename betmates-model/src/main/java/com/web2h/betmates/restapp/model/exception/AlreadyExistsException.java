package com.web2h.betmates.restapp.model.exception;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
	
	private static final Map<Field, String> messages;
	static {
		Map<Field, String> aMap = new HashMap<>();
        aMap.put(Field.ALIAS, "A user already exists with the given alias");
        aMap.put(Field.EMAIL, "A user already exists with the given email");
        messages = Collections.unmodifiableMap(aMap);
    }

	private static final long serialVersionUID = 1L;

	/** FIELD - Field that already exists. */
	private Field field;

	public AlreadyExistsException(Field field) {
		super(messages.get(field));
		this.field = field;
	}

	public ResponseEntity<Object> getResponseEntity() {
		ErrorResponse response = new ErrorResponse(getMessage(), field, ErrorCode.ALREADY_EXISTS);
		return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
	}
}