package com.web2h.betmates.restapp.model.validation;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Field labels enum. For error tracking.
 * 
 * @author web2h
 */
public enum Field {

	ALIAS("alias"),
	CITY("city"),
	COUNTRY("country"),
	ID("id"),
	EMAIL("email"),
	NAME_EN("nameEn"),
	NAME_FR("nameFr"),
	PASSWORD("password"),
	ROLE("role"),
	SHORT_NAME_EN("shortNameEn"),
	SHORT_NAME_FR("shortNameFr"),
	SPORT("sport"),
	START_DATE("startDate"),
	TEAM("Team"),
	TYPE("type"),
	VENUE("Venue");

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