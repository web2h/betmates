package com.web2h.betmates.restapp.rest.controller.reference;

import static com.web2h.betmates.restapp.model.entity.FieldLength.NAME_MAX_LENGTH;
import static com.web2h.betmates.restapp.model.entity.FieldLength.TEXT_MIN_LENGTH;
import static com.web2h.betmates.restapp.rest.controller.UrlConstants.VENUE_CREATION_URL;
import static com.web2h.betmates.restapp.rest.controller.UrlConstants.VENUE_EDITION_URL;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyObject;
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

import com.web2h.betmates.restapp.core.service.reference.VenueService;
import com.web2h.betmates.restapp.model.entity.FieldLength;
import com.web2h.betmates.restapp.model.entity.reference.City;
import com.web2h.betmates.restapp.model.entity.reference.Country;
import com.web2h.betmates.restapp.model.entity.reference.Venue;
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
@WebMvcTest(VenueController.class)
@ContextConfiguration(classes = { ApplicationTest.class })
@Import(value = WebSecurityTest.class)
@WithMockUser
public class VenueControllerTest extends CommonControllerTest {

	@MockBean
	private VenueService venueService;

	@SpyBean
	private VenueController VenueController;

	@Before
	public void before() throws Exception {
		given(venueService.create(anyObject(), anyObject())).willReturn(null);
		doReturn(null).when(VenueController).getLoggedInUser();
	}

	@Test
	public void create_WithValidCity_ShouldReturnOk() throws Exception {
		Venue venue = createValidVenueForCreation();
		String jsonVenue = asJsonString(venue);
		venue.setId(1l);
		given(venueService.create(anyObject(), anyObject())).willReturn(venue);

		ResultActions actions = mockMvc.perform(post(VENUE_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(jsonVenue));
		actions.andExpect(status().isOk());
		Assert.assertEquals(asJsonString(venue), actions.andReturn().getResponse().getContentAsString());
	}

	@Test
	public void create_WithValidCityNotTrimmed_ShouldReturnOk() throws Exception {
		Venue venue = createValidVenueForCreation();
		String nameEn = StringTools.random(FieldLength.NAME_MAX_LENGTH);
		String nameFr = StringTools.random(FieldLength.NAME_MAX_LENGTH);
		venue.setNameEn(StringTools.padWithBlanks(nameEn));
		venue.setNameFr(StringTools.padWithBlanks(nameFr));
		String jsonVenue = asJsonString(venue);
		venue.setId(1l);
		venue.setNameEn(nameEn);
		venue.setNameFr(nameFr);
		given(venueService.create(anyObject(), anyObject())).willReturn(venue);

		ResultActions actions = mockMvc.perform(post(VENUE_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(jsonVenue));
		actions.andExpect(status().isOk());
		Assert.assertEquals(asJsonString(venue), actions.andReturn().getResponse().getContentAsString());
	}

	@Test
	public void create_WithExistingEnglishName_ShouldReturnBadRequest() throws Exception {
		Venue venue = createValidVenueForCreation();
		given(venueService.create(anyObject(), anyObject())).willThrow(new AlreadyExistsException(Field.NAME_EN, Venue.class.getName()));

		ResultActions actions = mockMvc.perform(post(VENUE_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(venue)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.message", equalTo(AlreadyExistsException.messages.get(Field.NAME_EN.name() + Venue.class.getName()))));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.NAME_EN.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.ALREADY_EXISTS.getJsonValue())));
	}

	@Test
	public void create_WithExistingFrenchName_ShouldReturnBadRequest() throws Exception {
		Venue venue = createValidVenueForCreation();
		given(venueService.create(anyObject(), anyObject())).willThrow(new AlreadyExistsException(Field.NAME_FR, Venue.class.getName()));

		ResultActions actions = mockMvc.perform(post(VENUE_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(venue)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.message", equalTo(AlreadyExistsException.messages.get(Field.NAME_FR.name() + Venue.class.getName()))));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.NAME_FR.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.ALREADY_EXISTS.getJsonValue())));
	}

	@Test
	public void create_WithUnknownCity_ShouldReturnBadRequest() throws Exception {
		Venue venue = createValidVenueForCreation();
		given(venueService.create(anyObject(), anyObject())).willThrow(InvalidDataException.createWithFieldAndErrorCode(Field.CITY, ErrorCode.NOT_FOUND));

		ResultActions actions = mockMvc.perform(post(VENUE_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(venue)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.message", equalTo(InvalidDataException.DEFAULT_MESSAGE)));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.CITY.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.NOT_FOUND.getJsonValue())));
	}

	@Test
	public void create_WithServerException_ShouldReturnInternalServerError() throws Exception {
		Venue venue = createValidVenueForCreation();
		given(venueService.create(anyObject(), anyObject())).willThrow(new RuntimeException("Message"));

		ResultActions actions = mockMvc.perform(post(VENUE_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(venue)));
		actions.andExpect(status().isInternalServerError());
		actions.andExpect(jsonPath("$.message", equalTo("Message")));
	}

	@Test
	public void create_WithProvidedId_ShouldReturnBadRequest() throws Exception {
		Venue venue = createValidVenueForCreation();
		venue.setId(1l);
		testPostUrlAndExpectBadRequest(venue, VENUE_CREATION_URL, Field.ID, ErrorCode.NOT_EMPTY);
	}

	@Test
	public void create_WithMissingEnglishName_ShouldReturnBadRequest() throws Exception {
		Venue venue = createValidVenueForCreation();
		venue.setNameEn(null);
		testPostUrlAndExpectBadRequest(venue, VENUE_CREATION_URL, Field.NAME_EN, ErrorCode.EMPTY);
	}

	@Test
	public void create_WithTooLongEnglishName_ShouldReturnBadRequest() throws Exception {
		Venue venue = createValidVenueForCreation();
		venue.setNameEn(StringTools.random(NAME_MAX_LENGTH + 1));
		testPostUrlAndExpectBadRequest(venue, VENUE_CREATION_URL, Field.NAME_EN, ErrorCode.TOO_LONG);
	}

	@Test
	public void create_WithTooShortEnglishName_ShouldReturnBadRequest() throws Exception {
		Venue venue = createValidVenueForCreation();
		venue.setNameEn(StringTools.random(TEXT_MIN_LENGTH - 1));
		testPostUrlAndExpectBadRequest(venue, VENUE_CREATION_URL, Field.NAME_EN, ErrorCode.TOO_SHORT);
	}

	@Test
	public void create_WithMissingFrenchName_ShouldReturnBadRequest() throws Exception {
		Venue venue = createValidVenueForCreation();
		venue.setNameFr(null);
		testPostUrlAndExpectBadRequest(venue, VENUE_CREATION_URL, Field.NAME_FR, ErrorCode.EMPTY);
	}

	@Test
	public void create_WithTooLongFrenchName_ShouldReturnBadRequest() throws Exception {
		Venue venue = createValidVenueForCreation();
		venue.setNameFr(StringTools.random(NAME_MAX_LENGTH + 1));
		testPostUrlAndExpectBadRequest(venue, VENUE_CREATION_URL, Field.NAME_FR, ErrorCode.TOO_LONG);
	}

	@Test
	public void create_WithTooShortFrenchName_ShouldReturnBadRequest() throws Exception {
		Venue venue = createValidVenueForCreation();
		venue.setNameFr(StringTools.random(TEXT_MIN_LENGTH - 1));
		testPostUrlAndExpectBadRequest(venue, VENUE_CREATION_URL, Field.NAME_FR, ErrorCode.TOO_SHORT);
	}

	@Test
	public void create_WithMissingCity_ShouldReturnBadRequest() throws Exception {
		Venue venue = createValidVenueForCreation();
		venue.setCity(null);
		testPostUrlAndExpectBadRequest(venue, VENUE_CREATION_URL, Field.CITY, ErrorCode.EMPTY);
	}

	@Test
	public void edit_WithValidVenue_ShouldReturnOk() throws Exception {
		Venue venue = createValidVenueForEdition();
		String jsonVenue = asJsonString(venue);
		given(venueService.edit(anyObject(), anyObject())).willReturn(venue);

		ResultActions actions = mockMvc.perform(put(VENUE_EDITION_URL).contentType(MediaType.APPLICATION_JSON).content(jsonVenue));
		actions.andExpect(status().isOk());
		Assert.assertEquals(asJsonString(venue), actions.andReturn().getResponse().getContentAsString());
	}

	@Test
	public void edit_UnknownVenue_ShouldReturnNotFound() throws Exception {
		Venue venue = createValidVenueForEdition();
		String jsonVenue = asJsonString(venue);
		given(venueService.edit(anyObject(), anyObject())).willThrow(new NotFoundException(Field.ID, Venue.class.getName()));

		ResultActions actions = mockMvc.perform(put(VENUE_EDITION_URL).contentType(MediaType.APPLICATION_JSON).content(jsonVenue));
		actions.andExpect(status().isNotFound());
		actions.andExpect(jsonPath("$.message", equalTo(NotFoundException.messages.get(Field.ID.name() + Venue.class.getName()))));
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.ID.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.NOT_FOUND.getJsonValue())));
	}

	@Test
	public void edit_WithUnknownCity_ShouldReturnBadRequest() throws Exception {
		Venue venue = createValidVenueForEdition();
		given(venueService.edit(anyObject(), anyObject())).willThrow(InvalidDataException.createWithFieldAndErrorCode(Field.CITY, ErrorCode.NOT_FOUND));

		ResultActions actions = mockMvc.perform(put(VENUE_EDITION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(venue)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.message", equalTo(InvalidDataException.DEFAULT_MESSAGE)));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.CITY.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.NOT_FOUND.getJsonValue())));
	}

	@Test
	public void edit_WithExistingEnglishName_ShouldReturnBadRequest() throws Exception {
		Venue venue = createValidVenueForEdition();
		given(venueService.edit(anyObject(), anyObject())).willThrow(new AlreadyExistsException(Field.NAME_EN, Venue.class.getName()));

		ResultActions actions = mockMvc.perform(put(VENUE_EDITION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(venue)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.message", equalTo(AlreadyExistsException.messages.get(Field.NAME_EN.name() + Venue.class.getName()))));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.NAME_EN.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.ALREADY_EXISTS.getJsonValue())));
	}

	@Test
	public void edit_WithExistingFrenchName_ShouldReturnBadRequest() throws Exception {
		Venue venue = createValidVenueForEdition();
		given(venueService.edit(anyObject(), anyObject())).willThrow(new AlreadyExistsException(Field.NAME_FR, Venue.class.getName()));

		ResultActions actions = mockMvc.perform(put(VENUE_EDITION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(venue)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.message", equalTo(AlreadyExistsException.messages.get(Field.NAME_FR.name() + Venue.class.getName()))));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.NAME_FR.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.ALREADY_EXISTS.getJsonValue())));
	}

	@Test
	public void edit_WithServerException_ShouldReturnInternalServerError() throws Exception {
		Venue venue = createValidVenueForEdition();
		given(venueService.edit(anyObject(), anyObject())).willThrow(new RuntimeException("Message"));

		ResultActions actions = mockMvc.perform(put(VENUE_EDITION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(venue)));
		actions.andExpect(status().isInternalServerError());
		actions.andExpect(jsonPath("$.message", equalTo("Message")));
	}

	@Test
	public void edit_WithMissingId_ShouldReturnBadRequest() throws Exception {
		Venue venue = createValidVenueForEdition();
		venue.setId(null);
		testPutUrlAndExpectBadRequest(venue, VENUE_EDITION_URL, Field.ID, ErrorCode.EMPTY);
	}

	@Test
	public void edit_WithMissingEnglishName_ShouldReturnBadRequest() throws Exception {
		Venue venue = createValidVenueForEdition();
		venue.setNameEn(null);
		testPutUrlAndExpectBadRequest(venue, VENUE_EDITION_URL, Field.NAME_EN, ErrorCode.EMPTY);
	}

	@Test
	public void edit_WithTooLongEnglishName_ShouldReturnBadRequest() throws Exception {
		Venue venue = createValidVenueForEdition();
		venue.setNameEn(StringTools.random(NAME_MAX_LENGTH + 1));
		testPutUrlAndExpectBadRequest(venue, VENUE_EDITION_URL, Field.NAME_EN, ErrorCode.TOO_LONG);
	}

	@Test
	public void edit_WithTooShortEnglishName_ShouldReturnBadRequest() throws Exception {
		Venue venue = createValidVenueForEdition();
		venue.setNameEn(StringTools.random(TEXT_MIN_LENGTH - 1));
		testPutUrlAndExpectBadRequest(venue, VENUE_EDITION_URL, Field.NAME_EN, ErrorCode.TOO_SHORT);
	}

	@Test
	public void edit_WithMissingFrenchName_ShouldReturnBadRequest() throws Exception {
		Venue venue = createValidVenueForEdition();
		venue.setNameFr(null);
		testPutUrlAndExpectBadRequest(venue, VENUE_EDITION_URL, Field.NAME_FR, ErrorCode.EMPTY);
	}

	@Test
	public void edit_WithTooLongFrenchName_ShouldReturnBadRequest() throws Exception {
		Venue venue = createValidVenueForEdition();
		venue.setNameFr(StringTools.random(NAME_MAX_LENGTH + 1));
		testPutUrlAndExpectBadRequest(venue, VENUE_EDITION_URL, Field.NAME_FR, ErrorCode.TOO_LONG);
	}

	@Test
	public void edit_WithTooShortFrenchName_ShouldReturnBadRequest() throws Exception {
		Venue venue = createValidVenueForEdition();
		venue.setNameFr(StringTools.random(TEXT_MIN_LENGTH - 1));
		testPutUrlAndExpectBadRequest(venue, VENUE_EDITION_URL, Field.NAME_FR, ErrorCode.TOO_SHORT);
	}

	@Test
	public void edit_WithMissingCity_ShouldReturnBadRequest() throws Exception {
		Venue venue = createValidVenueForEdition();
		venue.setCity(null);
		testPutUrlAndExpectBadRequest(venue, VENUE_EDITION_URL, Field.CITY, ErrorCode.EMPTY);
	}

	private Venue createValidVenueForCreation() {
		Venue venue = new Venue();
		venue.setNameEn(StringTools.random(NAME_MAX_LENGTH));
		venue.setNameFr(StringTools.random(NAME_MAX_LENGTH));
		City city = new City();
		city.setId(1l);
		city.setNameEn(StringTools.random(NAME_MAX_LENGTH));
		city.setNameFr(StringTools.random(NAME_MAX_LENGTH));
		Country country = new Country();
		country.setId(1l);
		country.setNameEn(StringTools.random(NAME_MAX_LENGTH));
		country.setNameFr(StringTools.random(NAME_MAX_LENGTH));
		city.setCountry(country);
		venue.setCity(city);
		return venue;
	}

	private Venue createValidVenueForEdition() {
		Venue venue = createValidVenueForCreation();
		venue.setId(1l);
		return venue;
	}
}