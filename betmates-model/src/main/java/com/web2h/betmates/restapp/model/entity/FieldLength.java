package com.web2h.betmates.restapp.model.entity;

/**
 * Field max and min lengths. Constants list.
 * 
 * @author web2h
 */
public interface FieldLength {

	int ADDRESS_MAX_LENGTH = 256;

	int COUNTRY_CODE_MAX_LENGTH = 3;

	int COUNTRY_CODE_MIN_LENGTH = 3;

	int EMAIL_MAX_LENGTH = 128;

	int FUNCTION_MAX_LENGTH = 16;

	int LONG_NAME_MAX_LENGTH = 128;

	int NAME_MAX_LENGTH = 64;

	int NAME_MIN_LENGTH = 2;

	int PASSWORD_MAX_LENGTH = 128;

	int PASSWORD_MIN_LENGTH = 8;

	int POSTAL_CODE_MAX_LENGTH = 16;

	int POSTAL_CODE_MIN_LENGTH = 4;

	int REGION_MAX_LENGTH = 32;

	int REGION_MIN_LENGTH = 2;

	int STATUS_MAX_LENGTH = 16;
}