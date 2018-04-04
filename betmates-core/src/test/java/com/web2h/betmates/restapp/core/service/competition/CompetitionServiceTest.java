package com.web2h.betmates.restapp.core.service.competition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;

import com.web2h.betmates.restapp.core.service.CommonTest;
import com.web2h.betmates.restapp.model.entity.competition.Competition;
import com.web2h.betmates.restapp.model.entity.competition.CompetitionType;
import com.web2h.betmates.restapp.model.entity.user.AppUser;
import com.web2h.betmates.restapp.model.exception.AlreadyExistsException;
import com.web2h.betmates.restapp.model.exception.InvalidDataException;
import com.web2h.betmates.restapp.model.validation.Field;

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

		// TODO Test logs
		/*
		 * List<ReferenceLogEvent> log = sut.getLog(createdCountry.getId()); assertEquals(1, log.size()); assertEquals(LogEventType.CREATION, log.get(0).getType());
		 * assertEquals(createdCountry, log.get(0).getReference()); assertEquals(admin, log.get(0).getAppUser()); assertEquals(2, log.get(0).getChanges().size()); int
		 * changeCount = 0; for (ReferenceLogEventChange change : log.get(0).getChanges()) { if (Field.NAME_EN.equals(change.getField())) {
		 * assertNull(change.getOldValue()); assertEquals(createdCountry.getNameEn(), change.getNewValue()); changeCount++; } else if
		 * (Field.NAME_FR.equals(change.getField())) { assertNull(change.getOldValue()); assertEquals(createdCountry.getNameFr(), change.getNewValue()); changeCount++;
		 * } } assertEquals(log.get(0).getChanges().size(), changeCount);
		 */
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

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2020);
		calendar.set(Calendar.MONTH, Calendar.JUNE);
		calendar.set(Calendar.DAY_OF_MONTH, 10);
		calendar.set(2020, Calendar.JUNE, 10, 20, 0, 0);
		Date newStartDate = calendar.getTime();
		worldCup2018.setStartDate(newStartDate);

		Competition editedCompetition = sut.edit(worldCup2018, admin);
		assertEquals(new Long(1), editedCompetition.getId());
		Competition updatedCompetition = sut.get(worldCup2018.getId());
		assertEquals(updatedCompetition, worldCup2018);
		assertEquals(newStartDate, updatedCompetition.getStartDate());

		// TODO Test logs

		/*
		 * List<ReferenceLogEvent> log = sut.getLog(editedCity.getId()); assertEquals(2, log.size()); assertEquals(LogEventType.EDITION, log.get(0).getType());
		 * assertEquals(editedCity, log.get(0).getReference()); assertEquals(admin, log.get(0).getAppUser()); assertEquals(2, log.get(0).getChanges().size()); int
		 * changeCount = 0; for (ReferenceLogEventChange change : log.get(0).getChanges()) { if (Field.NAME_EN.equals(change.getField())) { assertEquals("Lille",
		 * change.getOldValue()); assertEquals(editedCity.getNameEn(), change.getNewValue()); changeCount++; } else if (Field.NAME_FR.equals(change.getField())) {
		 * assertEquals("Lille", change.getOldValue()); assertEquals(editedCity.getNameFr(), change.getNewValue()); changeCount++; } }
		 * assertEquals(log.get(0).getChanges().size(), changeCount);
		 */
	}

	private Competition createValidCompetitionForCreation() {
		Competition competition = new Competition();
		competition.setNameEn("Russia 2019");
		competition.setNameFr("Russie 2019");
		competition.setType(CompetitionType.FIFA_WORLD_CUP);
		competition.setStartDate(new Date());

		return competition;
	}
}