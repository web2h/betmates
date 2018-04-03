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
import com.web2h.betmates.restapp.model.entity.reference.City;
import com.web2h.betmates.restapp.model.entity.reference.log.ReferenceLogEvent;
import com.web2h.betmates.restapp.model.entity.reference.log.ReferenceLogEventChange;
import com.web2h.betmates.restapp.model.entity.user.AppUser;
import com.web2h.betmates.restapp.model.exception.AlreadyExistsException;
import com.web2h.betmates.restapp.model.exception.InvalidDataException;
import com.web2h.betmates.restapp.model.validation.Field;
import com.web2h.betmates.restapp.persistence.repository.user.AppUserRepository;

/**
 * City service test class.
 * 
 * @author web2h
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class CityServiceTest extends CommonTest {

	@Autowired
	private CityService sut;

	@Autowired
	private AppUserRepository appUserRepository;

	private AppUser admin;

	@Before
	public void setUp() {
		admin = appUserRepository.findOne(1l);
	}

	@Test(expected = NullPointerException.class)
	public void create_WithNullCity_ThrowNullPointerException() throws AlreadyExistsException, InvalidDataException {
		sut.create(null, new AppUser());
	}

	@Test(expected = NullPointerException.class)
	public void create_WithNullCreator_ThrowNullPointerException() throws AlreadyExistsException, InvalidDataException {
		sut.create(new City(), null);
	}

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-dataset/data.sql")
	public void create_WithExistingCity_ThrowAlreadyExistsException() throws AlreadyExistsException {
		City city = new City();
		city.setNameEn("Paris");
		city.setNameFr("Paris2");
		city.setCountry(france);

		try {
			sut.create(city, admin);
			fail("Creation should never succeed");
		} catch (Exception e) {
			assertTrue(e instanceof AlreadyExistsException);
			assertEquals(AlreadyExistsException.messages.get(Field.NAME_EN.name() + City.class.getName()), e.getMessage());
		}

		city = new City();
		city.setNameEn("Paris2");
		city.setNameFr("Paris");
		city.setCountry(france);

		try {
			sut.create(city, admin);
			fail("Creation should never succeed");
		} catch (Exception e) {
			assertTrue(e instanceof AlreadyExistsException);
			assertEquals(AlreadyExistsException.messages.get(Field.NAME_FR.name() + City.class.getName()), e.getMessage());
		}
	}

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-dataset/data.sql")
	public void create_WithNewCity_Succeed() throws AlreadyExistsException, InvalidDataException {
		City city = new City();
		city.setNameEn("Marseille");
		city.setNameFr("Marseille");
		city.setCountry(france);

		City createdCity = sut.create(city, admin);
		assertNotNull(createdCity.getId());
		assertEquals(city, createdCity);

		List<ReferenceLogEvent> log = sut.getLog(createdCity.getId());
		assertEquals(1, log.size());
		assertEquals(LogEventType.CREATION, log.get(0).getType());
		assertEquals(createdCity, log.get(0).getReference());
		assertEquals(admin, log.get(0).getAppUser());
		assertEquals(3, log.get(0).getChanges().size());
		int changeCount = 0;
		for (ReferenceLogEventChange change : log.get(0).getChanges()) {
			if (Field.NAME_EN.equals(change.getField())) {
				assertNull(change.getOldValue());
				assertEquals(createdCity.getNameEn(), change.getNewValue());
				changeCount++;
			} else if (Field.NAME_FR.equals(change.getField())) {
				assertNull(change.getOldValue());
				assertEquals(createdCity.getNameFr(), change.getNewValue());
				changeCount++;
			} else if (Field.COUNTRY.equals(change.getField())) {
				assertNull(change.getOldValue());
				assertEquals(createdCity.getCountry().getLogValue(), change.getNewValue());
				changeCount++;
			}
		}
		assertEquals(log.get(0).getChanges().size(), changeCount);
	}

	@Test(expected = NullPointerException.class)
	public void edit_WithNullCity_ThrowNullPointerException() throws Exception {
		sut.edit(null, new AppUser());
	}

	@Test(expected = NullPointerException.class)
	public void edit_WithNullCreator_ThrowNullPointerException() throws Exception {
		sut.edit(new City(), null);
	}

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-dataset/data.sql")
	public void edit_WithEnglishNameThatAlreadyExists_ThrowAlreadyExistsException() throws AlreadyExistsException {
		lille.setNameEn(paris.getNameEn());

		try {
			sut.edit(lille, admin);
			fail("Edition should never succeed");
		} catch (Exception e) {
			assertTrue(e instanceof AlreadyExistsException);
			assertEquals(AlreadyExistsException.messages.get(Field.NAME_EN.name() + City.class.getName()), e.getMessage());
		}
	}

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-dataset/data.sql")
	public void edit_WithFrenchNameThatAlreadyExists_ThrowAlreadyExistsException() throws AlreadyExistsException {
		lille.setNameFr(paris.getNameFr());

		try {
			sut.edit(lille, admin);
			fail("Edition should never succeed");
		} catch (Exception e) {
			assertTrue(e instanceof AlreadyExistsException);
			assertEquals(AlreadyExistsException.messages.get(Field.NAME_FR.name() + City.class.getName()), e.getMessage());
		}
	}

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-dataset/data.sql")
	public void edit_WithNewValues_Succeed() throws Exception {
		lille.setNameEn("Marseille");
		lille.setNameFr("Marseille");

		City editedCity = sut.edit(lille, admin);
		assertEquals(new Long(3), editedCity.getId());
		assertEquals(sut.get(lille.getId()), lille);

		List<ReferenceLogEvent> log = sut.getLog(editedCity.getId());
		assertEquals(2, log.size());
		assertEquals(LogEventType.EDITION, log.get(0).getType());
		assertEquals(editedCity, log.get(0).getReference());
		assertEquals(admin, log.get(0).getAppUser());
		assertEquals(2, log.get(0).getChanges().size());
		int changeCount = 0;
		for (ReferenceLogEventChange change : log.get(0).getChanges()) {
			if (Field.NAME_EN.equals(change.getField())) {
				assertEquals("Lille", change.getOldValue());
				assertEquals(editedCity.getNameEn(), change.getNewValue());
				changeCount++;
			} else if (Field.NAME_FR.equals(change.getField())) {
				assertEquals("Lille", change.getOldValue());
				assertEquals(editedCity.getNameFr(), change.getNewValue());
				changeCount++;
			}
		}
		assertEquals(log.get(0).getChanges().size(), changeCount);
	}
}