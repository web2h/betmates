package com.web2h.betmates.restapp.resp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CommonControllerTest {

	/**
	 * Converts a JAVA object into its JSON representation.
	 * 
	 * @param object
	 *            The JAVA object to convert
	 * @return The JSON representation
	 */
	public static String asJsonString(final Object object) {
		try {
			return new ObjectMapper().writeValueAsString(object);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}