package com.web2h.betmates.restapp.rest.controller.competition;

import static com.web2h.betmates.restapp.model.entity.FieldLength.NAME_MAX_LENGTH;
import static com.web2h.betmates.restapp.rest.controller.UrlConstants.COMPETITION_ADD_OR_REMOVE_TEAMS_URL;
import static com.web2h.betmates.restapp.rest.controller.UrlConstants.COMPETITION_ADD_OR_REMOVE_VENUES_URL;
import static com.web2h.betmates.restapp.rest.controller.UrlConstants.COMPETITION_CREATION_URL;
import static com.web2h.betmates.restapp.rest.controller.UrlConstants.COMPETITION_EDITION_URL;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

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

import com.web2h.betmates.restapp.core.service.competition.CompetitionService;
import com.web2h.betmates.restapp.model.entity.FieldLength;
import com.web2h.betmates.restapp.model.entity.competition.Competition;
import com.web2h.betmates.restapp.model.entity.competition.CompetitionType;
import com.web2h.betmates.restapp.model.entity.user.AppUser;
import com.web2h.betmates.restapp.model.exception.AlreadyExistsException;
import com.web2h.betmates.restapp.model.exception.NotFoundException;
import com.web2h.betmates.restapp.model.validation.ErrorCode;
import com.web2h.betmates.restapp.model.validation.Field;
import com.web2h.betmates.restapp.rest.controller.ApplicationTest;
import com.web2h.betmates.restapp.rest.controller.CommonControllerTest;
import com.web2h.betmates.restapp.rest.controller.WebSecurityTest;
import com.web2h.tools.DateTools;
import com.web2h.tools.StringTools;

@RunWith(SpringRunner.class)
@WebMvcTest(CompetitionController.class)
@ContextConfiguration(classes = { ApplicationTest.class })
@Import(value = WebSecurityTest.class)
@WithMockUser
public class CompetitionControllerTest extends CommonControllerTest {

	@MockBean
	private CompetitionService competitionService;

	@SpyBean
	private CompetitionController competitionController;

	@Before
	public void before() throws Exception {
		given(competitionService.create(any(Competition.class), any(AppUser.class))).willReturn(null);
		doReturn(null).when(competitionController).getLoggedInUser();
	}

	@Test
	public void addOrRemoveTeams_WithValidData_ShouldReturnOk() throws Exception {
		Competition competition = createValidCompetitionForEdition();
		String jsonCompetition = asJsonString(competition);
		given(competitionService.addOrRemoveTeams(any(Competition.class), any(AppUser.class))).willReturn(competition);

		ResultActions actions = mockMvc.perform(post(COMPETITION_ADD_OR_REMOVE_TEAMS_URL).contentType(MediaType.APPLICATION_JSON).content(jsonCompetition));
		actions.andExpect(status().isOk());
		Assert.assertEquals(asJsonString(competition), actions.andReturn().getResponse().getContentAsString());
	}

	@Test
	public void addOrRemoveTeams_UnknownCompetition_ShouldReturnNotFound() throws Exception {
		Competition competition = createValidCompetitionForEdition();
		String jsonCompetition = asJsonString(competition);
		given(competitionService.addOrRemoveTeams(any(Competition.class), any(AppUser.class))).willThrow(new NotFoundException(Field.ID, Competition.class.getName()));

		ResultActions actions = mockMvc.perform(post(COMPETITION_ADD_OR_REMOVE_TEAMS_URL).contentType(MediaType.APPLICATION_JSON).content(jsonCompetition));
		actions.andExpect(status().isNotFound());
		actions.andExpect(jsonPath("$.message", equalTo(NotFoundException.messages.get(Field.ID.name() + Competition.class.getName()))));
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.ID.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.NOT_FOUND.getJsonValue())));
	}

	@Test
	public void addOrRemoveTeams_InternalError_ShouldReturnInternalError() throws Exception {
		Competition competition = createValidCompetitionForEdition();
		String jsonCompetition = asJsonString(competition);
		given(competitionService.addOrRemoveTeams(any(Competition.class), any(AppUser.class))).willThrow(new RuntimeException("message"));

		ResultActions actions = mockMvc.perform(post(COMPETITION_ADD_OR_REMOVE_TEAMS_URL).contentType(MediaType.APPLICATION_JSON).content(jsonCompetition));
		actions.andExpect(status().isInternalServerError());
		actions.andExpect(jsonPath("$.message", equalTo("message")));
		actions.andExpect(jsonPath("$.errors", hasSize(0)));
	}

	@Test
	public void addOrRemoveVenuess_WithValidData_ShouldReturnOk() throws Exception {
		Competition competition = createValidCompetitionForEdition();
		String jsonCompetition = asJsonString(competition);
		given(competitionService.addOrRemoveVenues(any(Competition.class), any(AppUser.class))).willReturn(competition);

		ResultActions actions = mockMvc.perform(post(COMPETITION_ADD_OR_REMOVE_VENUES_URL).contentType(MediaType.APPLICATION_JSON).content(jsonCompetition));
		actions.andExpect(status().isOk());
		Assert.assertEquals(asJsonString(competition), actions.andReturn().getResponse().getContentAsString());
	}

	@Test
	public void addOrRemoveVenues_UnknownCompetition_ShouldReturnNotFound() throws Exception {
		Competition competition = createValidCompetitionForEdition();
		String jsonCompetition = asJsonString(competition);
		given(competitionService.addOrRemoveVenues(any(Competition.class), any(AppUser.class))).willThrow(new NotFoundException(Field.ID, Competition.class.getName()));

		ResultActions actions = mockMvc.perform(post(COMPETITION_ADD_OR_REMOVE_VENUES_URL).contentType(MediaType.APPLICATION_JSON).content(jsonCompetition));
		actions.andExpect(status().isNotFound());
		actions.andExpect(jsonPath("$.message", equalTo(NotFoundException.messages.get(Field.ID.name() + Competition.class.getName()))));
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.ID.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.NOT_FOUND.getJsonValue())));
	}

	@Test
	public void addOrRemoveVenues_InternalError_ShouldReturnInternalError() throws Exception {
		Competition competition = createValidCompetitionForEdition();
		String jsonCompetition = asJsonString(competition);
		given(competitionService.addOrRemoveVenues(any(Competition.class), any(AppUser.class))).willThrow(new RuntimeException("message"));

		ResultActions actions = mockMvc.perform(post(COMPETITION_ADD_OR_REMOVE_VENUES_URL).contentType(MediaType.APPLICATION_JSON).content(jsonCompetition));
		actions.andExpect(status().isInternalServerError());
		actions.andExpect(jsonPath("$.message", equalTo("message")));
		actions.andExpect(jsonPath("$.errors", hasSize(0)));
	}

	@Test
	public void create_WithValidCompetition_ShouldReturnOk() throws Exception {
		Competition competition = createValidCompetitionForCreation();
		String jsonCompetition = asJsonString(competition);
		competition.setId(1l);
		given(competitionService.create(any(Competition.class), any(AppUser.class))).willReturn(competition);

		ResultActions actions = mockMvc.perform(post(COMPETITION_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(jsonCompetition));
		actions.andExpect(status().isOk());
		Assert.assertEquals(asJsonString(competition), actions.andReturn().getResponse().getContentAsString());
	}

	@Test
	public void create_WithValidCompetitionNotTrimmed_ShouldReturnOk() throws Exception {
		Competition competition = createValidCompetitionForCreation();
		String nameEn = StringTools.random(FieldLength.NAME_MAX_LENGTH);
		String nameFr = StringTools.random(FieldLength.NAME_MAX_LENGTH);
		competition.setNameEn(StringTools.padWithBlanks(nameEn));
		competition.setNameFr(StringTools.padWithBlanks(nameFr));
		String jsonCompetition = asJsonString(competition);
		competition.setId(1l);
		competition.setNameEn(nameEn);
		competition.setNameFr(nameFr);
		given(competitionService.create(any(Competition.class), any(AppUser.class))).willReturn(competition);

		ResultActions actions = mockMvc.perform(post(COMPETITION_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(jsonCompetition));
		actions.andExpect(status().isOk());
		Assert.assertEquals(asJsonString(competition), actions.andReturn().getResponse().getContentAsString());
	}

	@Test
	public void create_WithExistingEnglishName_ShouldReturnBadRequest() throws Exception {
		Competition competition = createValidCompetitionForCreation();
		given(competitionService.create(any(Competition.class), any(AppUser.class))).willThrow(new AlreadyExistsException(Field.NAME_EN, Competition.class.getName()));

		ResultActions actions = mockMvc.perform(post(COMPETITION_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(competition)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.message", equalTo(AlreadyExistsException.messages.get(Field.NAME_EN.name() + Competition.class.getName()))));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.NAME_EN.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.ALREADY_EXISTS.getJsonValue())));
	}

	@Test
	public void create_WithExistingFrenchName_ShouldReturnBadRequest() throws Exception {
		Competition competition = createValidCompetitionForCreation();
		given(competitionService.create(any(Competition.class), any(AppUser.class))).willThrow(new AlreadyExistsException(Field.NAME_FR, Competition.class.getName()));

		ResultActions actions = mockMvc.perform(post(COMPETITION_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(competition)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.message", equalTo(AlreadyExistsException.messages.get(Field.NAME_FR.name() + Competition.class.getName()))));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.NAME_FR.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.ALREADY_EXISTS.getJsonValue())));
	}

	@Test
	public void create_WithServerException_ShouldReturnInternalServerError() throws Exception {
		Competition competition = createValidCompetitionForCreation();
		given(competitionService.create(any(Competition.class), any(AppUser.class))).willThrow(new RuntimeException("Message"));

		ResultActions actions = mockMvc.perform(post(COMPETITION_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(competition)));
		actions.andExpect(status().isInternalServerError());
		actions.andExpect(jsonPath("$.message", equalTo("Message")));
	}

	@Test
	public void create_WithProvidedId_ShouldReturnBadRequest() throws Exception {
		Competition competition = createValidCompetitionForCreation();
		competition.setId(1l);
		testPostUrlAndExpectBadRequest(competition, COMPETITION_CREATION_URL, Field.ID, ErrorCode.NOT_EMPTY);
	}

	@Test
	public void create_WithMissingEnglishName_ShouldReturnBadRequest() throws Exception {
		Competition competition = createValidCompetitionForCreation();
		competition.setNameEn(null);
		testPostUrlAndExpectBadRequest(competition, COMPETITION_CREATION_URL, Field.NAME_EN, ErrorCode.EMPTY);
	}

	@Test
	public void create_WithTooLongEnglishName_ShouldReturnBadRequest() throws Exception {
		Competition competition = createValidCompetitionForCreation();
		competition.setNameEn(StringTools.random(FieldLength.NAME_MAX_LENGTH + 1));
		testPostUrlAndExpectBadRequest(competition, COMPETITION_CREATION_URL, Field.NAME_EN, ErrorCode.TOO_LONG);
	}

	@Test
	public void create_WithTooShortEnglishName_ShouldReturnBadRequest() throws Exception {
		Competition competition = createValidCompetitionForCreation();
		competition.setNameEn(StringTools.random(FieldLength.TEXT_MIN_LENGTH - 1));
		testPostUrlAndExpectBadRequest(competition, COMPETITION_CREATION_URL, Field.NAME_EN, ErrorCode.TOO_SHORT);
	}

	@Test
	public void create_WithMissingFrenchName_ShouldReturnBadRequest() throws Exception {
		Competition competition = createValidCompetitionForCreation();
		competition.setNameFr(null);
		testPostUrlAndExpectBadRequest(competition, COMPETITION_CREATION_URL, Field.NAME_FR, ErrorCode.EMPTY);
	}

	@Test
	public void create_WithTooLongFrenchName_ShouldReturnBadRequest() throws Exception {
		Competition competition = createValidCompetitionForCreation();
		competition.setNameFr(StringTools.random(FieldLength.NAME_MAX_LENGTH + 1));
		testPostUrlAndExpectBadRequest(competition, COMPETITION_CREATION_URL, Field.NAME_FR, ErrorCode.TOO_LONG);
	}

	@Test
	public void create_WithTooShortFrenchName_ShouldReturnBadRequest() throws Exception {
		Competition competition = createValidCompetitionForCreation();
		competition.setNameFr(StringTools.random(FieldLength.TEXT_MIN_LENGTH - 1));
		testPostUrlAndExpectBadRequest(competition, COMPETITION_CREATION_URL, Field.NAME_FR, ErrorCode.TOO_SHORT);
	}

	@Test
	public void create_WithMissingType_ShouldReturnBadRequest() throws Exception {
		Competition competition = createValidCompetitionForCreation();
		competition.setType(null);
		testPostUrlAndExpectBadRequest(competition, COMPETITION_CREATION_URL, Field.TYPE, ErrorCode.EMPTY);
	}

	@Test
	public void create_WithMissingStartDate_ShouldReturnBadRequest() throws Exception {
		Competition competition = createValidCompetitionForCreation();
		competition.setStartDate(null);
		testPostUrlAndExpectBadRequest(competition, COMPETITION_CREATION_URL, Field.START_DATE, ErrorCode.EMPTY);
	}

	@Test
	public void create_WithStartDateBeforeToday_ShouldReturnBadRequest() throws Exception {
		Competition competition = createValidCompetitionForCreation();
		competition.setStartDate(DateTools.removeDays(new Date(), 1));
		testPostUrlAndExpectBadRequest(competition, COMPETITION_CREATION_URL, Field.START_DATE, ErrorCode.TOO_OLD);
	}

	@Test
	public void edit_WithValidCity_ShouldReturnOk() throws Exception {
		Competition competition = createValidCompetitionForEdition();
		String jsonCompetition = asJsonString(competition);
		given(competitionService.edit(any(Competition.class), any(AppUser.class))).willReturn(competition);

		ResultActions actions = mockMvc.perform(put(COMPETITION_EDITION_URL).contentType(MediaType.APPLICATION_JSON).content(jsonCompetition));
		actions.andExpect(status().isOk());
		Assert.assertEquals(asJsonString(competition), actions.andReturn().getResponse().getContentAsString());
	}

	@Test
	public void edit_UnknownCity_ShouldReturnNotFound() throws Exception {
		Competition competition = createValidCompetitionForEdition();
		String jsonCompetition = asJsonString(competition);
		given(competitionService.edit(any(Competition.class), any(AppUser.class))).willThrow(new NotFoundException(Field.ID, Competition.class.getName()));

		ResultActions actions = mockMvc.perform(put(COMPETITION_EDITION_URL).contentType(MediaType.APPLICATION_JSON).content(jsonCompetition));
		actions.andExpect(status().isNotFound());
		actions.andExpect(jsonPath("$.message", equalTo(NotFoundException.messages.get(Field.ID.name() + Competition.class.getName()))));
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.ID.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.NOT_FOUND.getJsonValue())));
	}

	@Test
	public void edit_WithExistingEnglishName_ShouldReturnBadRequest() throws Exception {
		Competition competition = createValidCompetitionForEdition();
		given(competitionService.edit(any(Competition.class), any(AppUser.class))).willThrow(new AlreadyExistsException(Field.NAME_EN, Competition.class.getName()));

		ResultActions actions = mockMvc.perform(put(COMPETITION_EDITION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(competition)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.message", equalTo(AlreadyExistsException.messages.get(Field.NAME_EN.name() + Competition.class.getName()))));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.NAME_EN.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.ALREADY_EXISTS.getJsonValue())));
	}

	@Test
	public void edit_WithExistingFrenchName_ShouldReturnBadRequest() throws Exception {
		Competition competition = createValidCompetitionForEdition();
		given(competitionService.edit(any(Competition.class), any(AppUser.class))).willThrow(new AlreadyExistsException(Field.NAME_FR, Competition.class.getName()));

		ResultActions actions = mockMvc.perform(put(COMPETITION_EDITION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(competition)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.message", equalTo(AlreadyExistsException.messages.get(Field.NAME_FR.name() + Competition.class.getName()))));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.NAME_FR.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.ALREADY_EXISTS.getJsonValue())));
	}

	@Test
	public void edit_WithServerException_ShouldReturnInternalServerError() throws Exception {
		Competition competition = createValidCompetitionForEdition();
		given(competitionService.edit(any(Competition.class), any(AppUser.class))).willThrow(new RuntimeException("Message"));

		ResultActions actions = mockMvc.perform(put(COMPETITION_EDITION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(competition)));
		actions.andExpect(status().isInternalServerError());
		actions.andExpect(jsonPath("$.message", equalTo("Message")));
	}

	@Test
	public void edit_WithMissingId_ShouldReturnBadRequest() throws Exception {
		Competition competition = createValidCompetitionForEdition();
		competition.setId(null);
		testPutUrlAndExpectBadRequest(competition, COMPETITION_EDITION_URL, Field.ID, ErrorCode.EMPTY);
	}

	@Test
	public void edit_WithMissingEnglishName_ShouldReturnBadRequest() throws Exception {
		Competition competition = createValidCompetitionForEdition();
		competition.setNameEn(null);
		testPutUrlAndExpectBadRequest(competition, COMPETITION_EDITION_URL, Field.NAME_EN, ErrorCode.EMPTY);
	}

	@Test
	public void edit_WithTooLongEnglishName_ShouldReturnBadRequest() throws Exception {
		Competition competition = createValidCompetitionForEdition();
		competition.setNameEn(StringTools.random(FieldLength.NAME_MAX_LENGTH + 1));
		testPutUrlAndExpectBadRequest(competition, COMPETITION_EDITION_URL, Field.NAME_EN, ErrorCode.TOO_LONG);
	}

	@Test
	public void edit_WithTooShortEnglishName_ShouldReturnBadRequest() throws Exception {
		Competition competition = createValidCompetitionForEdition();
		competition.setNameEn(StringTools.random(FieldLength.TEXT_MIN_LENGTH - 1));
		testPutUrlAndExpectBadRequest(competition, COMPETITION_EDITION_URL, Field.NAME_EN, ErrorCode.TOO_SHORT);
	}

	@Test
	public void edit_WithMissingFrenchName_ShouldReturnBadRequest() throws Exception {
		Competition competition = createValidCompetitionForEdition();
		competition.setNameFr(null);
		testPutUrlAndExpectBadRequest(competition, COMPETITION_EDITION_URL, Field.NAME_FR, ErrorCode.EMPTY);
	}

	@Test
	public void edit_WithTooLongFrenchName_ShouldReturnBadRequest() throws Exception {
		Competition competition = createValidCompetitionForEdition();
		competition.setNameFr(StringTools.random(FieldLength.NAME_MAX_LENGTH + 1));
		testPutUrlAndExpectBadRequest(competition, COMPETITION_EDITION_URL, Field.NAME_FR, ErrorCode.TOO_LONG);
	}

	@Test
	public void edit_WithTooShortFrenchName_ShouldReturnBadRequest() throws Exception {
		Competition competition = createValidCompetitionForEdition();
		competition.setNameFr(StringTools.random(FieldLength.TEXT_MIN_LENGTH - 1));
		testPutUrlAndExpectBadRequest(competition, COMPETITION_EDITION_URL, Field.NAME_FR, ErrorCode.TOO_SHORT);
	}

	@Test
	public void edit_WithMissingType_ShouldReturnBadRequest() throws Exception {
		Competition competition = createValidCompetitionForEdition();
		competition.setType(null);
		testPutUrlAndExpectBadRequest(competition, COMPETITION_EDITION_URL, Field.TYPE, ErrorCode.EMPTY);
	}

	@Test
	public void edit_WithMissingStartDate_ShouldReturnBadRequest() throws Exception {
		Competition competition = createValidCompetitionForEdition();
		competition.setStartDate(null);
		testPutUrlAndExpectBadRequest(competition, COMPETITION_EDITION_URL, Field.START_DATE, ErrorCode.EMPTY);
	}

	@Test
	public void edit_WithStartDateBeforeToday_ShouldReturnBadRequest() throws Exception {
		Competition competition = createValidCompetitionForEdition();
		competition.setStartDate(DateTools.removeDays(new Date(), 1));
		testPutUrlAndExpectBadRequest(competition, COMPETITION_EDITION_URL, Field.START_DATE, ErrorCode.TOO_OLD);
	}

	private Competition createValidCompetitionForCreation() {
		Competition competition = new Competition();
		competition.setNameEn(StringTools.random(NAME_MAX_LENGTH));
		competition.setNameFr(StringTools.random(NAME_MAX_LENGTH));
		competition.setType(CompetitionType.FIFA_WORLD_CUP);
		competition.setStartDate(new Date());
		return competition;
	}

	private Competition createValidCompetitionForEdition() {
		Competition competition = createValidCompetitionForCreation();
		competition.setId(1l);
		return competition;
	}
}