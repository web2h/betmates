package com.web2h.betmates.restapp.rest.controller.user;

import static com.web2h.betmates.restapp.rest.controller.UrlConstants.SIGN_UP_URL;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;

import com.web2h.betmates.restapp.model.entity.FieldLength;
import com.web2h.betmates.restapp.model.entity.user.AppUser;
import com.web2h.betmates.restapp.model.entity.user.AppUserRole;
import com.web2h.betmates.restapp.model.exception.AlreadyExistsException;
import com.web2h.betmates.restapp.model.validation.ErrorCode;
import com.web2h.betmates.restapp.model.validation.Field;
import com.web2h.betmates.restapp.rest.controller.ApplicationTest;
import com.web2h.betmates.restapp.rest.controller.CommonControllerTest;
import com.web2h.betmates.restapp.rest.controller.WebSecurityTest;
import com.web2h.tools.StringTools;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@ContextConfiguration(classes = { ApplicationTest.class })
@Import(value = WebSecurityTest.class)
public class UserControllerTest extends CommonControllerTest {

	@Test
	public void signUp_WithValidUser_ShouldReturnOk() throws Exception {

		AppUser appUser = createValidAppUser();
		String jsonUser = asJsonString(appUser);
		appUser.setId(1l);

		given(userService.signUpAppUser(any(AppUser.class))).willReturn(appUser);

		ResultActions actions = mockMvc.perform(post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON).content(jsonUser));
		actions.andExpect(status().isOk());
		Assert.assertEquals(asJsonString(appUser), actions.andReturn().getResponse().getContentAsString());
	}

	@Test
	public void signUp_WithValidUserNotTrimmed_ShouldReturnOkWithTrimmedValues() throws Exception {

		AppUser appUser = createValidAppUser();
		String email = "test@email.com";
		String alias = StringTools.random(FieldLength.NAME_MAX_LENGTH);
		appUser.setEmail(StringTools.padWithBlanks(email));
		appUser.setAlias(StringTools.padWithBlanks(alias));
		String jsonUser = asJsonString(appUser);

		appUser.setId(1l);
		appUser.setEmail(email);
		appUser.setAlias(alias);
		given(userService.signUpAppUser(any(AppUser.class))).willReturn(appUser);

		ResultActions actions = mockMvc.perform(post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON).content(jsonUser));

		actions.andExpect(status().isOk());
		Assert.assertEquals(asJsonString(appUser), actions.andReturn().getResponse().getContentAsString());
	}

	@Test
	public void signUp_WithExistingUserEmail_ShouldReturnBadRequest() throws Exception {
		AppUser appUser = createValidAppUser();
		given(userService.signUpAppUser(any(AppUser.class))).willThrow(new AlreadyExistsException(Field.EMAIL));

		ResultActions actions = mockMvc.perform(post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(appUser)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.message", equalTo(AlreadyExistsException.messages.get(Field.EMAIL.name()))));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.EMAIL.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.ALREADY_EXISTS.getJsonValue())));
	}

	@Test
	public void signUp_WithExistingUserAlias_ShouldReturnBadRequest() throws Exception {
		AppUser appUser = createValidAppUser();
		given(userService.signUpAppUser(any(AppUser.class))).willThrow(new AlreadyExistsException(Field.ALIAS));

		ResultActions actions = mockMvc.perform(post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(appUser)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.message", equalTo(AlreadyExistsException.messages.get(Field.ALIAS.name()))));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.ALIAS.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.ALREADY_EXISTS.getJsonValue())));
	}

	@Test
	public void signUp_WithServerException_ShouldReturnInternalServerError() throws Exception {
		AppUser appUser = createValidAppUser();
		given(userService.signUpAppUser(any(AppUser.class))).willThrow(new RuntimeException("Message"));

		ResultActions actions = mockMvc.perform(post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(appUser)));
		actions.andExpect(status().isInternalServerError());
		actions.andExpect(jsonPath("$.message", equalTo("Message")));
	}

	@Test
	public void signUp_WithMissingEmail_ShouldReturnBadRequest() throws Exception {
		AppUser appUser = createValidAppUser();
		appUser.setEmail(null);

		ResultActions actions = mockMvc.perform(post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(appUser)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.EMAIL.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.EMPTY.getJsonValue())));
	}

	@Test
	public void signUp_WithInvalidEmail_ShouldReturnBadRequest() throws Exception {
		AppUser appUser = createValidAppUser();
		appUser.setEmail("invalid.email.com");

		ResultActions actions = mockMvc.perform(post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(appUser)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.EMAIL.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.INVALID_EMAIL.getJsonValue())));
	}

	@Test
	public void signUp_WithTooLongEmail_ShouldReturnBadRequest() throws Exception {
		AppUser appUser = createValidAppUser();
		appUser.setEmail(StringTools.random(FieldLength.EMAIL_MAX_LENGTH) + "@email.com");

		ResultActions actions = mockMvc.perform(post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(appUser)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.EMAIL.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.TOO_LONG.getJsonValue())));
	}

	@Test
	public void signUp_WithMissingAlias_ShouldReturnBadRequest() throws Exception {
		AppUser appUser = createValidAppUser();
		appUser.setAlias(null);

		ResultActions actions = mockMvc.perform(post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(appUser)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.ALIAS.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.EMPTY.getJsonValue())));
	}

	@Test
	public void signUp_WithTooShortAlias_ShouldReturnBadRequest() throws Exception {
		AppUser appUser = createValidAppUser();
		appUser.setAlias(StringTools.random(FieldLength.TEXT_MIN_LENGTH - 1));

		ResultActions actions = mockMvc.perform(post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(appUser)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.ALIAS.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.TOO_SHORT.getJsonValue())));
	}

	@Test
	public void signUp_WithTooLongAlias_ShouldReturnBadRequest() throws Exception {
		AppUser appUser = createValidAppUser();
		appUser.setAlias(StringTools.random(FieldLength.NAME_MAX_LENGTH + 1));

		ResultActions actions = mockMvc.perform(post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(appUser)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.ALIAS.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.TOO_LONG.getJsonValue())));
	}

	@Test
	public void signUp_WithMissingRole_ShouldReturnBadRequest() throws Exception {
		AppUser appUser = createValidAppUser();
		appUser.setRole(null);

		ResultActions actions = mockMvc.perform(post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(appUser)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.ROLE.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.EMPTY.getJsonValue())));
	}

	@Test
	public void signUp_WithMissingPassword_ShouldReturnBadRequest() throws Exception {
		AppUser appUser = createValidAppUser();
		appUser.setPassword(null);

		ResultActions actions = mockMvc.perform(post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(appUser)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.PASSWORD.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.EMPTY.getJsonValue())));
	}

	@Test
	public void signUp_WithTooShortPassword_ShouldReturnBadRequest() throws Exception {
		AppUser appUser = createValidAppUser();
		appUser.setPassword(StringTools.random(FieldLength.PASSWORD_MIN_LENGTH - 1));

		ResultActions actions = mockMvc.perform(post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(appUser)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.PASSWORD.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.TOO_SHORT.getJsonValue())));
	}

	private AppUser createValidAppUser() {
		AppUser appUser = new AppUser();
		appUser.setEmail("email@valid.com");
		appUser.setAlias(StringTools.random(FieldLength.NAME_MAX_LENGTH));
		appUser.setPassword("12345678");
		appUser.setRole(AppUserRole.ROLE_PLAYER);
		return appUser;
	}
}