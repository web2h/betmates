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
 * Already exists exception. When an element is being added for a second time.
 * 
 * @author web2h
 */
public class AlreadyExistsException extends Exception {

	public static final Map<String, String> messages;
	static {
		Map<String, String> aMap = new HashMap<>();
		aMap.put(Field.ALIAS.name(), "A user already exists with the given alias");
		aMap.put(Field.EMAIL.name(), "A user already exists with the given email");
		aMap.put(Field.NAME_EN.name() + City.class.getName(), "A city already exists in this country with the given English name");
		aMap.put(Field.NAME_FR.name() + City.class.getName(), "A city already exists in this country with the given French name");
		aMap.put(Field.NAME_EN.name() + Country.class.getName(), "A country already exists with the given English name");
		aMap.put(Field.NAME_FR.name() + Country.class.getName(), "A country already exists with the given French name");
		aMap.put(Field.NAME_EN.name() + Venue.class.getName(), "A venue already exists in this city with the given English name");
		aMap.put(Field.NAME_FR.name() + Venue.class.getName(), "A venue already exists in this city with the given French name");
		messages = Collections.unmodifiableMap(aMap);
	}

	private static final long serialVersionUID = 1L;

	/** FIELD - Field that already exists. */
	private Field field;

	public AlreadyExistsException(Field field) {
		super(messages.get(field.name()));
		this.field = field;
	}

	public AlreadyExistsException(Field field, String className) {
		super(messages.get(field.name() + className));
		this.field = field;
	}

	public ResponseEntity<Object> getResponseEntity() {
		ErrorResponse response = new ErrorResponse(getMessage(), field, ErrorCode.ALREADY_EXISTS);
		return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
	}
}