package com.web2h.betmates.restapp.rest.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web2h.betmates.restapp.core.service.user.UserService;
import com.web2h.betmates.restapp.model.validation.ErrorCode;
import com.web2h.betmates.restapp.model.validation.Field;

public class CommonControllerTest {

	@Autowired
	protected MockMvc mockMvc;

	@MockBean
	protected UserService userService;

	@MockBean
	protected BCryptPasswordEncoder bCryptPasswordEncoder;

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

	protected void testPostUrlAndExpectBadRequest(Object objectToTest, String url, Field field, ErrorCode errorCode) throws Exception {
		ResultActions actions = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(asJsonString(objectToTest)));
		expectBadRequest(actions, field, errorCode);
	}

	protected void testPutUrlAndExpectBadRequest(Object objectToTest, String url, Field field, ErrorCode errorCode) throws Exception {
		ResultActions actions = mockMvc.perform(put(url).contentType(MediaType.APPLICATION_JSON).content(asJsonString(objectToTest)));
		expectBadRequest(actions, field, errorCode);
	}

	private void expectBadRequest(ResultActions actions, Field field, ErrorCode errorCode) throws Exception {
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(field.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(errorCode.getJsonValue())));
	}
}