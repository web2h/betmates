package com.web2h.betmates.restapp.core.service.reference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;

import com.web2h.betmates.restapp.core.service.CommonTest;
import com.web2h.betmates.restapp.model.entity.reference.Venue;
import com.web2h.betmates.restapp.model.entity.user.AppUser;
import com.web2h.betmates.restapp.model.exception.AlreadyExistsException;
import com.web2h.betmates.restapp.model.exception.InvalidDataException;
import com.web2h.betmates.restapp.model.validation.Field;
import com.web2h.betmates.restapp.persistence.repository.user.AppUserRepository;

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

	@Autowired
	private AppUserRepository appUserRepository;

	private AppUser admin;

	@Before
	public void setUp() {
		admin = appUserRepository.findOne(1l);
	}

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
	public void create_WithExistingVenue_ThrowAlreadyExistsException() throws AlreadyExistsException {
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

		venue.setNameEn("Parc des Princes2");
		venue.setNameFr("Parc des Princes");

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

		// TODO Check logs

		Venue createdVenue = sut.create(venue, admin);
		assertNotNull(createdVenue.getId());
		assertEquals(venue, createdVenue);
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

		// TODO Check logs

		Venue editedVenue = sut.edit(parcDesPrinces, admin);
		assertEquals(new Long(4), editedVenue.getId());
		assertEquals(sut.get(parcDesPrinces.getId()), parcDesPrinces);
	}
}