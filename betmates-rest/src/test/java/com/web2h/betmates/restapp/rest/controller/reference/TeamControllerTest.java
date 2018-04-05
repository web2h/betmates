package com.web2h.betmates.restapp.rest.controller.reference;

import static com.web2h.betmates.restapp.model.entity.FieldLength.NAME_MAX_LENGTH;
import static com.web2h.betmates.restapp.model.entity.FieldLength.SHORT_NAME_MAX_LENGTH;
import static com.web2h.betmates.restapp.model.entity.FieldLength.TEXT_MIN_LENGTH;
import static com.web2h.betmates.restapp.rest.controller.UrlConstants.TEAM_CREATION_URL;
import static com.web2h.betmates.restapp.rest.controller.UrlConstants.TEAM_EDITION_URL;
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

import com.web2h.betmates.restapp.core.service.reference.TeamService;
import com.web2h.betmates.restapp.model.entity.reference.Sport;
import com.web2h.betmates.restapp.model.entity.reference.Team;
import com.web2h.betmates.restapp.model.exception.AlreadyExistsException;
import com.web2h.betmates.restapp.model.exception.NotFoundException;
import com.web2h.betmates.restapp.model.validation.ErrorCode;
import com.web2h.betmates.restapp.model.validation.Field;
import com.web2h.betmates.restapp.rest.controller.ApplicationTest;
import com.web2h.betmates.restapp.rest.controller.CommonControllerTest;
import com.web2h.betmates.restapp.rest.controller.WebSecurityTest;
import com.web2h.tools.StringTools;

@RunWith(SpringRunner.class)
@WebMvcTest(TeamController.class)
@ContextConfiguration(classes = { ApplicationTest.class })
@Import(value = WebSecurityTest.class)
@WithMockUser
public class TeamControllerTest extends CommonControllerTest {

	@MockBean
	private TeamService teamService;

	@SpyBean
	private TeamController teamController;

	@Before
	public void before() throws Exception {
		given(teamService.create(anyObject(), anyObject())).willReturn(null);
		doReturn(null).when(teamController).getLoggedInUser();
	}

	@Test
	public void create_WithValidTeam_ShouldReturnOk() throws Exception {
		Team team = createValidTeamForCreation();
		String jsonTeam = asJsonString(team);
		team.setId(1l);
		given(teamService.create(anyObject(), anyObject())).willReturn(team);

		ResultActions actions = mockMvc.perform(post(TEAM_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(jsonTeam));
		actions.andExpect(status().isOk());
		Assert.assertEquals(asJsonString(team), actions.andReturn().getResponse().getContentAsString());
	}

	@Test
	public void create_WithValidTeamNotTrimmed_ShouldReturnOk() throws Exception {
		Team team = createValidTeamForCreation();
		String nameEn = StringTools.random(NAME_MAX_LENGTH);
		String nameFr = StringTools.random(NAME_MAX_LENGTH);
		String shortNameEn = StringTools.random(SHORT_NAME_MAX_LENGTH);
		String shortNameFr = StringTools.random(SHORT_NAME_MAX_LENGTH);
		team.setNameEn(StringTools.padWithBlanks(nameEn));
		team.setNameFr(StringTools.padWithBlanks(nameFr));
		team.setShortNameEn(StringTools.padWithBlanks(shortNameEn));
		team.setShortNameFr(StringTools.padWithBlanks(shortNameFr));
		String jsonTeam = asJsonString(team);

		team.setId(1l);
		team.setNameEn(nameEn);
		team.setNameFr(nameFr);
		team.setShortNameEn(shortNameEn);
		team.setShortNameFr(shortNameFr);
		given(teamService.create(anyObject(), anyObject())).willReturn(team);

		ResultActions actions = mockMvc.perform(post(TEAM_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(jsonTeam));
		actions.andExpect(status().isOk());
		Assert.assertEquals(asJsonString(team), actions.andReturn().getResponse().getContentAsString());
	}

	@Test
	public void create_WithExistingEnglishName_ShouldReturnBadRequest() throws Exception {
		Team team = createValidTeamForCreation();
		given(teamService.create(anyObject(), anyObject())).willThrow(new AlreadyExistsException(Field.NAME_EN, Team.class.getName()));

		ResultActions actions = mockMvc.perform(post(TEAM_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(team)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.message", equalTo(AlreadyExistsException.messages.get(Field.NAME_EN.name() + Team.class.getName()))));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.NAME_EN.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.ALREADY_EXISTS.getJsonValue())));
	}

	@Test
	public void create_WithExistingFrenchName_ShouldReturnBadRequest() throws Exception {
		Team team = createValidTeamForCreation();
		given(teamService.create(anyObject(), anyObject())).willThrow(new AlreadyExistsException(Field.NAME_FR, Team.class.getName()));

		ResultActions actions = mockMvc.perform(post(TEAM_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(team)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.message", equalTo(AlreadyExistsException.messages.get(Field.NAME_FR.name() + Team.class.getName()))));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.NAME_FR.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.ALREADY_EXISTS.getJsonValue())));
	}

	@Test
	public void create_WithServerException_ShouldReturnInternalServerError() throws Exception {
		Team team = createValidTeamForCreation();
		given(teamService.create(anyObject(), anyObject())).willThrow(new RuntimeException("Message"));

		ResultActions actions = mockMvc.perform(post(TEAM_CREATION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(team)));
		actions.andExpect(status().isInternalServerError());
		actions.andExpect(jsonPath("$.message", equalTo("Message")));
	}

	@Test
	public void create_WithProvidedId_ShouldReturnBadRequest() throws Exception {
		Team team = createValidTeamForCreation();
		team.setId(1l);
		testPostUrlAndExpectBadRequest(team, TEAM_CREATION_URL, Field.ID, ErrorCode.NOT_EMPTY);
	}

	@Test
	public void create_WithMissingEnglishName_ShouldReturnBadRequest() throws Exception {
		Team team = createValidTeamForCreation();
		team.setNameEn(null);
		testPostUrlAndExpectBadRequest(team, TEAM_CREATION_URL, Field.NAME_EN, ErrorCode.EMPTY);
	}

	@Test
	public void create_WithTooLongEnglishName_ShouldReturnBadRequest() throws Exception {
		Team team = createValidTeamForCreation();
		team.setNameEn(StringTools.random(NAME_MAX_LENGTH + 1));
		testPostUrlAndExpectBadRequest(team, TEAM_CREATION_URL, Field.NAME_EN, ErrorCode.TOO_LONG);
	}

	@Test
	public void create_WithTooShortEnglishName_ShouldReturnBadRequest() throws Exception {
		Team team = createValidTeamForCreation();
		team.setNameEn(StringTools.random(TEXT_MIN_LENGTH - 1));
		testPostUrlAndExpectBadRequest(team, TEAM_CREATION_URL, Field.NAME_EN, ErrorCode.TOO_SHORT);
	}

	@Test
	public void create_WithMissingFrenchName_ShouldReturnBadRequest() throws Exception {
		Team team = createValidTeamForCreation();
		team.setNameFr(null);
		testPostUrlAndExpectBadRequest(team, TEAM_CREATION_URL, Field.NAME_FR, ErrorCode.EMPTY);
	}

	@Test
	public void create_WithTooLongFrenchName_ShouldReturnBadRequest() throws Exception {
		Team team = createValidTeamForCreation();
		team.setNameFr(StringTools.random(NAME_MAX_LENGTH + 1));
		testPostUrlAndExpectBadRequest(team, TEAM_CREATION_URL, Field.NAME_FR, ErrorCode.TOO_LONG);
	}

	@Test
	public void create_WithTooShortFrenchName_ShouldReturnBadRequest() throws Exception {
		Team team = createValidTeamForCreation();
		team.setNameFr(StringTools.random(TEXT_MIN_LENGTH - 1));
		testPostUrlAndExpectBadRequest(team, TEAM_CREATION_URL, Field.NAME_FR, ErrorCode.TOO_SHORT);
	}

	@Test
	public void create_WithMissingSport_ShouldReturnBadRequest() throws Exception {
		Team team = createValidTeamForCreation();
		team.setSport(null);
		testPostUrlAndExpectBadRequest(team, TEAM_CREATION_URL, Field.SPORT, ErrorCode.EMPTY);
	}

	@Test
	public void create_WithMissingShortEnglishName_ShouldReturnBadRequest() throws Exception {
		Team team = createValidTeamForCreation();
		team.setShortNameEn(null);
		testPostUrlAndExpectBadRequest(team, TEAM_CREATION_URL, Field.SHORT_NAME_EN, ErrorCode.EMPTY);
	}

	@Test
	public void create_WithTooLongShortEnglishName_ShouldReturnBadRequest() throws Exception {
		Team team = createValidTeamForCreation();
		team.setShortNameEn(StringTools.random(SHORT_NAME_MAX_LENGTH + 1));
		testPostUrlAndExpectBadRequest(team, TEAM_CREATION_URL, Field.SHORT_NAME_EN, ErrorCode.TOO_LONG);
	}

	@Test
	public void create_WithTooShortShortEnglishName_ShouldReturnBadRequest() throws Exception {
		Team team = createValidTeamForCreation();
		team.setShortNameEn(StringTools.random(TEXT_MIN_LENGTH - 1));
		testPostUrlAndExpectBadRequest(team, TEAM_CREATION_URL, Field.SHORT_NAME_EN, ErrorCode.TOO_SHORT);
	}

	@Test
	public void create_WithMissingShortFrenchName_ShouldReturnBadRequest() throws Exception {
		Team team = createValidTeamForCreation();
		team.setShortNameFr(null);
		testPostUrlAndExpectBadRequest(team, TEAM_CREATION_URL, Field.SHORT_NAME_FR, ErrorCode.EMPTY);
	}

	@Test
	public void create_WithTooLongShortFrenchName_ShouldReturnBadRequest() throws Exception {
		Team team = createValidTeamForCreation();
		team.setShortNameFr(StringTools.random(SHORT_NAME_MAX_LENGTH + 1));
		testPostUrlAndExpectBadRequest(team, TEAM_CREATION_URL, Field.SHORT_NAME_FR, ErrorCode.TOO_LONG);
	}

	@Test
	public void create_WithTooShortShortFrenchName_ShouldReturnBadRequest() throws Exception {
		Team team = createValidTeamForCreation();
		team.setShortNameFr(StringTools.random(TEXT_MIN_LENGTH - 1));
		testPostUrlAndExpectBadRequest(team, TEAM_CREATION_URL, Field.SHORT_NAME_FR, ErrorCode.TOO_SHORT);
	}

	@Test
	public void edit_WithValidTeam_ShouldReturnOk() throws Exception {
		Team team = createValidTeamForEdition();
		String jsonTeam = asJsonString(team);
		given(teamService.edit(anyObject(), anyObject())).willReturn(team);

		ResultActions actions = mockMvc.perform(put(TEAM_EDITION_URL).contentType(MediaType.APPLICATION_JSON).content(jsonTeam));
		actions.andExpect(status().isOk());
		Assert.assertEquals(asJsonString(team), actions.andReturn().getResponse().getContentAsString());
	}

	@Test
	public void edit_UnknownTeam_ShouldReturnNotFound() throws Exception {
		Team team = createValidTeamForEdition();
		String jsonTeam = asJsonString(team);
		given(teamService.edit(anyObject(), anyObject())).willThrow(new NotFoundException(Field.ID, Team.class.getName()));

		ResultActions actions = mockMvc.perform(put(TEAM_EDITION_URL).contentType(MediaType.APPLICATION_JSON).content(jsonTeam));
		actions.andExpect(status().isNotFound());
		actions.andExpect(jsonPath("$.message", equalTo(NotFoundException.messages.get(Field.ID.name() + Team.class.getName()))));
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.ID.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.NOT_FOUND.getJsonValue())));
	}

	@Test
	public void edit_WithExistingEnglishName_ShouldReturnBadRequest() throws Exception {
		Team team = createValidTeamForEdition();
		given(teamService.edit(anyObject(), anyObject())).willThrow(new AlreadyExistsException(Field.NAME_EN, Team.class.getName()));

		ResultActions actions = mockMvc.perform(put(TEAM_EDITION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(team)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.message", equalTo(AlreadyExistsException.messages.get(Field.NAME_EN.name() + Team.class.getName()))));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.NAME_EN.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.ALREADY_EXISTS.getJsonValue())));
	}

	@Test
	public void edit_WithExistingFrenchName_ShouldReturnBadRequest() throws Exception {
		Team team = createValidTeamForEdition();
		given(teamService.edit(anyObject(), anyObject())).willThrow(new AlreadyExistsException(Field.NAME_FR, Team.class.getName()));

		ResultActions actions = mockMvc.perform(put(TEAM_EDITION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(team)));
		actions.andExpect(status().isBadRequest());
		actions.andExpect(jsonPath("$.errors", hasSize(1)));
		actions.andExpect(jsonPath("$.message", equalTo(AlreadyExistsException.messages.get(Field.NAME_FR.name() + Team.class.getName()))));
		actions.andExpect(jsonPath("$.errors[0].field", equalTo(Field.NAME_FR.toString())));
		actions.andExpect(jsonPath("$.errors[0].errorCode", equalTo(ErrorCode.ALREADY_EXISTS.getJsonValue())));
	}

	@Test
	public void edit_WithServerException_ShouldReturnInternalServerError() throws Exception {
		Team team = createValidTeamForEdition();
		given(teamService.edit(anyObject(), anyObject())).willThrow(new RuntimeException("Message"));

		ResultActions actions = mockMvc.perform(put(TEAM_EDITION_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(team)));
		actions.andExpect(status().isInternalServerError());
		actions.andExpect(jsonPath("$.message", equalTo("Message")));
	}

	@Test
	public void edit_WithMissingId_ShouldReturnBadRequest() throws Exception {
		Team team = createValidTeamForEdition();
		team.setId(null);
		testPutUrlAndExpectBadRequest(team, TEAM_EDITION_URL, Field.ID, ErrorCode.EMPTY);
	}

	@Test
	public void edit_WithMissingEnglishName_ShouldReturnBadRequest() throws Exception {
		Team team = createValidTeamForEdition();
		team.setNameEn(null);
		testPutUrlAndExpectBadRequest(team, TEAM_EDITION_URL, Field.NAME_EN, ErrorCode.EMPTY);
	}

	@Test
	public void edit_WithTooLongEnglishName_ShouldReturnBadRequest() throws Exception {
		Team team = createValidTeamForEdition();
		team.setNameEn(StringTools.random(NAME_MAX_LENGTH + 1));
		testPutUrlAndExpectBadRequest(team, TEAM_EDITION_URL, Field.NAME_EN, ErrorCode.TOO_LONG);
	}

	@Test
	public void edit_WithTooShortEnglishName_ShouldReturnBadRequest() throws Exception {
		Team team = createValidTeamForEdition();
		team.setNameEn(StringTools.random(TEXT_MIN_LENGTH - 1));
		testPutUrlAndExpectBadRequest(team, TEAM_EDITION_URL, Field.NAME_EN, ErrorCode.TOO_SHORT);
	}

	@Test
	public void edit_WithMissingFrenchName_ShouldReturnBadRequest() throws Exception {
		Team team = createValidTeamForEdition();
		team.setNameFr(null);
		testPutUrlAndExpectBadRequest(team, TEAM_EDITION_URL, Field.NAME_FR, ErrorCode.EMPTY);
	}

	@Test
	public void edit_WithTooLongFrenchName_ShouldReturnBadRequest() throws Exception {
		Team team = createValidTeamForEdition();
		team.setNameFr(StringTools.random(NAME_MAX_LENGTH + 1));
		testPutUrlAndExpectBadRequest(team, TEAM_EDITION_URL, Field.NAME_FR, ErrorCode.TOO_LONG);
	}

	@Test
	public void edit_WithTooShortFrenchName_ShouldReturnBadRequest() throws Exception {
		Team team = createValidTeamForEdition();
		team.setNameFr(StringTools.random(TEXT_MIN_LENGTH - 1));
		testPutUrlAndExpectBadRequest(team, TEAM_EDITION_URL, Field.NAME_FR, ErrorCode.TOO_SHORT);
	}

	@Test
	public void edit_WithMissingSport_ShouldReturnBadRequest() throws Exception {
		Team team = createValidTeamForEdition();
		team.setSport(null);
		testPutUrlAndExpectBadRequest(team, TEAM_EDITION_URL, Field.SPORT, ErrorCode.EMPTY);
	}

	@Test
	public void edit_WithMissingShortEnglishName_ShouldReturnBadRequest() throws Exception {
		Team team = createValidTeamForEdition();
		team.setShortNameEn(null);
		testPutUrlAndExpectBadRequest(team, TEAM_EDITION_URL, Field.SHORT_NAME_EN, ErrorCode.EMPTY);
	}

	@Test
	public void edit_WithTooLongShortEnglishName_ShouldReturnBadRequest() throws Exception {
		Team team = createValidTeamForEdition();
		team.setShortNameEn(StringTools.random(NAME_MAX_LENGTH + 1));
		testPutUrlAndExpectBadRequest(team, TEAM_EDITION_URL, Field.SHORT_NAME_EN, ErrorCode.TOO_LONG);
	}

	@Test
	public void edit_WithTooShortShortEnglishName_ShouldReturnBadRequest() throws Exception {
		Team team = createValidTeamForEdition();
		team.setShortNameEn(StringTools.random(TEXT_MIN_LENGTH - 1));
		testPutUrlAndExpectBadRequest(team, TEAM_EDITION_URL, Field.SHORT_NAME_EN, ErrorCode.TOO_SHORT);
	}

	@Test
	public void edit_WithMissingShortFrenchName_ShouldReturnBadRequest() throws Exception {
		Team team = createValidTeamForEdition();
		team.setShortNameFr(null);
		testPutUrlAndExpectBadRequest(team, TEAM_EDITION_URL, Field.SHORT_NAME_FR, ErrorCode.EMPTY);
	}

	@Test
	public void edit_WithTooLongShortFrenchName_ShouldReturnBadRequest() throws Exception {
		Team team = createValidTeamForEdition();
		team.setShortNameFr(StringTools.random(NAME_MAX_LENGTH + 1));
		testPutUrlAndExpectBadRequest(team, TEAM_EDITION_URL, Field.SHORT_NAME_FR, ErrorCode.TOO_LONG);
	}

	@Test
	public void edit_WithTooShortShortFrenchName_ShouldReturnBadRequest() throws Exception {
		Team team = createValidTeamForEdition();
		team.setShortNameFr(StringTools.random(TEXT_MIN_LENGTH - 1));
		testPutUrlAndExpectBadRequest(team, TEAM_EDITION_URL, Field.SHORT_NAME_FR, ErrorCode.TOO_SHORT);
	}

	private Team createValidTeamForCreation() {
		Team team = new Team();
		team.setNameEn(StringTools.random(NAME_MAX_LENGTH));
		team.setNameFr(StringTools.random(NAME_MAX_LENGTH));
		team.setSport(Sport.SOCCER);
		team.setShortNameEn(StringTools.random(SHORT_NAME_MAX_LENGTH));
		team.setShortNameFr(StringTools.random(SHORT_NAME_MAX_LENGTH));
		return team;
	}

	private Team createValidTeamForEdition() {
		Team team = createValidTeamForCreation();
		team.setId(1l);
		return team;
	}
}
