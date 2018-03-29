package com.web2h.betmates.restapp.rest.controller.reference;

import static com.web2h.betmates.restapp.model.entity.FieldLength.NAME_MAX_LENGTH;
import static com.web2h.betmates.restapp.model.entity.FieldLength.TEXT_MIN_LENGTH;
import static com.web2h.betmates.restapp.rest.controller.UrlConstants.COUNTRY_CREATION_URL;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.web2h.betmates.restapp.core.service.reference.CountryService;
import com.web2h.betmates.restapp.core.service.user.UserService;
import com.web2h.betmates.restapp.model.entity.FieldLength;
import com.web2h.betmates.restapp.model.entity.reference.Country;
import com.web2h.betmates.restapp.model.exception.AlreadyExistsException;
import com.web2h.betmates.restapp.model.validation.ErrorCode;
import com.web2h.betmates.restapp.model.validation.Field;
import com.web2h.betmates.restapp.rest.controller.ApplicationTest;
import com.web2h.betmates.restapp.rest.controller.CommonControllerTest;
import com.web2h.betmates.restapp.rest.controller.WebSecurityTest;
import com.web2h.tools.StringTools;

@RunWith(SpringRunner.class)
@WebMvcTest(CountryController.class)
@ContextConfiguration(classes = { ApplicationTest.class })
@Import(value = WebSecurityTest.class)
@WithMockUser
public class CountryControllerTest extends CommonControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CountryService countryService;

	@MockBean
	private UserService userService;

	@MockBean
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@SpyBean
	private CountryController countryController;

	@Before
	public void before() throws Exception {
		given(countryService.create(anyObject(), anyObject())).willReturn(null);
		doReturn(null).when(countryController).getLoggedInUser();
	}

	@Test
	public void create_WithValidCountry_ShouldReturnOk() throws Exception {
		Country country = createValidCountry();
		String jsonCountry = asJsonString(country);
		country.setId(1l);
		given(countryService.create(anyObject(), anyObject())).willReturn(country);

		ResultActions actions = mockMvc.perform(post(COUNTRY_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(jsonCountry));
		actions.andExpect(status().isOk());
		Assert.assertEquals(asJsonString(country), actions.andReturn().getResponse().getContentAsString());
	}

	@Test
	public void create_WithValidCountryNotTrimmed_ShouldReturnOk() throws Exception {
		Country country = createValidCountry();
		String nameEn = StringTools.random(FieldLength.NAME_MAX_LENGTH);
		String nameFr = StringTools.random(FieldLength.NAME_MAX_LENGTH);
		country.setNameEn(StringTools.padWithBlanks(nameEn));
		country.setNameFr(StringTools.padWithBlanks(nameFr));
		String jsonCountry = asJsonString(country);

		country.setId(1l);
		country.setNameEn(nameEn);
		country.setNameFr(nameFr);
		given(countryService.create(anyObject(), anyObject())).willReturn(country);

		ResultActions actions = mockMvc.perform(post(COUNTRY_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(jsonCountry));
		actions.andExpect(status().isOk());
		Assert.assertEquals(asJsonString(country), actions.andReturn().getResponse().getContentAsString());
	}

	@Test
	public void create_WithExistingEnglishName_ShouldReturnBadRequest() throws Exception {
		Country country = createValidCountry();
		given(countryService.create(anyObject(), anyObject())).willThrow(new AlreadyExistsException(Field.NAME_EN, Country.class.getName()));

		ResultActions actions = mockMvc.perform(post(COUNTRY_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(country)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.message", equalTo(AlreadyExistsException.messages.get(Field.NAME_EN.name() + Country.class.getName()))));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.NAME_EN.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.ALREADY_EXISTS.getJsonValue())));
	}

	@Test
	public void create_WithExistingFrenchName_ShouldReturnBadRequest() throws Exception {
		Country country = createValidCountry();
		given(countryService.create(anyObject(), anyObject())).willThrow(new AlreadyExistsException(Field.NAME_FR, Country.class.getName()));

		ResultActions actions = mockMvc.perform(post(COUNTRY_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(country)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.message", equalTo(AlreadyExistsException.messages.get(Field.NAME_FR.name() + Country.class.getName()))));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.NAME_FR.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.ALREADY_EXISTS.getJsonValue())));
	}

	@Test
	public void create_WithServerException_ShouldReturnInternalServerError() throws Exception {
		Country country = createValidCountry();
		given(countryService.create(anyObject(), anyObject())).willThrow(new RuntimeException("Message"));

		ResultActions actions = mockMvc.perform(post(COUNTRY_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(country)));
		actions.andExpect(status().isInternalServerError());
		actions.andExpect(jsonPath("$.message", equalTo("Message")));
	}

	@Test
	public void create_WithProvidedId_ShouldReturnBadRequest() throws Exception {
		Country country = createValidCountry();
		country.setId(1l);

		ResultActions actions = mockMvc.perform(post(COUNTRY_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(country)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.ID.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.NOT_EMPTY.getJsonValue())));
	}

	@Test
	public void create_WithMissingEnglishName_ShouldReturnBadRequest() throws Exception {
		Country country = createValidCountry();
		country.setNameEn(null);

		ResultActions actions = mockMvc.perform(post(COUNTRY_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(country)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.NAME_EN.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.EMPTY.getJsonValue())));
	}

	@Test
	public void create_WithTooLongEnglishName_ShouldReturnBadRequest() throws Exception {
		Country country = createValidCountry();
		country.setNameEn(StringTools.random(NAME_MAX_LENGTH + 1));

		ResultActions actions = mockMvc.perform(post(COUNTRY_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(country)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.NAME_EN.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.TOO_LONG.getJsonValue())));
	}

	@Test
	public void create_WithTooShortEnglishName_ShouldReturnBadRequest() throws Exception {
		Country country = createValidCountry();
		country.setNameEn(StringTools.random(TEXT_MIN_LENGTH - 1));

		ResultActions actions = mockMvc.perform(post(COUNTRY_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(country)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.NAME_EN.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.TOO_SHORT.getJsonValue())));
	}

	@Test
	public void create_WithMissingFrenchName_ShouldReturnBadRequest() throws Exception {
		Country country = createValidCountry();
		country.setNameFr(null);

		ResultActions actions = mockMvc.perform(post(COUNTRY_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(country)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.NAME_FR.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.EMPTY.getJsonValue())));
	}

	@Test
	public void create_WithTooLongFrenchName_ShouldReturnBadRequest() throws Exception {
		Country country = createValidCountry();
		country.setNameFr(StringTools.random(NAME_MAX_LENGTH + 1));

		ResultActions actions = mockMvc.perform(post(COUNTRY_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(country)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.NAME_FR.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.TOO_LONG.getJsonValue())));
	}

	@Test
	public void create_WithTooShortFrenchName_ShouldReturnBadRequest() throws Exception {
		Country country = createValidCountry();
		country.setNameFr(StringTools.random(TEXT_MIN_LENGTH - 1));

		ResultActions actions = mockMvc.perform(post(COUNTRY_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(country)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.NAME_FR.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.TOO_SHORT.getJsonValue())));
	}

	private Country createValidCountry() {
		Country country = new Country();
		country.setNameEn(StringTools.random(NAME_MAX_LENGTH));
		country.setNameFr(StringTools.random(NAME_MAX_LENGTH));
		return country;
	}
}