package com.web2h.betmates.restapp.core.service.reference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;

import com.web2h.betmates.restapp.core.service.CommonTest;
import com.web2h.betmates.restapp.model.entity.log.LogEventType;
import com.web2h.betmates.restapp.model.entity.reference.Sport;
import com.web2h.betmates.restapp.model.entity.reference.Team;
import com.web2h.betmates.restapp.model.entity.reference.log.ReferenceLogEvent;
import com.web2h.betmates.restapp.model.entity.reference.log.ReferenceLogEventChange;
import com.web2h.betmates.restapp.model.entity.user.AppUser;
import com.web2h.betmates.restapp.model.exception.AlreadyExistsException;
import com.web2h.betmates.restapp.model.exception.InvalidDataException;
import com.web2h.betmates.restapp.model.validation.Field;

/**
 * Team service test class.
 * 
 * @author web2h
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class TeamServiceTest extends CommonTest {

	@Autowired
	private TeamService sut;

	@Test(expected = NullPointerException.class)
	public void create_WithNullCountry_ThrowNullPointerException() throws AlreadyExistsException, InvalidDataException {
		sut.create(null, new AppUser());
	}

	@Test(expected = NullPointerException.class)
	public void create_WithNullCreator_ThrowNullPointerException() throws AlreadyExistsException, InvalidDataException {
		sut.create(new Team(), null);
	}

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-dataset/data.sql")
	public void create_WithExistingEnglishName_ThrowAlreadyExistsException() throws AlreadyExistsException {
		Team team = new Team();
		team.setNameEn("Boston Celtics");
		team.setNameFr("Boston Bruins");
		team.setSport(Sport.BASKET_BALL);
		team.setShortNameEn("Bruins");
		team.setShortNameFr("Bruins");

		try {
			sut.create(team, admin);
			fail("Creation should never succeed");
		} catch (Exception e) {
			assertTrue(e instanceof AlreadyExistsException);
			assertEquals(AlreadyExistsException.messages.get(Field.NAME_EN.name() + Team.class.getName()), e.getMessage());
		}
	}

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-dataset/data.sql")
	public void create_WithExistingFrenchName_ThrowAlreadyExistsException() throws AlreadyExistsException {
		Team team = new Team();
		team.setNameEn("Boston Bruins");
		team.setNameFr("Boston Celtics");
		team.setSport(Sport.BASKET_BALL);
		team.setShortNameEn("Bruins");
		team.setShortNameFr("Bruins");

		try {
			sut.create(team, admin);
			fail("Creation should never succeed");
		} catch (Exception e) {
			assertTrue(e instanceof AlreadyExistsException);
			assertEquals(AlreadyExistsException.messages.get(Field.NAME_FR.name() + Team.class.getName()), e.getMessage());
		}
	}

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-dataset/data.sql")
	public void create_WithValidTeam_Succeed() throws AlreadyExistsException, InvalidDataException {
		Team team = new Team();
		team.setNameEn("Paris Saint-Germain En");
		team.setNameFr("Paris Saint-Germain Fr");
		team.setSport(Sport.SOCCER);
		team.setShortNameEn("PSG En");
		team.setShortNameFr("PSG Fr");

		Team createdTeam = sut.create(team, admin);
		assertNotNull(createdTeam.getId());
		assertEquals(team, createdTeam);

		List<ReferenceLogEvent> log = sut.getLog(createdTeam.getId());
		assertEquals(1, log.size());
		assertEquals(LogEventType.CREATION, log.get(0).getType());
		assertEquals(createdTeam, log.get(0).getReference());
		assertEquals(admin, log.get(0).getAppUser());
		assertEquals(5, log.get(0).getChanges().size());
		int changeCount = 0;
		for (ReferenceLogEventChange change : log.get(0).getChanges()) {
			if (Field.NAME_EN.equals(change.getField())) {
				assertNull(change.getOldValue());
				assertEquals(createdTeam.getNameEn(), change.getNewValue());
				changeCount++;
			} else if (Field.NAME_FR.equals(change.getField())) {
				assertNull(change.getOldValue());
				assertEquals(createdTeam.getNameFr(), change.getNewValue());
				changeCount++;
			} else if (Field.SPORT.equals(change.getField())) {
				assertNull(change.getOldValue());
				assertEquals(createdTeam.getSport().getLogValue(), change.getNewValue());
				changeCount++;
			} else if (Field.SHORT_NAME_EN.equals(change.getField())) {
				assertNull(change.getOldValue());
				assertEquals(createdTeam.getShortNameEn(), change.getNewValue());
				changeCount++;
			} else if (Field.SHORT_NAME_FR.equals(change.getField())) {
				assertNull(change.getOldValue());
				assertEquals(createdTeam.getShortNameFr(), change.getNewValue());
				changeCount++;
			}
		}
		assertEquals(log.get(0).getChanges().size(), changeCount);
	}

	@Test(expected = NullPointerException.class)
	public void edit_WithNullCountry_ThrowNullPointerException() throws Exception {
		sut.edit(null, new AppUser());
	}

	@Test(expected = NullPointerException.class)
	public void edit_WithNullCreator_ThrowNullPointerException() throws Exception {
		sut.edit(new Team(), null);
	}

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-dataset/data.sql")
	public void edit_WithEnglishNameThatAlreadyExists_ThrowAlreadyExistsException() throws AlreadyExistsException {
		miamiHeat.setNameEn(bostonCeltics.getNameEn());

		try {
			sut.edit(miamiHeat, admin);
			fail("Edition should never succeed");
		} catch (Exception e) {
			assertTrue(e instanceof AlreadyExistsException);
			assertEquals(AlreadyExistsException.messages.get(Field.NAME_EN.name() + Team.class.getName()), e.getMessage());
		}
	}

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-dataset/data.sql")
	public void edit_WithFrenchNameThatAlreadyExists_ThrowAlreadyExistsException() throws AlreadyExistsException {
		miamiHeat.setNameFr(bostonCeltics.getNameFr());

		try {
			sut.edit(miamiHeat, admin);
			fail("Edition should never succeed");
		} catch (Exception e) {
			assertTrue(e instanceof AlreadyExistsException);
			assertEquals(AlreadyExistsException.messages.get(Field.NAME_FR.name() + Team.class.getName()), e.getMessage());
		}
	}

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-dataset/data.sql")
	public void edit_WithNewValues_Succeed() throws Exception {
		miamiHeat.setNameEn("Miami Heat 2");
		miamiHeat.setNameFr("Miami Heat 2");
		miamiHeat.setSport(Sport.SOCCER);
		miamiHeat.setShortNameEn("Heat 2");
		miamiHeat.setShortNameFr("Heat 2");

		Team editedTeam = sut.edit(miamiHeat, admin);
		assertEquals(new Long(31), editedTeam.getId());
		assertEquals(sut.get(miamiHeat.getId()), miamiHeat);

		List<ReferenceLogEvent> log = sut.getLog(editedTeam.getId());
		assertEquals(2, log.size());
		assertEquals(LogEventType.EDITION, log.get(0).getType());
		assertEquals(editedTeam, log.get(0).getReference());
		assertEquals(admin, log.get(0).getAppUser());
		assertEquals(5, log.get(0).getChanges().size());
		int changeCount = 0;
		for (ReferenceLogEventChange change : log.get(0).getChanges()) {
			if (Field.NAME_EN.equals(change.getField())) {
				assertEquals("Miami Heat", change.getOldValue());
				assertEquals(editedTeam.getNameEn(), change.getNewValue());
				changeCount++;
			} else if (Field.NAME_FR.equals(change.getField())) {
				assertEquals("Miami Heat", change.getOldValue());
				assertEquals(editedTeam.getNameFr(), change.getNewValue());
				changeCount++;
			} else if (Field.SPORT.equals(change.getField())) {
				assertEquals(Sport.BASKET_BALL.getLogValue(), change.getOldValue());
				assertEquals(editedTeam.getSport().getLogValue(), change.getNewValue());
				changeCount++;
			} else if (Field.SHORT_NAME_EN.equals(change.getField())) {
				assertEquals("Heat", change.getOldValue());
				assertEquals(editedTeam.getShortNameEn(), change.getNewValue());
				changeCount++;
			} else if (Field.SHORT_NAME_FR.equals(change.getField())) {
				assertEquals("Heat", change.getOldValue());
				assertEquals(editedTeam.getShortNameFr(), change.getNewValue());
				changeCount++;
			}
		}
		assertEquals(log.get(0).getChanges().size(), changeCount);
	}

}