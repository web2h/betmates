package com.web2h.betmates.restapp.model.exception;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.web2h.betmates.restapp.model.entity.reference.Reference;
import com.web2h.betmates.restapp.model.http.ErrorResponse;
import com.web2h.betmates.restapp.model.validation.ErrorCode;
import com.web2h.betmates.restapp.model.validation.Field;
import com.web2h.betmates.restapp.model.validation.ValidationError;

public class InvalidDataException extends Exception {

	public static final String DEFAULT_MESSAGE = "Invalid data was provided";

	private static final long serialVersionUID = 1L;

	private ErrorResponse errorResponse;

	public InvalidDataException(List<ObjectError> errors) {
		super(DEFAULT_MESSAGE);
		errorResponse = new ErrorResponse(getMessage());

		for (ObjectError error : errors) {
			errorResponse.getErrors().add(new ValidationError((FieldError) error));
		}
	}

	@SuppressWarnings("rawtypes")
	public InvalidDataException(Set notFoundReferences, Field field) {
		super(DEFAULT_MESSAGE);
		errorResponse = new ErrorResponse(getMessage());

		for (Object reference : notFoundReferences) {
			ValidationError validationError = new ValidationError(field, ErrorCode.NOT_FOUND);
			validationError.setOriginalValue(((Reference) reference).getLogValue());
			errorResponse.getErrors().add(validationError);
		}
	}

	public InvalidDataException(Field field, ErrorCode errorCode) {
		super(DEFAULT_MESSAGE);
		errorResponse = new ErrorResponse(getMessage());
		errorResponse.getErrors().add(new ValidationError(field, errorCode));
	}

	public ResponseEntity<Object> getResponseEntity() {
		return new ResponseEntity<Object>(errorResponse, HttpStatus.BAD_REQUEST);
	}
}