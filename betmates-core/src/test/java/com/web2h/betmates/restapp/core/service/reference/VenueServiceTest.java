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
import com.web2h.betmates.restapp.model.entity.reference.City;
import com.web2h.betmates.restapp.model.entity.reference.Venue;
import com.web2h.betmates.restapp.model.entity.reference.log.ReferenceLogEvent;
import com.web2h.betmates.restapp.model.entity.reference.log.ReferenceLogEventChange;
import com.web2h.betmates.restapp.model.entity.user.AppUser;
import com.web2h.betmates.restapp.model.exception.AlreadyExistsException;
import com.web2h.betmates.restapp.model.exception.InvalidDataException;
import com.web2h.betmates.restapp.model.validation.Field;

/**
 * Venue service test class.
 * 
 * @author web2h
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class VenueServiceTest extends CommonTest {

	@Autowired
	private VenueService sut;

	@Test(expected = NullPointerException.class)
	public void create_WithNullVenue_ThrowNullPointerException() throws AlreadyExistsException, InvalidDataException {
		sut.create(null, new AppUser());
	}

	@Test(expected = NullPointerException.class)
	public void create_WithNullCreator_ThrowNullPointerException() throws AlreadyExistsException, InvalidDataException {
		sut.create(new Venue(), null);
	}

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-dataset/data.sql")
	public void create_WithExistingEnglishName_ThrowAlreadyExistsException() throws AlreadyExistsException {
		Venue venue = new Venue();
		venue.setNameEn("Parc des Princes");
		venue.setNameFr("Parc des Princes2");
		venue.setCity(paris);

		try {
			sut.create(venue, admin);
			fail("Creation should never succeed");
		} catch (Exception e) {
			assertTrue(e instanceof AlreadyExistsException);
			assertEquals(AlreadyExistsException.messages.get(Field.NAME_EN.name() + Venue.class.getName()), e.getMessage());
		}
	}

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-dataset/data.sql")
	public void create_WithExistingFrenchName_ThrowAlreadyExistsException() throws AlreadyExistsException {
		Venue venue = new Venue();
		venue.setNameEn("Parc des Princes2");
		venue.setNameFr("Parc des Princes");
		venue.setCity(paris);

		try {
			sut.create(venue, admin);
			fail("Creation should never succeed");
		} catch (Exception e) {
			assertTrue(e instanceof AlreadyExistsException);
			assertEquals(AlreadyExistsException.messages.get(Field.NAME_FR.name() + Venue.class.getName()), e.getMessage());
		}
	}

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-dataset/data.sql")
	public void create_WithNewVenue_Succeed() throws AlreadyExistsException, InvalidDataException {
		Venue venue = new Venue();
		venue.setNameEn("Bercy");
		venue.setNameFr("Bercy");
		venue.setCity(paris);

		Venue createdVenue = sut.create(venue, admin);
		assertNotNull(createdVenue.getId());
		assertEquals(venue, createdVenue);

		List<ReferenceLogEvent> log = sut.getLog(createdVenue.getId());
		assertEquals(1, log.size());
		assertEquals(LogEventType.CREATION, log.get(0).getType());
		assertEquals(createdVenue, log.get(0).getReference());
		assertEquals(admin, log.get(0).getAppUser());
		assertEquals(3, log.get(0).getChanges().size());
		int changeCount = 0;
		for (ReferenceLogEventChange change : log.get(0).getChanges()) {
			if (Field.NAME_EN.equals(change.getField())) {
				assertNull(change.getOldValue());
				assertEquals(createdVenue.getNameEn(), change.getNewValue());
				changeCount++;
			} else if (Field.NAME_FR.equals(change.getField())) {
				assertNull(change.getOldValue());
				assertEquals(createdVenue.getNameFr(), change.getNewValue());
				changeCount++;
			} else if (Field.CITY.equals(change.getField())) {
				assertNull(change.getOldValue());
				assertEquals(createdVenue.getCity().getLogValue(), change.getNewValue());
				changeCount++;
			}
		}
		assertEquals(log.get(0).getChanges().size(), changeCount);
	}

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-dataset/data.sql")
	public void create_WithNewVenueWithOnlyCityId_SucceedAndReturnWholeCity() throws AlreadyExistsException, InvalidDataException {
		Venue venue = new Venue();
		venue.setNameEn("Bercy");
		venue.setNameFr("Bercy");
		venue.setCity(new City());
		venue.getCity().setId(paris.getId());

		Venue createdVenue = sut.create(venue, admin);
		assertNotNull(createdVenue.getId());
		assertEquals(paris.getNameEn(), createdVenue.getCity().getNameEn());
		assertEquals(paris.getNameFr(), createdVenue.getCity().getNameFr());
	}

	@Test(expected = NullPointerException.class)
	public void edit_WithNullVenue_ThrowNullPointerException() throws Exception {
		sut.edit(null, new AppUser());
	}

	@Test(expected = NullPointerException.class)
	public void edit_WithNullCreator_ThrowNullPointerException() throws Exception {
		sut.edit(new Venue(), null);
	}

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-dataset/data.sql")
	public void edit_WithEnglishNameThatAlreadyExists_ThrowAlreadyExistsException() throws AlreadyExistsException {
		parcDesPrinces.setNameEn(stadeCharlety.getNameEn());

		try {
			sut.edit(parcDesPrinces, admin);
			fail("Edition should never succeed");
		} catch (Exception e) {
			assertTrue(e instanceof AlreadyExistsException);
			assertEquals(AlreadyExistsException.messages.get(Field.NAME_EN.name() + Venue.class.getName()), e.getMessage());
		}
	}

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-dataset/data.sql")
	public void edit_WithFrenchNameThatAlreadyExists_ThrowAlreadyExistsException() throws AlreadyExistsException {
		parcDesPrinces.setNameFr(stadeCharlety.getNameEn());

		try {
			sut.edit(parcDesPrinces, admin);
			fail("Edition should never succeed");
		} catch (Exception e) {
			assertTrue(e instanceof AlreadyExistsException);
			assertEquals(AlreadyExistsException.messages.get(Field.NAME_FR.name() + Venue.class.getName()), e.getMessage());
		}
	}

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-dataset/data.sql")
	public void edit_WithNewValues_Succeed() throws Exception {
		parcDesPrinces.setNameEn("Bercy");
		parcDesPrinces.setNameFr("Bercy");
		parcDesPrinces.setCity(lille);

		Venue editedVenue = sut.edit(parcDesPrinces, admin);
		assertEquals(new Long(4), editedVenue.getId());
		assertEquals(sut.get(parcDesPrinces.getId()), parcDesPrinces);

		List<ReferenceLogEvent> log = sut.getLog(editedVenue.getId());
		assertEquals(2, log.size());
		assertEquals(LogEventType.EDITION, log.get(0).getType());
		assertEquals(editedVenue, log.get(0).getReference());
		assertEquals(admin, log.get(0).getAppUser());
		assertEquals(3, log.get(0).getChanges().size());
		int changeCount = 0;
		for (ReferenceLogEventChange change : log.get(0).getChanges()) {
			if (Field.NAME_EN.equals(change.getField())) {
				assertEquals("Parc des Princes", change.getOldValue());
				assertEquals(editedVenue.getNameEn(), change.getNewValue());
				changeCount++;
			} else if (Field.NAME_FR.equals(change.getField())) {
				assertEquals("Parc des Princes", change.getOldValue());
				assertEquals(editedVenue.getNameFr(), change.getNewValue());
				changeCount++;
			} else if (Field.CITY.equals(change.getField())) {
				assertEquals("Paris / Paris", change.getOldValue());
				assertEquals(editedVenue.getCity().getLogValue(), change.getNewValue());
				changeCount++;
			}
		}
		assertEquals(log.get(0).getChanges().size(), changeCount);
	}

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-dataset/data.sql")
	public void edit_WithNewValuesButOnlyCityId_SucceedAndReturnWholeCity() throws Exception {
		parcDesPrinces.setNameEn("Bercy");
		parcDesPrinces.setNameFr("Bercy");
		parcDesPrinces.setCity(new City());
		parcDesPrinces.getCity().setId(lille.getId());

		Venue editedVenue = sut.edit(parcDesPrinces, admin);
		assertEquals(new Long(4), editedVenue.getId());
		assertEquals(sut.get(parcDesPrinces.getId()), parcDesPrinces);
		assertEquals(lille.getNameEn(), editedVenue.getCity().getNameEn());
		assertEquals(lille.getNameFr(), editedVenue.getCity().getNameFr());
	}
}