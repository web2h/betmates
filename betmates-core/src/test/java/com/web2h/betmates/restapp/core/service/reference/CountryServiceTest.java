package com.web2h.betmates.restapp.core.service.reference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
import com.web2h.betmates.restapp.model.entity.log.LogEventType;
import com.web2h.betmates.restapp.model.entity.reference.Country;
import com.web2h.betmates.restapp.model.entity.reference.log.ReferenceLogEvent;
import com.web2h.betmates.restapp.model.entity.reference.log.ReferenceLogEventChange;
import com.web2h.betmates.restapp.model.entity.user.AppUser;
import com.web2h.betmates.restapp.model.exception.AlreadyExistsException;
import com.web2h.betmates.restapp.model.exception.InvalidDataException;
import com.web2h.betmates.restapp.model.validation.Field;
import com.web2h.betmates.restapp.persistence.repository.user.AppUserRepository;

/**
 * Country service test class.
 * 
 * @author web2h
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class CountryServiceTest extends CommonTest {

	@Autowired
	private CountryService sut;

	@Autowired
	private AppUserRepository appUserRepository;

	private AppUser admin;

	@Before
	public void setUp() {
		admin = appUserRepository.findOne(1l);
	}

	@Test(expected = NullPointerException.class)
	public void create_WithNullCountry_ThrowNullPointerException() throws AlreadyExistsException, InvalidDataException {
		sut.create(null, new AppUser());
	}

	@Test(expected = NullPointerException.class)
	public void create_WithNullCreator_ThrowNullPointerException() throws AlreadyExistsException, InvalidDataException {
		sut.create(new Country(), null);
	}

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-dataset/data.sql")
	public void create_WithExistingCountry_ThrowAlreadyExistsException() throws AlreadyExistsException {
		Country country = new Country();
		country.setNameEn("France");
		country.setNameFr("France2");

		try {
			sut.create(country, admin);
			fail("Creation should never succeed");
		} catch (Exception e) {
			assertTrue(e instanceof AlreadyExistsException);
			assertEquals(AlreadyExistsException.messages.get(Field.NAME_EN.name() + Country.class.getName()), e.getMessage());
		}

		country = new Country();
		country.setNameEn("France2");
		country.setNameFr("France");

		try {
			sut.create(country, admin);
			fail("Creation should never succeed");
		} catch (Exception e) {
			assertTrue(e instanceof AlreadyExistsException);
			assertEquals(AlreadyExistsException.messages.get(Field.NAME_FR.name() + Country.class.getName()), e.getMessage());
		}
	}

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-dataset/data.sql")
	public void create_WithNewCountry_Succeed() throws AlreadyExistsException, InvalidDataException {
		Country country = new Country();
		country.setNameEn("Russia");
		country.setNameFr("Russie");

		Country createdCountry = sut.create(country, admin);
		assertNotNull(createdCountry.getId());
		assertEquals(country, createdCountry);

		List<ReferenceLogEvent> log = sut.getLog(createdCountry.getId());
		assertEquals(1, log.size());
		assertEquals(LogEventType.CREATION, log.get(0).getType());
		assertEquals(createdCountry, log.get(0).getReference());
		assertEquals(admin, log.get(0).getAppUser());
		assertEquals(2, log.get(0).getChanges().size());
		int changeCount = 0;
		for (ReferenceLogEventChange change : log.get(0).getChanges()) {
			if (Field.NAME_EN.equals(change.getField())) {
				assertNull(change.getOldValue());
				assertEquals(createdCountry.getNameEn(), change.getNewValue());
				changeCount++;
			} else if (Field.NAME_FR.equals(change.getField())) {
				assertNull(change.getOldValue());
				assertEquals(createdCountry.getNameFr(), change.getNewValue());
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
		sut.edit(new Country(), null);
	}

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-dataset/data.sql")
	public void edit_WithEnglishNameThatAlreadyExists_ThrowAlreadyExistsException() throws AlreadyExistsException {
		france.setNameEn(belgium.getNameEn());

		try {
			sut.edit(france, admin);
			fail("Edition should never succeed");
		} catch (Exception e) {
			assertTrue(e instanceof AlreadyExistsException);
			assertEquals(AlreadyExistsException.messages.get(Field.NAME_EN.name() + Country.class.getName()), e.getMessage());
		}
	}

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-dataset/data.sql")
	public void edit_WithFrenchNameThatAlreadyExists_ThrowAlreadyExistsException() throws AlreadyExistsException {
		france.setNameFr(belgium.getNameFr());

		try {
			sut.edit(france, admin);
			fail("Edition should never succeed");
		} catch (Exception e) {
			assertTrue(e instanceof AlreadyExistsException);
			assertEquals(AlreadyExistsException.messages.get(Field.NAME_FR.name() + Country.class.getName()), e.getMessage());
		}
	}

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-dataset/data.sql")
	public void edit_WithNewValues_Succeed() throws Exception {
		france.setNameEn("Russia");
		france.setNameFr("Russie");

		Country editedCountry = sut.edit(france, admin);
		assertEquals(new Long(1), editedCountry.getId());
		assertEquals(sut.get(france.getId()), france);

		List<ReferenceLogEvent> log = sut.getLog(editedCountry.getId());
		assertEquals(2, log.size());
		assertEquals(LogEventType.EDITION, log.get(0).getType());
		assertEquals(editedCountry, log.get(0).getReference());
		assertEquals(admin, log.get(0).getAppUser());
		assertEquals(2, log.get(0).getChanges().size());
		int changeCount = 0;
		for (ReferenceLogEventChange change : log.get(0).getChanges()) {
			if (Field.NAME_EN.equals(change.getField())) {
				assertEquals("France", change.getOldValue());
				assertEquals(editedCountry.getNameEn(), change.getNewValue());
				changeCount++;
			} else if (Field.NAME_FR.equals(change.getField())) {
				assertEquals("France", change.getOldValue());
				assertEquals(editedCountry.getNameFr(), change.getNewValue());
				changeCount++;
			}
		}
		assertEquals(log.get(0).getChanges().size(), changeCount);
	}
}
