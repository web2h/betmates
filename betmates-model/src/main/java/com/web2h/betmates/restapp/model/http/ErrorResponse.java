package com.web2h.betmates.restapp.model.http;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.web2h.betmates.restapp.model.validation.ErrorCode;
import com.web2h.betmates.restapp.model.validation.Field;
import com.web2h.betmates.restapp.model.validation.ValidationError;

/**
 * POJO returned to client when an error occurs. The error can be described with a message, the original object from the client request, a list of errors (form
 * submission with multiple errors) ...
 * 
 * @author web2h*
 */
@JsonInclude(Include.NON_NULL)
public class ErrorResponse {

	/** MESSAGE - The main error message. */
	private String message;

	/** OBJECT_FROM_REQUEST - The original object send by the client. */
	private Object objectFromRequest;

	/** ERRORS - List of errors in case of data validation. */
	private List<ValidationError> errors;

	public ErrorResponse(String message) {
		this.message = message;
		errors = new ArrayList<>();
	}

	/**
	 * Constructs a new error response with a validation error.
	 * 
	 * @param message
	 *            Exception message
	 * @param field
	 *            Field concerned by the exception
	 * @param errorCode
	 *            Error code
	 */
	public ErrorResponse(String message, Field field, ErrorCode errorCode) {
		this.message = message;
		errors = new ArrayList<>();
		errors.add(new ValidationError(field, errorCode));
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getObjectFromRequest() {
		return objectFromRequest;
	}

	public void setObjectFromRequest(Object objectFromRequest) {
		this.objectFromRequest = objectFromRequest;
	}

	public List<ValidationError> getErrors() {
		return errors;
	}
}