package com.web2h.betmates.restapp.model.validation;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Field labels enum. For error tracking.
 * 
 * @author web2h
 */
public enum Field {

	ALIAS("alias"),
	ID("id"), 
	EMAIL("email"),
	PASSWORD("password");

	private Field(String lowerCaseValue) {
		this.lowerCaseValue = lowerCaseValue;
	}

	private String lowerCaseValue;

	public static Field getByLowerCaseValue(String lowerCaseValue) {
		for (Field field : Field.values()) {
			if (field.getLowerCaseValue().equals(lowerCaseValue)) {
				return field;
			}
		}
		return null;
	}

	@JsonValue
	public String getLowerCaseValue() {
		return lowerCaseValue;
	}

	public void setLowerCaseValue(String lowerCaseValue) {
		this.lowerCaseValue = lowerCaseValue;
	}

	@Override
	public String toString() {
		return lowerCaseValue;
	}
}