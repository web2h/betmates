package com.web2h.betmates.restapp.model.validation;

import org.springframework.validation.FieldError;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Data validation error POJO.
 * 
 * @author web2h
 */
@JsonInclude(Include.NON_NULL)
public class ValidationError {

	/** FIELD - Field concerned by the error. */
	private Field field;

	/** ERROR_CODE - Error code. */
	private ErrorCode errorCode;

	/** COMPLEMENT - Complement of the error (max length if TOO_LONG error). */
	private String complement;

	public ValidationError(FieldError springFieldError) {

		String fieldName = springFieldError.getField().substring(springFieldError.getField().lastIndexOf(".") + 1);
		field = Field.getByLowerCaseValue(fieldName);

		String code = springFieldError.getCodes()[springFieldError.getCodes().length - 1];
		if ("Size".equals(code)) {
			int minLength = Math.min((Integer) springFieldError.getArguments()[1],
					(Integer) springFieldError.getArguments()[2]);
			int maxLength = Math.max((Integer) springFieldError.getArguments()[1],
					(Integer) springFieldError.getArguments()[2]);
			if (((String) springFieldError.getRejectedValue()).length() > maxLength) {
				code = ErrorCode.TOO_LONG.getJsonValue();
				complement = String.valueOf(maxLength);
			} else {
				code = ErrorCode.TOO_SHORT.getJsonValue();
				complement = String.valueOf(minLength);
			}
		}
		errorCode = ErrorCode.getBySpringFieldError(code);
	}

	public ValidationError(Field field, ErrorCode errorCode) {
		this.field = field;
		this.errorCode = errorCode;
	}

	public ValidationError(Field field, ErrorCode errorCode, String complement) {
		this.field = field;
		this.errorCode = errorCode;
		this.complement = complement;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public String getComplement() {
		return complement;
	}

	public void setComplement(String complement) {
		this.complement = complement;
	}
}