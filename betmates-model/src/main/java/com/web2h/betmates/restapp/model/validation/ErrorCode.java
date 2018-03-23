package com.web2h.betmates.restapp.model.validation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Error code type enum.
 * 
 * @author web2h
 */
public enum ErrorCode {

	ALREADY_EXISTS("AlreadyExists"),
	EMPTY("Empty", "NotNull"),
	NOT_EMPTY("NotEmpty", "Null"),
	NOT_FOUND("NotFound"),
	INVALID_EMAIL("InvalidEmail", "Email"),
	TOO_LONG("TooLong"),
	TOO_SHORT("TooShort");

	private Set<String> codes;

	private String jsonValue;

	private ErrorCode(String jsonValue, String... codes) {
		this.jsonValue = jsonValue;
		this.codes = new HashSet<>(Arrays.asList(codes));
	}

	public static ErrorCode getBySpringFieldError(String springErrorCode) {
		for (ErrorCode errorCode : ErrorCode.values()) {
			if (errorCode.codes.contains(springErrorCode) || errorCode.jsonValue.equals(springErrorCode)) {
				return errorCode;
			}
		}
		return null;
	}

	@JsonValue
	public String getJsonValue() {
		return jsonValue;
	}
}