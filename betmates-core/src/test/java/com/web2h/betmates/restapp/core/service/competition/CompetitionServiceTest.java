package com.web2h.betmates.restapp.core.service.competition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;

import com.web2h.betmates.restapp.core.service.CommonTest;
import com.web2h.betmates.restapp.core.service.reference.TeamService;
import com.web2h.betmates.restapp.core.service.reference.VenueService;
import com.web2h.betmates.restapp.model.entity.competition.Competition;
import com.web2h.betmates.restapp.model.entity.competition.CompetitionGroup;
import com.web2h.betmates.restapp.model.entity.competition.CompetitionTeam;
import com.web2h.betmates.restapp.model.entity.competition.CompetitionType;
import com.web2h.betmates.restapp.model.entity.competition.log.CompetitionLogEvent;
import com.web2h.betmates.restapp.model.entity.competition.log.CompetitionLogEventChange;
import com.web2h.betmates.restapp.model.entity.log.LogEventType;
import com.web2h.betmates.restapp.model.entity.reference.Venue;
import com.web2h.betmates.restapp.model.entity.user.AppUser;
import com.web2h.betmates.restapp.model.exception.AlreadyExistsException;
import com.web2h.betmates.restapp.model.exception.InvalidDataException;
import com.web2h.betmates.restapp.model.exception.NotFoundException;
import com.web2h.betmates.restapp.model.validation.Field;
import com.web2h.tools.DateTools;

/**
 * Competition service test class.
 * 
 * @author web2h
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class CompetitionServiceTest extends CommonTest {

	@Autowired
	private CompetitionService sut;

	@Autowired
	private TeamService teamService;

	@Autowired
	private VenueService venueService;

	private Date someDate;

	private Date someDate2;

	@Before
	public void before() {
		super.before();
		Calendar calendar = Calendar.getInstance();
		calendar.set(2018, Calendar.JUNE, 10, 20, 00, 00);
		someDate = calendar.getTime();

		calendar.set(2020, Calendar.JUNE, 10, 20, 00, 00);
		someDate2 = calendar.getTime();
	}

	@Test(expected = NullPointerException.class)
	public void addOrRemoveTeams_WithNullCompetition_ThrowNullPointerException() throws Exception {
		sut.addOrRemoveTeams(null, new AppUser());
	}

	@Test(expected = NullPointerException.class)
	public void addOrRemoveTeams_WithNullEditor_ThrowNullPointerException() throws Exception {
		sut.addOrRemoveTeams(new Competition(), null);
	}

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-dataset/data.sql")
	public void addOrRemoveTeams_WithUnknownCompetition_ThrowNotFoundException() throws NotFoundException {
		nbaPlayoffs2018.setId(10000l);

		try {
			sut.addOrRemoveTeams(nbaPlayoffs2018, admin);
			fail("Team change should never succeed");
		} catch (Exception e) {
			assertTrue(e instanceof NotFoundException);
			assertEquals(NotFoundException.messages.get(Field.ID.name() + Competition.class.getName()), e.getMessage());
		}
	}

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-dataset/data.sql")
	public void addOrRemoveTeams_WithUnknownTeam_ThrowInvalidDataException() throws Exception {
		nbaPlayoffs2018.addTeam(teamService.get(chicagoBulls.getId()), CompetitionGroup.EAST, 4);
		nbaPlayoffs2018.addTeam(unknownTeam, CompetitionGroup.EAST, 2);

		try {
			sut.addOrRemoveTeams(nbaPlayoffs2018, admin);
			fail("Team change should never succeed");
		} catch (Exception e) {
			assertTrue(e instanceof InvalidDataException);
			assertEquals(InvalidDataException.DEFAULT_MESSAGE, e.getMessage());
		}
	}

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-dataset/data.sql")
	public void addOrRemoveTeams_AddTeams_TeamsAreAdded() throws Exception {
		nbaPlayoffs2018.addTeam(teamService.get(chicagoBulls.getId()), CompetitionGroup.EAST, 8);
		nbaPlayoffs2018.addTeam(teamService.get(orlandoMagic.getId()), CompetitionGroup.EAST, 7);

		sut.addOrRemoveTeams(nbaPlayoffs2018, admin);

		Competition editedCompetition = sut.get(nbaPlayoffs2018.getId());
		assertEquals(4, editedCompetition.getTeams().size());

		boolean miamiIsHere = false;
		boolean chicagoIsHere = false;
		boolean bostonIsHere = false;
		boolean orlandoIsHere = false;

		for (CompetitionTeam team : editedCompetition.getTeams()) {
			if (bostonCeltics.equals(team.getTeam())) {
				bostonIsHere = true;
			} else if (miamiHeat.equals(team.getTeam())) {
				miamiIsHere = true;
			} else if (chicagoBulls.equals(team.getTeam())) {
				chicagoIsHere = true;
			} else if (orlandoMagic.equals(team.getTeam())) {
				orlandoIsHere = true;
			}
		}
		assertTrue(miamiIsHere);
		assertTrue(chicagoIsHere);
		assertTrue(bostonIsHere);
		assertTrue(orlandoIsHere);

		List<CompetitionLogEvent> log = sut.getLog(editedCompetition.getId());
		assertEquals(7, log.size());
		assertEquals(LogEventType.TEAM_ADDITION, log.get(0).getType());
		assertEquals(LogEventType.TEAM_ADDITION, log.get(1).getType());
		assertEquals(editedCompetition, log.get(0).getCompetition());
		assertEquals(admin, log.get(0).getAppUser());

		boolean descriptionsOk = false;
		if ("Team (34)Chicago Bulls has been added".equals(log.get(0).getDescription()) && "Team (33)Orlando Magic has been added".equals(log.get(1).getDescription())) {
			descriptionsOk = true;
		}
		if ("Team (34)Chicago Bulls has been added".equals(log.get(1).getDescription()) && "Team (33)Orlando Magic has been added".equals(log.get(0).getDescription())) {
			descriptionsOk = true;
		}
		assertTrue(descriptionsOk);
	}

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-dataset/data.sql")
	public void addOrRemoveTeams_RemoveTeam_TeamIsRemoved() throws Exception {
		nbaPlayoffs2018.removeTeam(bostonCeltics);

		sut.addOrRemoveTeams(nbaPlayoffs2018, admin);

		Competition editedCompetition = sut.get(nbaPlayoffs2018.getId());
		assertEquals(1, editedCompetition.getTeams().size());

		boolean miamiIsHere = false;
		boolean bostonIsNotHere = true;

		for (CompetitionTeam team : editedCompetition.getTeams()) {
			if (bostonCeltics.equals(team.getTeam())) {
				bostonIsNotHere = false;
			} else if (miamiHeat.equals(team.getTeam())) {
				miamiIsHere = true;
			}
		}
		assertTrue(miamiIsHere);
		assertTrue(bostonIsNotHere);

		List<CompetitionLogEvent> log = sut.getLog(editedCompetition.getId());
		assertEquals(6, log.size());
		assertEquals(LogEventType.TEAM_REMOVAL, log.get(0).getType());
		assertEquals(editedCompetition, log.get(0).getCompetition());
		assertEquals(admin, log.get(0).getAppUser());
		assertEquals("Team (32)Boston Celtics has been removed", log.get(0).getDescription());
	}

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-dataset/data.sql")
	public void addOrRemoveTeams_AddTeamAndRemoveTeam_TeamsAreUpdated() throws Exception {
		nbaPlayoffs2018.removeTeam(bostonCeltics);
		nbaPlayoffs2018.addTeam(teamService.get(chicagoBulls.getId()), CompetitionGroup.EAST, 7);

		sut.addOrRemoveTeams(nbaPlayoffs2018, admin);

		Competition editedCompetition = sut.get(nbaPlayoffs2018.getId());
		assertEquals(2, editedCompetition.getTeams().size());

		boolean miamiIsHere = false;
		boolean chicagoIsHere = false;
		boolean bostonIsNotHere = true;

		for (CompetitionTeam team : editedCompetition.getTeams()) {
			if (bostonCeltics.equals(team.getTeam())) {
				bostonIsNotHere = false;
			} else if (miamiHeat.equals(team.getTeam())) {
				miamiIsHere = true;
			} else if (chicagoBulls.equals(team.getTeam())) {
				chicagoIsHere = true;
			}
		}
		assertTrue(miamiIsHere);
		assertTrue(chicagoIsHere);
		assertTrue(bostonIsNotHere);

		List<CompetitionLogEvent> log = sut.getLog(editedCompetition.getId());
		assertEquals(7, log.size());
		assertEquals(LogEventType.TEAM_REMOVAL, log.get(1).getType());
		assertEquals(editedCompetition, log.get(1).getCompetition());
		assertEquals(admin, log.get(1).getAppUser());
		assertEquals("Team (32)Boston Celtics has been removed", log.get(1).getDescription());
		assertEquals(LogEventType.TEAM_ADDITION, log.get(0).getType());
		assertEquals(editedCompetition, log.get(0).getCompetition());
		assertEquals(admin, log.get(0).getAppUser());
		assertEquals("Team (34)Chicago Bulls has been added", log.get(0).getDescription());
	}

	@Test(expected = NullPointerException.class)
	public void addOrRemoveVenues_WithNullCompetition_ThrowNullPointerException() throws Exception {
		sut.addOrRemoveVenues(null, new AppUser());
	}

	@Test(expected = NullPointerException.class)
	public void addOrRemoveVenues_WithNullEditor_ThrowNullPointerException() throws Exception {
		sut.addOrRemoveVenues(new Competition(), null);
	}

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-dataset/data.sql")
	public void addOrRemoveVenues_WithUnknownCompetition_ThrowNotFoundException() throws NotFoundException {
		nbaPlayoffs2018.setId(10000l);

		try {
			sut.addOrRemoveVenues(nbaPlayoffs2018, admin);
			fail("Venue change should never succeed");
		} catch (Exception e) {
			assertTrue(e instanceof NotFoundException);
			assertEquals(NotFoundException.messages.get(Field.ID.name() + Competition.class.getName()), e.getMessage());
		}
	}

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-dataset/data.sql")
	public void addOrRemoveVenues_WithUnknownTeam_ThrowInvalidDataException() throws Exception {
		nbaPlayoffs2018.getVenues().add(unknownVenue);

		try {
			sut.addOrRemoveVenues(nbaPlayoffs2018, admin);
			fail("Venue change should never succeed");
		} catch (Exception e) {
			assertTrue(e instanceof InvalidDataException);
			assertEquals(InvalidDataException.DEFAULT_MESSAGE, e.getMessage());
		}
	}

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-dataset/data.sql")
	public void addOrRemoveVenues_AddVenues_VenuesAreAdded() throws Exception {
		nbaPlayoffs2018.getVenues().add(venueService.get(uCenter.getId()));

		sut.addOrRemoveVenues(nbaPlayoffs2018, admin);

		Competition editedCompetition = sut.get(nbaPlayoffs2018.getId());
		assertEquals(3, editedCompetition.getVenues().size());

		boolean aaArenaIsHere = false;
		boolean uCenterIsHere = false;
		boolean tdGardenIsHere = false;

		for (Venue venue : editedCompetition.getVenues()) {
			if (tdGarden.equals(venue)) {
				tdGardenIsHere = true;
			} else if (aaArena.equals(venue)) {
				aaArenaIsHere = true;
			} else if (uCenter.equals(venue)) {
				uCenterIsHere = true;
			}
		}
		assertTrue(aaArenaIsHere);
		assertTrue(tdGardenIsHere);
		assertTrue(uCenterIsHere);

		List<CompetitionLogEvent> log = sut.getLog(editedCompetition.getId());
		assertEquals(6, log.size());
		assertEquals(LogEventType.VENUE_ADDITION, log.get(0).getType());
		assertEquals(editedCompetition, log.get(0).getCompetition());
		assertEquals(admin, log.get(0).getAppUser());
		assertEquals("Venue (25)United Center has been added", log.get(0).getDescription());
	}

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-dataset/data.sql")
	public void addOrRemoveVenues_RemoveVenue_VenueIsRemoved() throws Exception {
		nbaPlayoffs2018.getVenues().remove(tdGarden);

		sut.addOrRemoveVenues(nbaPlayoffs2018, admin);

		Competition editedCompetition = sut.get(nbaPlayoffs2018.getId());
		assertEquals(1, editedCompetition.getVenues().size());

		boolean aaArenaIsHere = false;
		boolean tdGardenIsNotHere = true;

		for (Venue venue : editedCompetition.getVenues()) {
			if (tdGarden.equals(venue)) {
				tdGardenIsNotHere = false;
			} else if (aaArena.equals(venue)) {
				aaArenaIsHere = true;
			}
		}
		assertTrue(aaArenaIsHere);
		assertTrue(tdGardenIsNotHere);

		List<CompetitionLogEvent> log = sut.getLog(editedCompetition.getId());
		assertEquals(6, log.size());
		assertEquals(LogEventType.VENUE_REMOVAL, log.get(0).getType());
		assertEquals(editedCompetition, log.get(0).getCompetition());
		assertEquals(admin, log.get(0).getAppUser());
		assertEquals("Venue (24)TD Garden has been removed", log.get(0).getDescription());
	}

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-dataset/data.sql")
	public void addOrRemoveVenues_AddVenueAndRemoveVenue_VenuesAreUpdated() throws Exception {
		nbaPlayoffs2018.getVenues().remove(tdGarden);
		nbaPlayoffs2018.getVenues().add(venueService.get(uCenter.getId()));

		sut.addOrRemoveVenues(nbaPlayoffs2018, admin);

		Competition editedCompetition = sut.get(nbaPlayoffs2018.getId());
		assertEquals(2, editedCompetition.getVenues().size());

		boolean aaArenaIsHere = false;
		boolean uCenterIsHere = false;
		boolean tdGardenIsNotHere = true;

		for (Venue venue : editedCompetition.getVenues()) {
			if (tdGarden.equals(venue)) {
				tdGardenIsNotHere = false;
			} else if (aaArena.equals(venue)) {
				aaArenaIsHere = true;
			} else if (uCenter.equals(venue)) {
				uCenterIsHere = true;
			}
		}
		assertTrue(uCenterIsHere);
		assertTrue(aaArenaIsHere);
		assertTrue(tdGardenIsNotHere);

		List<CompetitionLogEvent> log = sut.getLog(editedCompetition.getId());
		assertEquals(7, log.size());
		assertEquals(LogEventType.VENUE_REMOVAL, log.get(1).getType());
		assertEquals(editedCompetition, log.get(1).getCompetition());
		assertEquals(admin, log.get(1).getAppUser());
		assertEquals("Venue (24)TD Garden has been removed", log.get(1).getDescription());
		assertEquals(LogEventType.VENUE_ADDITION, log.get(0).getType());
		assertEquals(editedCompetition, log.get(0).getCompetition());
		assertEquals(admin, log.get(0).getAppUser());
		assertEquals("Venue (25)United Center has been added", log.get(0).getDescription());
	}

	@Test(expected = NullPointerException.class)
	public void create_WithNullCompetition_ThrowNullPointerException() throws AlreadyExistsException, InvalidDataException {
		sut.create(null, new AppUser());
	}

	@Test(expected = NullPointerException.class)
	public void create_WithNullCreator_ThrowNullPointerException() throws AlreadyExistsException, InvalidDataException {
		sut.create(new Competition(), null);
	}

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-dataset/data.sql")
	public void create_WithExistingEnglishName_ThrowAlreadyExistsException() throws AlreadyExistsException {
		Competition competition = createValidCompetitionForCreation();
		competition.setNameEn("Russia 2018");

		try {
			sut.create(competition, admin);
			fail("Creation should never succeed");
		} catch (Exception e) {
			assertTrue(e instanceof AlreadyExistsException);
			assertEquals(AlreadyExistsException.messages.get(Field.NAME_EN.name() + Competition.class.getName()), e.getMessage());
		}
	}

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-dataset/data.sql")
	public void create_WithExistingFrenchName_ThrowAlreadyExistsException() throws AlreadyExistsException {
		Competition competition = createValidCompetitionForCreation();
		competition.setNameFr("Russie 2018");

		try {
			sut.create(competition, admin);
			fail("Creation should never succeed");
		} catch (Exception e) {
			assertTrue(e instanceof AlreadyExistsException);
			assertEquals(AlreadyExistsException.messages.get(Field.NAME_FR.name() + Competition.class.getName()), e.getMessage());
		}
	}

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-dataset/data.sql")
	public void create_WithNewValidCompetition_Succeed() throws AlreadyExistsException, InvalidDataException {
		Competition competition = createValidCompetitionForCreation();

		Competition createdCompetition = sut.create(competition, admin);
		assertNotNull(createdCompetition.getId());
		assertEquals(competition, createdCompetition);

		List<CompetitionLogEvent> log = sut.getLog(createdCompetition.getId());
		assertEquals(1, log.size());
		assertEquals(LogEventType.CREATION, log.get(0).getType());
		assertEquals(createdCompetition, log.get(0).getCompetition());
		assertEquals(admin, log.get(0).getAppUser());
		assertEquals(4, log.get(0).getChanges().size());
		int changeCount = 0;
		for (CompetitionLogEventChange change : log.get(0).getChanges()) {
			if (Field.NAME_EN.equals(change.getField())) {
				assertNull(change.getOldValue());
				assertEquals(createdCompetition.getNameEn(), change.getNewValue());
				changeCount++;
			} else if (Field.NAME_FR.equals(change.getField())) {
				assertNull(change.getOldValue());
				assertEquals(createdCompetition.getNameFr(), change.getNewValue());
				changeCount++;
			} else if (Field.TYPE.equals(change.getField())) {
				assertNull(change.getOldValue());
				assertEquals(createdCompetition.getType().toString(), change.getNewValue());
				changeCount++;
			} else if (Field.START_DATE.equals(change.getField())) {
				assertNull(change.getOldValue());
				assertEquals(DateTools.toString(someDate), change.getNewValue());
				changeCount++;
			}
		}
		assertEquals(log.get(0).getChanges().size(), changeCount);

	}

	@Test(expected = NullPointerException.class)
	public void edit_WithNullCompetition_ThrowNullPointerException() throws Exception {
		sut.edit(null, new AppUser());
	}

	@Test(expected = NullPointerException.class)
	public void edit_WithNullCreator_ThrowNullPointerException() throws Exception {
		sut.edit(new Competition(), null);
	}

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-dataset/data.sql")
	public void edit_WithEnglishNameThatAlreadyExists_ThrowAlreadyExistsException() throws AlreadyExistsException {
		worldCup2018.setNameEn(nbaPlayoffs2018.getNameEn());

		try {
			sut.edit(worldCup2018, admin);
			fail("Edition should never succeed");
		} catch (Exception e) {
			assertTrue(e instanceof AlreadyExistsException);
			assertEquals(AlreadyExistsException.messages.get(Field.NAME_EN.name() + Competition.class.getName()), e.getMessage());
		}
	}

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-dataset/data.sql")
	public void edit_WithFrenchNameThatAlreadyExists_ThrowAlreadyExistsException() throws AlreadyExistsException {
		worldCup2018.setNameFr(nbaPlayoffs2018.getNameFr());

		try {
			sut.edit(worldCup2018, admin);
			fail("Edition should never succeed");
		} catch (Exception e) {
			assertTrue(e instanceof AlreadyExistsException);
			assertEquals(AlreadyExistsException.messages.get(Field.NAME_FR.name() + Competition.class.getName()), e.getMessage());
		}
	}

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-dataset/data.sql")
	public void edit_WithNewValues_Succeed() throws Exception {
		worldCup2018.setNameEn("Russia World Cup 2020");
		worldCup2018.setNameFr("Coupe du monde Russie 2020");
		worldCup2018.setType(CompetitionType.UEFA_EURO);
		worldCup2018.setStartDate(someDate2);

		Competition editedCompetition = sut.edit(worldCup2018, admin);
		assertEquals(new Long(1), editedCompetition.getId());
		Competition updatedCompetition = sut.get(worldCup2018.getId());
		assertEquals(updatedCompetition, worldCup2018);
		assertEquals(someDate2, updatedCompetition.getStartDate());

		List<CompetitionLogEvent> log = sut.getLog(editedCompetition.getId());
		assertEquals(2, log.size());
		assertEquals(LogEventType.EDITION, log.get(0).getType());
		assertEquals(editedCompetition, log.get(0).getCompetition());
		assertEquals(admin, log.get(0).getAppUser());
		assertEquals(4, log.get(0).getChanges().size());
		int changeCount = 0;
		for (CompetitionLogEventChange change : log.get(0).getChanges()) {
			if (Field.NAME_EN.equals(change.getField())) {
				assertEquals("Russia 2018", change.getOldValue());
				assertEquals(editedCompetition.getNameEn(), change.getNewValue());
				changeCount++;
			} else if (Field.NAME_FR.equals(change.getField())) {
				assertEquals("Russie 2018", change.getOldValue());
				assertEquals(editedCompetition.getNameFr(), change.getNewValue());
				changeCount++;
			} else if (Field.TYPE.equals(change.getField())) {
				assertEquals(CompetitionType.FIFA_WORLD_CUP.toString(), change.getOldValue());
				assertEquals(editedCompetition.getType().toString(), change.getNewValue());
				changeCount++;
			} else if (Field.START_DATE.equals(change.getField())) {
				assertEquals(DateTools.toString(someDate), change.getOldValue());
				assertEquals(DateTools.toString(someDate2), change.getNewValue());
				changeCount++;
			}
		}
		assertEquals(log.get(0).getChanges().size(), changeCount);

	}

	private Competition createValidCompetitionForCreation() {
		Competition competition = new Competition();
		competition.setNameEn("Russia 2019");
		competition.setNameFr("Russie 2019");
		competition.setType(CompetitionType.FIFA_WORLD_CUP);
		competition.setStartDate(someDate);

		return competition;
	}
}