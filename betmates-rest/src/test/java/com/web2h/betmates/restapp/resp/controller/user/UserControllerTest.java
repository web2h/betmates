package com.web2h.betmates.restapp.resp.controller.user;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyObject;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.web2h.betmates.restapp.core.service.user.UserService;
import com.web2h.betmates.restapp.model.entity.FieldLength;
import com.web2h.betmates.restapp.model.entity.user.AppUser;
import com.web2h.betmates.restapp.model.entity.user.AppUserRole;
import com.web2h.betmates.restapp.resp.controller.ApplicationTest;
import com.web2h.betmates.restapp.resp.controller.CommonControllerTest;
import com.web2h.betmates.restapp.resp.controller.WebSecurityTest;
import com.web2h.betmates.restapp.rest.controller.user.UserController;
import com.web2h.tools.StringTools;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@ContextConfiguration(classes = { ApplicationTest.class })
@Import(value = WebSecurityTest.class)
public class UserControllerTest extends CommonControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@MockBean
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Test
	public void signUp_WithValidUser_ShouldReturnOk() throws Exception {

		AppUser user = createValidUserForCreation();
		String userJson = asJsonString(user);
		user.setId(1l);
		given(userService.signUpAppUser(anyObject())).willReturn(user);

		ResultActions actions = mockMvc.perform(post("/user/sign-up").contentType(MediaType.APPLICATION_JSON).content(userJson));

		actions.andExpect(status().isOk());
		actions.andExpect(jsonPath("$.id", equalTo(1)));
		actions.andExpect(jsonPath("$.email", equalTo(user.getEmail())));
		actions.andExpect(jsonPath("$.alias", equalTo(user.getAlias())));
		actions.andExpect(jsonPath("$.role", equalTo(user.getRole().name())));
		actions.andExpect(jsonPath("$.password", nullValue()));
		actions.andExpect(jsonPath("$.status", nullValue()));
	}

	/*
	 * @Test public void signUp_WithValidUserNotTrimmed_ShouldReturnOkWithTrimmedValues() throws Exception {
	 * 
	 * AppUserJson appUser = new AppUserJson(); String email = "test@email.com"; String firstName = StringTools.random(FieldLengths.NAME_MAX_LENGTH); String
	 * lastName = StringTools.random(FieldLengths.NAME_MAX_LENGTH); appUser.setEmail("   " + email + " "); appUser.setFirstName("  " + firstName + "  ");
	 * appUser.setLastName("  " + lastName + " "); appUser.setPassword(StringTools.random(FieldLengths.PASSWORD_MIN_LENGTH));
	 * 
	 * AppUserJson createdAppUser = new AppUserJson(); createdAppUser.setId(1l); createdAppUser.setEmail(appUser.getEmail());
	 * createdAppUser.setFirstName(appUser.getFirstName()); createdAppUser.setLastName(appUser.getLastName());
	 * given(userService.signUpAppUser(anyObject())).willReturn(createdAppUser);
	 * 
	 * ResultActions actions = mockMvc.perform(post("/users/sign-up").contentType(MediaType.APPLICATION_JSON).content(asJsonString(appUser)));
	 * 
	 * actions.andExpect(status().isOk()); actions.andExpect(jsonPath("$.id", equalTo(1))); actions.andExpect(jsonPath("$.email", equalTo(appUser.getEmail())));
	 * actions.andExpect(jsonPath("$.firstName", equalTo(appUser.getFirstName()))); actions.andExpect(jsonPath("$.lastName", equalTo(appUser.getLastName()))); }
	 */

	/*
	 * @Test public void signUp_WithExistingUser_ShouldReturnBadRequest() throws Exception { AppUserJson appUser = new AppUserJson();
	 * appUser.setEmail("test@email.com"); appUser.setFirstName(StringTools.random(FieldLengths.NAME_MAX_LENGTH));
	 * appUser.setLastName(StringTools.random(FieldLengths.NAME_MAX_LENGTH)); appUser.setPassword(StringTools.random(FieldLengths.PASSWORD_MIN_LENGTH));
	 * 
	 * given(userService.signUpAppUser(anyObject())).willThrow(new AlreadyExistsException("Message", Field.EMAIL));
	 * 
	 * ResultActions actions = mockMvc.perform(post("/users/sign-up").contentType(MediaType.APPLICATION_JSON).content(asJsonString(appUser)));
	 * actions.andExpect(status().isBadRequest()); actions.andExpect(jsonPath("$.errors", hasSize(1))); actions.andExpect(jsonPath("$.message",
	 * equalTo("Message"))); actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.EMAIL.toString()))); actions.andExpect(jsonPath("$.errors[0].errorCode",
	 * equalTo(ErrorCode.ALREADY_EXISTS.getJsonValue()))); }
	 * 
	 * @Test public void signUp_WithServerException_ShouldReturnInternalServerError() throws Exception { AppUserJson appUser = new AppUserJson();
	 * appUser.setEmail("test@email.com"); appUser.setFirstName(StringTools.random(FieldLengths.NAME_MAX_LENGTH));
	 * appUser.setLastName(StringTools.random(FieldLengths.NAME_MAX_LENGTH)); appUser.setPassword(StringTools.random(FieldLengths.PASSWORD_MIN_LENGTH));
	 * 
	 * given(userService.signUpAppUser(anyObject())).willThrow(new RuntimeException("Message"));
	 * 
	 * ResultActions actions = mockMvc.perform(post("/users/sign-up").contentType(MediaType.APPLICATION_JSON).content(asJsonString(appUser)));
	 * actions.andExpect(status().isInternalServerError()); actions.andExpect(jsonPath("$.message", equalTo("Message"))); }
	 */

	/*
	 * @Test public void signUp_WithMissingEmail_ShouldReturnBadRequest() throws Exception { AppUser appUser = createValidUserForCreation(); appUser.setEmail(null);
	 * 
	 * given(userService.signUpAppUser(anyObject())).willThrow(new RuntimeException("Message"));
	 * 
	 * ResultActions actions = mockMvc.perform(post(URL_SIGN_UP).contentType(MediaType.APPLICATION_JSON).content(asJsonString(appUser)));
	 * actions.andExpect(status().isBadRequest()); actions.andExpect(jsonPath("$.errors", hasSize(1))); actions.andExpect(jsonPath("$.errors[0].field",
	 * equalTo(Field.EMAIL.toString()))); actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.EMPTY.getJsonValue()))); }
	 * 
	 * @Test public void signUp_WithInvalidEmail_ShouldReturnBadRequest() throws Exception { AppUser appUser = createValidUserForCreation();
	 * appUser.setEmail("invalid.email.com");
	 * 
	 * given(userService.signUpAppUser(anyObject())).willThrow(new RuntimeException("Message"));
	 * 
	 * ResultActions actions = mockMvc.perform(post(URL_SIGN_UP).contentType(MediaType.APPLICATION_JSON).content(asJsonString(appUser)));
	 * actions.andExpect(status().isBadRequest()); actions.andExpect(jsonPath("$.errors", hasSize(1))); actions.andExpect(jsonPath("$.errors[0].field",
	 * equalTo(Field.EMAIL.toString()))); actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.INVALID_EMAIL.getJsonValue()))); }
	 * 
	 * @Test public void signUp_WithTooLongEmail_ShouldReturnBadRequest() throws Exception { AppUser appUser = createValidUserForCreation();
	 * appUser.setEmail(StringTools.random(FieldLength.EMAIL_MAX_LENGTH) + "@email.com");
	 * 
	 * given(userService.signUpAppUser(anyObject())).willThrow(new RuntimeException("Message"));
	 * 
	 * ResultActions actions = mockMvc.perform(post(URL_SIGN_UP).contentType(MediaType.APPLICATION_JSON).content(asJsonString(appUser)));
	 * actions.andExpect(status().isBadRequest()); actions.andExpect(jsonPath("$.errors", hasSize(1))); actions.andExpect(jsonPath("$.errors[0].field",
	 * equalTo(Field.EMAIL.toString()))); actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.TOO_LONG.getJsonValue()))); }
	 * 
	 * @Test public void signUp_WithMissingPassword_ShouldReturnBadRequest() throws Exception { AppUser appUser = createValidUserForCreation();
	 * appUser.setPassword(null);
	 * 
	 * given(userService.signUpAppUser(anyObject())).willThrow(new RuntimeException("Message"));
	 * 
	 * ResultActions actions = mockMvc.perform(post(URL_SIGN_UP).contentType(MediaType.APPLICATION_JSON).content(asJsonString(appUser)));
	 * actions.andExpect(status().isBadRequest()); actions.andExpect(jsonPath("$.errors", hasSize(1))); actions.andExpect(jsonPath("$.errors[0].field",
	 * equalTo(Field.PASSWORD.toString()))); actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.EMPTY.getJsonValue()))); }
	 * 
	 * @Test public void signUp_WithTooShortPassword_ShouldReturnBadRequest() throws Exception { AppUser appUser = createValidUserForCreation();
	 * appUser.setPassword(StringTools.random(PASSWORD_MIN_LENGTH - 1));
	 * 
	 * given(userService.signUpAppUser(anyObject())).willThrow(new RuntimeException("Message"));
	 * 
	 * ResultActions actions = mockMvc.perform(post(URL_SIGN_UP).contentType(MediaType.APPLICATION_JSON).content(asJsonString(appUser)));
	 * actions.andExpect(status().isBadRequest()); actions.andExpect(jsonPath("$.errors", hasSize(1))); actions.andExpect(jsonPath("$.errors[0].field",
	 * equalTo(Field.PASSWORD.toString()))); actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.TOO_SHORT.getJsonValue()))); }
	 */
	private AppUser createValidUserForCreation() {
		AppUser appUser = new AppUser();
		appUser.setEmail("player@betmates.com");
		appUser.setAlias(StringTools.random(FieldLength.NAME_MAX_LENGTH));
		appUser.setPassword(StringTools.random(FieldLength.PASSWORD_MIN_LENGTH));
		appUser.setRole(AppUserRole.ROLE_PLAYER);

		return appUser;
	}
}