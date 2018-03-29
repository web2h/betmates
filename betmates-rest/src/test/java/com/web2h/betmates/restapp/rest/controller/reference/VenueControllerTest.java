package com.web2h.betmates.restapp.rest.controller.reference;

import static com.web2h.betmates.restapp.model.entity.FieldLength.NAME_MAX_LENGTH;
import static com.web2h.betmates.restapp.model.entity.FieldLength.TEXT_MIN_LENGTH;
import static com.web2h.betmates.restapp.rest.controller.UrlConstants.VENUE_CREATION_URL;
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

import com.web2h.betmates.restapp.core.service.reference.VenueService;
import com.web2h.betmates.restapp.core.service.user.UserService;
import com.web2h.betmates.restapp.model.entity.FieldLength;
import com.web2h.betmates.restapp.model.entity.reference.City;
import com.web2h.betmates.restapp.model.entity.reference.Country;
import com.web2h.betmates.restapp.model.entity.reference.Venue;
import com.web2h.betmates.restapp.model.exception.AlreadyExistsException;
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

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private VenueService venueService;

	@MockBean
	private UserService userService;

	@MockBean
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@SpyBean
	private VenueController VenueController;

	@Before
	public void before() throws Exception {
		given(venueService.create(anyObject(), anyObject())).willReturn(null);
		doReturn(null).when(VenueController).getLoggedInUser();
	}

	@Test
	public void create_WithValidCity_ShouldReturnOk() throws Exception {
		Venue venue = createValidVenue();
		String jsonVenue = asJsonString(venue);
		venue.setId(1l);
		given(venueService.create(anyObject(), anyObject())).willReturn(venue);

		ResultActions actions = mockMvc.perform(post(VENUE_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(jsonVenue));
		actions.andExpect(status().isOk());
		Assert.assertEquals(asJsonString(venue), actions.andReturn().getResponse().getContentAsString());
	}

	@Test
	public void create_WithValidCityNotTrimmed_ShouldReturnOk() throws Exception {
		Venue venue = createValidVenue();
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
		Venue venue = createValidVenue();
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
		Venue venue = createValidVenue();
		given(venueService.create(anyObject(), anyObject())).willThrow(new AlreadyExistsException(Field.NAME_FR, Venue.class.getName()));

		ResultActions actions = mockMvc.perform(post(VENUE_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(venue)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.message", equalTo(AlreadyExistsException.messages.get(Field.NAME_FR.name() + Venue.class.getName()))));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.NAME_FR.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.ALREADY_EXISTS.getJsonValue())));
	}

	@Test
	public void create_WithServerException_ShouldReturnInternalServerError() throws Exception {
		Venue venue = createValidVenue();
		given(venueService.create(anyObject(), anyObject())).willThrow(new RuntimeException("Message"));

		ResultActions actions = mockMvc.perform(post(VENUE_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(venue)));
		actions.andExpect(status().isInternalServerError());
		actions.andExpect(jsonPath("$.message", equalTo("Message")));
	}

	@Test
	public void create_WithProvidedId_ShouldReturnBadRequest() throws Exception {
		Venue venue = createValidVenue();
		venue.setId(1l);

		ResultActions actions = mockMvc.perform(post(VENUE_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(venue)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.ID.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.NOT_EMPTY.getJsonValue())));
	}

	@Test
	public void create_WithMissingEnglishName_ShouldReturnBadRequest() throws Exception {
		Venue venue = createValidVenue();
		venue.setNameEn(null);

		ResultActions actions = mockMvc.perform(post(VENUE_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(venue)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.NAME_EN.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.EMPTY.getJsonValue())));
	}

	@Test
	public void create_WithTooLongEnglishName_ShouldReturnBadRequest() throws Exception {
		Venue venue = createValidVenue();
		venue.setNameEn(StringTools.random(NAME_MAX_LENGTH + 1));

		ResultActions actions = mockMvc.perform(post(VENUE_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(venue)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.NAME_EN.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.TOO_LONG.getJsonValue())));
	}

	@Test
	public void create_WithTooShortEnglishName_ShouldReturnBadRequest() throws Exception {
		Venue venue = createValidVenue();
		venue.setNameEn(StringTools.random(TEXT_MIN_LENGTH - 1));

		ResultActions actions = mockMvc.perform(post(VENUE_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(venue)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.NAME_EN.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.TOO_SHORT.getJsonValue())));
	}

	@Test
	public void create_WithMissingFrenchName_ShouldReturnBadRequest() throws Exception {
		Venue venue = createValidVenue();
		venue.setNameFr(null);

		ResultActions actions = mockMvc.perform(post(VENUE_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(venue)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.NAME_FR.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.EMPTY.getJsonValue())));
	}

	@Test
	public void create_WithTooLongFrenchName_ShouldReturnBadRequest() throws Exception {
		Venue venue = createValidVenue();
		venue.setNameFr(StringTools.random(NAME_MAX_LENGTH + 1));

		ResultActions actions = mockMvc.perform(post(VENUE_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(venue)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.NAME_FR.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.TOO_LONG.getJsonValue())));
	}

	@Test
	public void create_WithTooShortFrenchName_ShouldReturnBadRequest() throws Exception {
		Venue venue = createValidVenue();
		venue.setNameFr(StringTools.random(TEXT_MIN_LENGTH - 1));

		ResultActions actions = mockMvc.perform(post(VENUE_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(venue)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.NAME_FR.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.TOO_SHORT.getJsonValue())));
	}

	@Test
	public void create_WithMissingCity_ShouldReturnBadRequest() throws Exception {
		Venue venue = createValidVenue();
		venue.setCity(null);

		ResultActions actions = mockMvc.perform(post(VENUE_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(venue)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.CITY.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.EMPTY.getJsonValue())));
	}

	private Venue createValidVenue() {
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
}