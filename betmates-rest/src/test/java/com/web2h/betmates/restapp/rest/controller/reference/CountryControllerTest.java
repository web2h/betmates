package com.web2h.betmates.restapp.rest.controller.reference;

import static com.web2h.betmates.restapp.model.entity.FieldLength.NAME_MAX_LENGTH;
import static com.web2h.betmates.restapp.model.entity.FieldLength.TEXT_MIN_LENGTH;
import static com.web2h.betmates.restapp.rest.controller.UrlConstants.COUNTRY_CREATION_URL;
import static com.web2h.betmates.restapp.rest.controller.UrlConstants.COUNTRY_EDITION_URL;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;

import com.web2h.betmates.restapp.core.service.reference.CountryService;
import com.web2h.betmates.restapp.model.entity.FieldLength;
import com.web2h.betmates.restapp.model.entity.reference.Country;
import com.web2h.betmates.restapp.model.entity.user.AppUser;
import com.web2h.betmates.restapp.model.exception.AlreadyExistsException;
import com.web2h.betmates.restapp.model.exception.NotFoundException;
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

	@MockBean
	private CountryService countryService;

	@SpyBean
	private CountryController countryController;

	@Before
	public void before() throws Exception {
		given(countryService.create(any(Country.class), any(AppUser.class))).willReturn(null);
		doReturn(null).when(countryController).getLoggedInUser();
	}

	@Test
	public void create_WithValidCountry_ShouldReturnOk() throws Exception {
		Country country = createValidCountryForCreation();
		String jsonCountry = asJsonString(country);
		country.setId(1l);
		given(countryService.create(any(Country.class), any(AppUser.class))).willReturn(country);

		ResultActions actions = mockMvc.perform(post(COUNTRY_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(jsonCountry));
		actions.andExpect(status().isOk());
		Assert.assertEquals(asJsonString(country), actions.andReturn().getResponse().getContentAsString());
	}

	@Test
	public void create_WithValidCountryNotTrimmed_ShouldReturnOk() throws Exception {
		Country country = createValidCountryForCreation();
		String nameEn = StringTools.random(FieldLength.NAME_MAX_LENGTH);
		String nameFr = StringTools.random(FieldLength.NAME_MAX_LENGTH);
		country.setNameEn(StringTools.padWithBlanks(nameEn));
		country.setNameFr(StringTools.padWithBlanks(nameFr));
		String jsonCountry = asJsonString(country);

		country.setId(1l);
		country.setNameEn(nameEn);
		country.setNameFr(nameFr);
		given(countryService.create(any(Country.class), any(AppUser.class))).willReturn(country);

		ResultActions actions = mockMvc.perform(post(COUNTRY_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(jsonCountry));
		actions.andExpect(status().isOk());
		Assert.assertEquals(asJsonString(country), actions.andReturn().getResponse().getContentAsString());
	}

	@Test
	public void create_WithExistingEnglishName_ShouldReturnBadRequest() throws Exception {
		Country country = createValidCountryForCreation();
		given(countryService.create(any(Country.class), any(AppUser.class))).willThrow(new AlreadyExistsException(Field.NAME_EN, Country.class.getName()));

		ResultActions actions = mockMvc.perform(post(COUNTRY_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(country)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.message", equalTo(AlreadyExistsException.messages.get(Field.NAME_EN.name() + Country.class.getName()))));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.NAME_EN.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.ALREADY_EXISTS.getJsonValue())));
	}

	@Test
	public void create_WithExistingFrenchName_ShouldReturnBadRequest() throws Exception {
		Country country = createValidCountryForCreation();
		given(countryService.create(any(Country.class), any(AppUser.class))).willThrow(new AlreadyExistsException(Field.NAME_FR, Country.class.getName()));

		ResultActions actions = mockMvc.perform(post(COUNTRY_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(country)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.message", equalTo(AlreadyExistsException.messages.get(Field.NAME_FR.name() + Country.class.getName()))));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.NAME_FR.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.ALREADY_EXISTS.getJsonValue())));
	}

	@Test
	public void create_WithServerException_ShouldReturnInternalServerError() throws Exception {
		Country country = createValidCountryForCreation();
		given(countryService.create(any(Country.class), any(AppUser.class))).willThrow(new RuntimeException("Message"));

		ResultActions actions = mockMvc.perform(post(COUNTRY_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(country)));
		actions.andExpect(status().isInternalServerError());
		actions.andExpect(jsonPath("$.message", equalTo("Message")));
	}

	@Test
	public void create_WithProvidedId_ShouldReturnBadRequest() throws Exception {
		Country country = createValidCountryForCreation();
		country.setId(1l);
		testPostUrlAndExpectBadRequest(country, COUNTRY_CREATION_URL, Field.ID, ErrorCode.NOT_EMPTY);
	}

	@Test
	public void create_WithMissingEnglishName_ShouldReturnBadRequest() throws Exception {
		Country country = createValidCountryForCreation();
		country.setNameEn(null);
		testPostUrlAndExpectBadRequest(country, COUNTRY_CREATION_URL, Field.NAME_EN, ErrorCode.EMPTY);
	}

	@Test
	public void create_WithTooLongEnglishName_ShouldReturnBadRequest() throws Exception {
		Country country = createValidCountryForCreation();
		country.setNameEn(StringTools.random(NAME_MAX_LENGTH + 1));
		testPostUrlAndExpectBadRequest(country, COUNTRY_CREATION_URL, Field.NAME_EN, ErrorCode.TOO_LONG);
	}

	@Test
	public void create_WithTooShortEnglishName_ShouldReturnBadRequest() throws Exception {
		Country country = createValidCountryForCreation();
		country.setNameEn(StringTools.random(TEXT_MIN_LENGTH - 1));
		testPostUrlAndExpectBadRequest(country, COUNTRY_CREATION_URL, Field.NAME_EN, ErrorCode.TOO_SHORT);
	}

	@Test
	public void create_WithMissingFrenchName_ShouldReturnBadRequest() throws Exception {
		Country country = createValidCountryForCreation();
		country.setNameFr(null);
		testPostUrlAndExpectBadRequest(country, COUNTRY_CREATION_URL, Field.NAME_FR, ErrorCode.EMPTY);
	}

	@Test
	public void create_WithTooLongFrenchName_ShouldReturnBadRequest() throws Exception {
		Country country = createValidCountryForCreation();
		country.setNameFr(StringTools.random(NAME_MAX_LENGTH + 1));
		testPostUrlAndExpectBadRequest(country, COUNTRY_CREATION_URL, Field.NAME_FR, ErrorCode.TOO_LONG);
	}

	@Test
	public void create_WithTooShortFrenchName_ShouldReturnBadRequest() throws Exception {
		Country country = createValidCountryForCreation();
		country.setNameFr(StringTools.random(TEXT_MIN_LENGTH - 1));
		testPostUrlAndExpectBadRequest(country, COUNTRY_CREATION_URL, Field.NAME_FR, ErrorCode.TOO_SHORT);
	}

	@Test
	public void edit_WithValidCountry_ShouldReturnOk() throws Exception {
		Country country = createValidCountryForEdition();
		String jsonCountry = asJsonString(country);
		given(countryService.edit(any(Country.class), any(AppUser.class))).willReturn(country);

		ResultActions actions = mockMvc.perform(put(COUNTRY_EDITION_URL).contentType(MediaType.APPLICATION_JSON).content(jsonCountry));
		actions.andExpect(status().isOk());
		Assert.assertEquals(asJsonString(country), actions.andReturn().getResponse().getContentAsString());
	}

	@Test
	public void edit_UnknownCountry_ShouldReturnNotFound() throws Exception {
		Country country = createValidCountryForEdition();
		String jsonCountry = asJsonString(country);
		given(countryService.edit(any(Country.class), any(AppUser.class))).willThrow(new NotFoundException(Field.ID, Country.class.getName()));

		ResultActions actions = mockMvc.perform(put(COUNTRY_EDITION_URL).contentType(MediaType.APPLICATION_JSON).content(jsonCountry));
		actions.andExpect(status().isNotFound());
		actions.andExpect(jsonPath("$.message", equalTo(NotFoundException.messages.get(Field.ID.name() + Country.class.getName()))));
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.ID.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.NOT_FOUND.getJsonValue())));
	}

	@Test
	public void edit_WithExistingEnglishName_ShouldReturnBadRequest() throws Exception {
		Country country = createValidCountryForEdition();
		given(countryService.edit(any(Country.class), any(AppUser.class))).willThrow(new AlreadyExistsException(Field.NAME_EN, Country.class.getName()));

		ResultActions actions = mockMvc.perform(put(COUNTRY_EDITION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(country)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.message", equalTo(AlreadyExistsException.messages.get(Field.NAME_EN.name() + Country.class.getName()))));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.NAME_EN.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.ALREADY_EXISTS.getJsonValue())));
	}

	@Test
	public void edit_WithExistingFrenchName_ShouldReturnBadRequest() throws Exception {
		Country country = createValidCountryForEdition();
		given(countryService.edit(any(Country.class), any(AppUser.class))).willThrow(new AlreadyExistsException(Field.NAME_FR, Country.class.getName()));

		ResultActions actions = mockMvc.perform(put(COUNTRY_EDITION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(country)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.message", equalTo(AlreadyExistsException.messages.get(Field.NAME_FR.name() + Country.class.getName()))));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.NAME_FR.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.ALREADY_EXISTS.getJsonValue())));
	}

	@Test
	public void edit_WithServerException_ShouldReturnInternalServerError() throws Exception {
		Country country = createValidCountryForEdition();
		given(countryService.edit(any(Country.class), any(AppUser.class))).willThrow(new RuntimeException("Message"));

		ResultActions actions = mockMvc.perform(put(COUNTRY_EDITION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(country)));
		actions.andExpect(status().isInternalServerError());
		actions.andExpect(jsonPath("$.message", equalTo("Message")));
	}

	@Test
	public void edit_WithMissingId_ShouldReturnBadRequest() throws Exception {
		Country country = createValidCountryForEdition();
		country.setId(null);
		testPutUrlAndExpectBadRequest(country, COUNTRY_EDITION_URL, Field.ID, ErrorCode.EMPTY);
	}

	@Test
	public void edit_WithMissingEnglishName_ShouldReturnBadRequest() throws Exception {
		Country country = createValidCountryForEdition();
		country.setNameEn(null);
		testPutUrlAndExpectBadRequest(country, COUNTRY_EDITION_URL, Field.NAME_EN, ErrorCode.EMPTY);
	}

	@Test
	public void edit_WithTooLongEnglishName_ShouldReturnBadRequest() throws Exception {
		Country country = createValidCountryForEdition();
		country.setNameEn(StringTools.random(NAME_MAX_LENGTH + 1));
		testPutUrlAndExpectBadRequest(country, COUNTRY_EDITION_URL, Field.NAME_EN, ErrorCode.TOO_LONG);
	}

	@Test
	public void edit_WithTooShortEnglishName_ShouldReturnBadRequest() throws Exception {
		Country country = createValidCountryForEdition();
		country.setNameEn(StringTools.random(TEXT_MIN_LENGTH - 1));
		testPutUrlAndExpectBadRequest(country, COUNTRY_EDITION_URL, Field.NAME_EN, ErrorCode.TOO_SHORT);
	}

	@Test
	public void edit_WithMissingFrenchName_ShouldReturnBadRequest() throws Exception {
		Country country = createValidCountryForEdition();
		country.setNameFr(null);
		testPutUrlAndExpectBadRequest(country, COUNTRY_EDITION_URL, Field.NAME_FR, ErrorCode.EMPTY);
	}

	@Test
	public void edit_WithTooLongFrenchName_ShouldReturnBadRequest() throws Exception {
		Country country = createValidCountryForEdition();
		country.setNameFr(StringTools.random(NAME_MAX_LENGTH + 1));
		testPutUrlAndExpectBadRequest(country, COUNTRY_EDITION_URL, Field.NAME_FR, ErrorCode.TOO_LONG);
	}

	@Test
	public void edit_WithTooShortFrenchName_ShouldReturnBadRequest() throws Exception {
		Country country = createValidCountryForEdition();
		country.setNameFr(StringTools.random(TEXT_MIN_LENGTH - 1));
		testPutUrlAndExpectBadRequest(country, COUNTRY_EDITION_URL, Field.NAME_FR, ErrorCode.TOO_SHORT);
	}

	private Country createValidCountryForCreation() {
		Country country = new Country();
		country.setNameEn(StringTools.random(NAME_MAX_LENGTH));
		country.setNameFr(StringTools.random(NAME_MAX_LENGTH));
		return country;
	}

	private Country createValidCountryForEdition() {
		Country country = createValidCountryForCreation();
		country.setId(1l);
		return country;
	}
}