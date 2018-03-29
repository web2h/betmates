package com.web2h.betmates.restapp.model.exception;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.web2h.betmates.restapp.model.entity.reference.City;
import com.web2h.betmates.restapp.model.entity.reference.Country;
import com.web2h.betmates.restapp.model.entity.reference.Venue;
import com.web2h.betmates.restapp.model.http.ErrorResponse;
import com.web2h.betmates.restapp.model.validation.ErrorCode;
import com.web2h.betmates.restapp.model.validation.Field;

/**
 * Not found exception. When an element could not be found.
 * 
 * @author web2h
 */
public class NotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public static final Map<String, String> messages;
	static {
		Map<String, String> aMap = new HashMap<>();
		aMap.put(Field.ID.name() + City.class.getName(), "No city could be found with the given ID");
		aMap.put(Field.ID.name() + Country.class.getName(), "No country could be found with the given ID");
		aMap.put(Field.ID.name() + Venue.class.getName(), "No venue could be found with the given ID");
		messages = Collections.unmodifiableMap(aMap);
	}

	/** FIELD - Field that could not be found. */
	private Field field;

	public NotFoundException(String message) {
		super(message);
	}

	public NotFoundException(Field field, String className) {
		super(messages.get(field.name() + className));
		this.field = field;
	}

	public ResponseEntity<Object> getResponseEntity() {
		ErrorResponse response = new ErrorResponse(getMessage(), field, ErrorCode.NOT_FOUND);
		return new ResponseEntity<Object>(response, HttpStatus.NOT_FOUND);
	}
}