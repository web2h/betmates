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
import com.web2h.betmates.restapp.model.entity.reference.City;
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

		// TODO Check logs

		City createdCity = sut.create(city, admin);
		assertNotNull(createdCity.getId());
		assertEquals(city, createdCity);
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

		// TODO Check logs

		City editedCity = sut.edit(lille, admin);
		assertEquals(new Long(3), editedCity.getId());
		assertEquals(sut.get(lille.getId()), lille);
	}
}