package com.web2h.betmates.restapp.model.exception;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.web2h.betmates.restapp.model.entity.competition.CompetitionTeam;
import com.web2h.betmates.restapp.model.entity.reference.Venue;
import com.web2h.betmates.restapp.model.http.ErrorResponse;
import com.web2h.betmates.restapp.model.validation.ErrorCode;
import com.web2h.betmates.restapp.model.validation.Field;
import com.web2h.betmates.restapp.model.validation.ValidationError;

public class InvalidDataException extends Exception {

	public static final String DEFAULT_MESSAGE = "Invalid data was provided";

	private static final long serialVersionUID = 1L;

	private ErrorResponse errorResponse;

	public InvalidDataException() {
		super(DEFAULT_MESSAGE);
		errorResponse = new ErrorResponse(getMessage());
	}

	public static InvalidDataException createWithErrorList(List<ObjectError> errors) {
		InvalidDataException ide = new InvalidDataException();

		for (ObjectError error : errors) {
			ide.getErrorResponse().getErrors().add(new ValidationError((FieldError) error));
		}
		return ide;
	}

	public static InvalidDataException createWithFieldAndErrorCode(Field field, ErrorCode errorCode) {
		InvalidDataException ide = new InvalidDataException();
		ide.getErrorResponse().getErrors().add(new ValidationError(field, errorCode));
		return ide;
	}

	public static InvalidDataException createWithUnfoundTeams(Set<CompetitionTeam> notFoundTeams) {
		InvalidDataException ide = new InvalidDataException();

		for (CompetitionTeam team : notFoundTeams) {
			ValidationError validationError = new ValidationError(Field.TEAM, ErrorCode.NOT_FOUND);
			validationError.setOriginalValue(team.getTeam().getLogValue());
			ide.getErrorResponse().getErrors().add(validationError);
		}
		return ide;
	}

	public static InvalidDataException createWithUnfoundVenues(Set<Venue> notFoundVenues) {
		InvalidDataException ide = new InvalidDataException();

		for (Venue venue : notFoundVenues) {
			ValidationError validationError = new ValidationError(Field.VENUE, ErrorCode.NOT_FOUND);
			validationError.setOriginalValue(venue.getLogValue());
			ide.getErrorResponse().getErrors().add(validationError);
		}
		return ide;
	}

	public ResponseEntity<Object> getResponseEntity() {
		return new ResponseEntity<Object>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	public ErrorResponse getErrorResponse() {
		return errorResponse;
	}
}