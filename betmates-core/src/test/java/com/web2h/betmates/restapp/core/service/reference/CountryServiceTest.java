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
import com.web2h.betmates.restapp.model.entity.reference.Country;
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

		// TODO Check logs

		Country createdCountry = sut.create(country, admin);
		assertNotNull(createdCountry.getId());
		assertEquals(country, createdCountry);
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

		// TODO Check logs

		Country editedCountry = sut.edit(france, admin);
		assertEquals(new Long(1), editedCountry.getId());
		assertEquals(sut.get(france.getId()), france);
	}
}
