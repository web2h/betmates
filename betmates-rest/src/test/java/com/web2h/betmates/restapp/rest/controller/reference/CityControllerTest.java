package com.web2h.betmates.restapp.rest.controller.reference;

import static com.web2h.betmates.restapp.model.entity.FieldLength.NAME_MAX_LENGTH;
import static com.web2h.betmates.restapp.model.entity.FieldLength.TEXT_MIN_LENGTH;
import static com.web2h.betmates.restapp.rest.controller.UrlConstants.CITY_CREATION_URL;
import static com.web2h.betmates.restapp.rest.controller.UrlConstants.CITY_EDITION_URL;
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

import com.web2h.betmates.restapp.core.service.reference.CityService;
import com.web2h.betmates.restapp.model.entity.FieldLength;
import com.web2h.betmates.restapp.model.entity.reference.City;
import com.web2h.betmates.restapp.model.entity.reference.Country;
import com.web2h.betmates.restapp.model.entity.user.AppUser;
import com.web2h.betmates.restapp.model.exception.AlreadyExistsException;
import com.web2h.betmates.restapp.model.exception.InvalidDataException;
import com.web2h.betmates.restapp.model.exception.NotFoundException;
import com.web2h.betmates.restapp.model.validation.ErrorCode;
import com.web2h.betmates.restapp.model.validation.Field;
import com.web2h.betmates.restapp.rest.controller.ApplicationTest;
import com.web2h.betmates.restapp.rest.controller.CommonControllerTest;
import com.web2h.betmates.restapp.rest.controller.WebSecurityTest;
import com.web2h.tools.StringTools;

@RunWith(SpringRunner.class)
@WebMvcTest(CityController.class)
@ContextConfiguration(classes = { ApplicationTest.class })
@Import(value = WebSecurityTest.class)
@WithMockUser
public class CityControllerTest extends CommonControllerTest {

	@MockBean
	private CityService cityService;

	@SpyBean
	private CityController cityController;

	@Before
	public void before() throws Exception {
		given(cityService.create(any(City.class), any(AppUser.class))).willReturn(null);
		doReturn(null).when(cityController).getLoggedInUser();
	}

	@Test
	public void create_WithValidCity_ShouldReturnOk() throws Exception {
		City city = createValidCityForCreation();
		String jsonCity = asJsonString(city);
		city.setId(1l);
		given(cityService.create(any(City.class), any(AppUser.class))).willReturn(city);

		ResultActions actions = mockMvc.perform(post(CITY_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(jsonCity));
		actions.andExpect(status().isOk());
		Assert.assertEquals(asJsonString(city), actions.andReturn().getResponse().getContentAsString());
	}

	@Test
	public void create_WithValidCityNotTrimmed_ShouldReturnOk() throws Exception {
		City city = createValidCityForCreation();
		String nameEn = StringTools.random(FieldLength.NAME_MAX_LENGTH);
		String nameFr = StringTools.random(FieldLength.NAME_MAX_LENGTH);
		city.setNameEn(StringTools.padWithBlanks(nameEn));
		city.setNameFr(StringTools.padWithBlanks(nameFr));
		String jsonCity = asJsonString(city);
		city.setId(1l);
		city.setNameEn(nameEn);
		city.setNameFr(nameFr);
		given(cityService.create(any(City.class), any(AppUser.class))).willReturn(city);

		ResultActions actions = mockMvc.perform(post(CITY_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(jsonCity));
		actions.andExpect(status().isOk());
		Assert.assertEquals(asJsonString(city), actions.andReturn().getResponse().getContentAsString());
	}

	@Test
	public void create_WithExistingEnglishName_ShouldReturnBadRequest() throws Exception {
		City city = createValidCityForCreation();
		given(cityService.create(any(City.class), any(AppUser.class))).willThrow(new AlreadyExistsException(Field.NAME_EN, City.class.getName()));

		ResultActions actions = mockMvc.perform(post(CITY_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(city)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.message", equalTo(AlreadyExistsException.messages.get(Field.NAME_EN.name() + City.class.getName()))));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.NAME_EN.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.ALREADY_EXISTS.getJsonValue())));
	}

	@Test
	public void create_WithExistingFrenchName_ShouldReturnBadRequest() throws Exception {
		City city = createValidCityForCreation();
		given(cityService.create(any(City.class), any(AppUser.class))).willThrow(new AlreadyExistsException(Field.NAME_FR, City.class.getName()));

		ResultActions actions = mockMvc.perform(post(CITY_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(city)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.message", equalTo(AlreadyExistsException.messages.get(Field.NAME_FR.name() + City.class.getName()))));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.NAME_FR.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.ALREADY_EXISTS.getJsonValue())));
	}

	@Test
	public void create_WithUnknownCountry_ShouldReturnBadRequest() throws Exception {
		City city = createValidCityForCreation();
		given(cityService.create(any(City.class), any(AppUser.class))).willThrow(InvalidDataException.createWithFieldAndErrorCode(Field.COUNTRY, ErrorCode.NOT_FOUND));

		ResultActions actions = mockMvc.perform(post(CITY_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(city)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.message", equalTo(InvalidDataException.DEFAULT_MESSAGE)));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.COUNTRY.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.NOT_FOUND.getJsonValue())));
	}

	@Test
	public void create_WithServerException_ShouldReturnInternalServerError() throws Exception {
		City city = createValidCityForCreation();
		given(cityService.create(any(City.class), any(AppUser.class))).willThrow(new RuntimeException("Message"));

		ResultActions actions = mockMvc.perform(post(CITY_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(city)));
		actions.andExpect(status().isInternalServerError());
		actions.andExpect(jsonPath("$.message", equalTo("Message")));
	}

	@Test
	public void create_WithProvidedId_ShouldReturnBadRequest() throws Exception {
		City city = createValidCityForCreation();
		city.setId(1l);
		testPostUrlAndExpectBadRequest(city, CITY_CREATION_URL, Field.ID, ErrorCode.NOT_EMPTY);
	}

	@Test
	public void create_WithMissingEnglishName_ShouldReturnBadRequest() throws Exception {
		City city = createValidCityForCreation();
		city.setNameEn(null);
		testPostUrlAndExpectBadRequest(city, CITY_CREATION_URL, Field.NAME_EN, ErrorCode.EMPTY);
	}

	@Test
	public void create_WithTooLongEnglishName_ShouldReturnBadRequest() throws Exception {
		City city = createValidCityForCreation();
		city.setNameEn(StringTools.random(NAME_MAX_LENGTH + 1));
		testPostUrlAndExpectBadRequest(city, CITY_CREATION_URL, Field.NAME_EN, ErrorCode.TOO_LONG);
	}

	@Test
	public void create_WithTooShortEnglishName_ShouldReturnBadRequest() throws Exception {
		City city = createValidCityForCreation();
		city.setNameEn(StringTools.random(TEXT_MIN_LENGTH - 1));
		testPostUrlAndExpectBadRequest(city, CITY_CREATION_URL, Field.NAME_EN, ErrorCode.TOO_SHORT);
	}

	@Test
	public void create_WithMissingFrenchName_ShouldReturnBadRequest() throws Exception {
		City city = createValidCityForCreation();
		city.setNameFr(null);
		testPostUrlAndExpectBadRequest(city, CITY_CREATION_URL, Field.NAME_FR, ErrorCode.EMPTY);
	}

	@Test
	public void create_WithTooLongFrenchName_ShouldReturnBadRequest() throws Exception {
		City city = createValidCityForCreation();
		city.setNameFr(StringTools.random(NAME_MAX_LENGTH + 1));
		testPostUrlAndExpectBadRequest(city, CITY_CREATION_URL, Field.NAME_FR, ErrorCode.TOO_LONG);
	}

	@Test
	public void create_WithTooShortFrenchName_ShouldReturnBadRequest() throws Exception {
		City city = createValidCityForCreation();
		city.setNameFr(StringTools.random(TEXT_MIN_LENGTH - 1));
		testPostUrlAndExpectBadRequest(city, CITY_CREATION_URL, Field.NAME_FR, ErrorCode.TOO_SHORT);
	}

	@Test
	public void create_WithMissingCountry_ShouldReturnBadRequest() throws Exception {
		City city = createValidCityForCreation();
		city.setCountry(null);
		testPostUrlAndExpectBadRequest(city, CITY_CREATION_URL, Field.COUNTRY, ErrorCode.EMPTY);
	}

	@Test
	public void edit_WithValidCity_ShouldReturnOk() throws Exception {
		City city = createValidCityForEdition();
		String jsonCity = asJsonString(city);
		given(cityService.edit(any(City.class), any(AppUser.class))).willReturn(city);

		ResultActions actions = mockMvc.perform(put(CITY_EDITION_URL).contentType(MediaType.APPLICATION_JSON).content(jsonCity));
		actions.andExpect(status().isOk());
		Assert.assertEquals(asJsonString(city), actions.andReturn().getResponse().getContentAsString());
	}

	@Test
	public void edit_UnknownCity_ShouldReturnNotFound() throws Exception {
		City city = createValidCityForEdition();
		String jsonCity = asJsonString(city);
		given(cityService.edit(any(City.class), any(AppUser.class))).willThrow(new NotFoundException(Field.ID, City.class.getName()));

		ResultActions actions = mockMvc.perform(put(CITY_EDITION_URL).contentType(MediaType.APPLICATION_JSON).content(jsonCity));
		actions.andExpect(status().isNotFound());
		actions.andExpect(jsonPath("$.message", equalTo(NotFoundException.messages.get(Field.ID.name() + City.class.getName()))));
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.ID.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.NOT_FOUND.getJsonValue())));
	}

	@Test
	public void edit_WithUnknownCountry_ShouldReturnBadRequest() throws Exception {
		City city = createValidCityForEdition();
		given(cityService.edit(any(City.class), any(AppUser.class))).willThrow(InvalidDataException.createWithFieldAndErrorCode(Field.COUNTRY, ErrorCode.NOT_FOUND));

		ResultActions actions = mockMvc.perform(put(CITY_EDITION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(city)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.message", equalTo(InvalidDataException.DEFAULT_MESSAGE)));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.COUNTRY.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.NOT_FOUND.getJsonValue())));
	}

	@Test
	public void edit_WithExistingEnglishName_ShouldReturnBadRequest() throws Exception {
		City city = createValidCityForEdition();
		given(cityService.edit(any(City.class), any(AppUser.class))).willThrow(new AlreadyExistsException(Field.NAME_EN, City.class.getName()));

		ResultActions actions = mockMvc.perform(put(CITY_EDITION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(city)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.message", equalTo(AlreadyExistsException.messages.get(Field.NAME_EN.name() + City.class.getName()))));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.NAME_EN.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.ALREADY_EXISTS.getJsonValue())));
	}

	@Test
	public void edit_WithExistingFrenchName_ShouldReturnBadRequest() throws Exception {
		City city = createValidCityForEdition();
		given(cityService.edit(any(City.class), any(AppUser.class))).willThrow(new AlreadyExistsException(Field.NAME_FR, City.class.getName()));

		ResultActions actions = mockMvc.perform(put(CITY_EDITION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(city)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.message", equalTo(AlreadyExistsException.messages.get(Field.NAME_FR.name() + City.class.getName()))));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.NAME_FR.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.ALREADY_EXISTS.getJsonValue())));
	}

	@Test
	public void edit_WithServerException_ShouldReturnInternalServerError() throws Exception {
		City city = createValidCityForEdition();
		given(cityService.edit(any(City.class), any(AppUser.class))).willThrow(new RuntimeException("Message"));

		ResultActions actions = mockMvc.perform(put(CITY_EDITION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(city)));
		actions.andExpect(status().isInternalServerError());
		actions.andExpect(jsonPath("$.message", equalTo("Message")));
	}

	@Test
	public void edit_WithMissingId_ShouldReturnBadRequest() throws Exception {
		City city = createValidCityForEdition();
		city.setId(null);
		testPutUrlAndExpectBadRequest(city, CITY_EDITION_URL, Field.ID, ErrorCode.EMPTY);
	}

	@Test
	public void edit_WithMissingEnglishName_ShouldReturnBadRequest() throws Exception {
		City city = createValidCityForEdition();
		city.setNameEn(null);
		testPutUrlAndExpectBadRequest(city, CITY_EDITION_URL, Field.NAME_EN, ErrorCode.EMPTY);
	}

	@Test
	public void edit_WithTooLongEnglishName_ShouldReturnBadRequest() throws Exception {
		City city = createValidCityForEdition();
		city.setNameEn(StringTools.random(NAME_MAX_LENGTH + 1));
		testPutUrlAndExpectBadRequest(city, CITY_EDITION_URL, Field.NAME_EN, ErrorCode.TOO_LONG);
	}

	@Test
	public void edit_WithTooShortEnglishName_ShouldReturnBadRequest() throws Exception {
		City city = createValidCityForEdition();
		city.setNameEn(StringTools.random(TEXT_MIN_LENGTH - 1));
		testPutUrlAndExpectBadRequest(city, CITY_EDITION_URL, Field.NAME_EN, ErrorCode.TOO_SHORT);
	}

	@Test
	public void edit_WithMissingFrenchName_ShouldReturnBadRequest() throws Exception {
		City city = createValidCityForEdition();
		city.setNameFr(null);
		testPutUrlAndExpectBadRequest(city, CITY_EDITION_URL, Field.NAME_FR, ErrorCode.EMPTY);
	}

	@Test
	public void edit_WithTooLongFrenchName_ShouldReturnBadRequest() throws Exception {
		City city = createValidCityForEdition();
		city.setNameFr(StringTools.random(NAME_MAX_LENGTH + 1));
		testPutUrlAndExpectBadRequest(city, CITY_EDITION_URL, Field.NAME_FR, ErrorCode.TOO_LONG);
	}

	@Test
	public void edit_WithTooShortFrenchName_ShouldReturnBadRequest() throws Exception {
		City city = createValidCityForEdition();
		city.setNameFr(StringTools.random(TEXT_MIN_LENGTH - 1));
		testPutUrlAndExpectBadRequest(city, CITY_EDITION_URL, Field.NAME_FR, ErrorCode.TOO_SHORT);
	}

	@Test
	public void edit_WithTooShortFrenchNamMissingCountry_ShouldReturnBadRequest() throws Exception {
		City city = createValidCityForEdition();
		city.setCountry(null);
		testPutUrlAndExpectBadRequest(city, CITY_EDITION_URL, Field.COUNTRY, ErrorCode.EMPTY);
	}

	private City createValidCityForCreation() {
		City city = new City();
		city.setNameEn(StringTools.random(NAME_MAX_LENGTH));
		city.setNameFr(StringTools.random(NAME_MAX_LENGTH));
		Country country = new Country();
		country.setId(1l);
		country.setNameEn(StringTools.random(NAME_MAX_LENGTH));
		country.setNameFr(StringTools.random(NAME_MAX_LENGTH));
		city.setCountry(country);
		return city;
	}

	private City createValidCityForEdition() {
		City city = createValidCityForCreation();
		city.setId(1l);
		return city;
	}
}