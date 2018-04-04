package com.web2h.betmates.restapp.core.service;

import java.util.Calendar;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import com.web2h.betmates.restapp.model.entity.competition.Competition;
import com.web2h.betmates.restapp.model.entity.competition.CompetitionType;
import com.web2h.betmates.restapp.model.entity.reference.City;
import com.web2h.betmates.restapp.model.entity.reference.Country;
import com.web2h.betmates.restapp.model.entity.reference.Venue;
import com.web2h.betmates.restapp.model.entity.user.AppUser;
import com.web2h.betmates.restapp.persistence.repository.user.AppUserRepository;

public class CommonTest {

	@Autowired
	private AppUserRepository appUserRepository;

	protected AppUser admin;

	protected City paris;
	protected City lille;
	protected City brussels;

	protected Competition worldCup2018;
	protected Competition nbaPlayoffs2018;

	protected Country france;
	protected Country belgium;

	protected Venue parcDesPrinces;
	protected Venue stadeCharlety;
	protected Venue stadeDuRoiBaudouin;

	@Before
	public void before() {
		admin = appUserRepository.findOne(1l);

		createCountries();
		createCities();
		createVenues();

		createCompetitions();
	}

	private void createCities() {
		paris = new City();
		paris.setId(2l);
		paris.setNameEn("Paris");
		paris.setNameFr("Paris");
		paris.setCountry(france);

		lille = new City();
		lille.setId(3l);
		lille.setNameEn("Lille");
		lille.setNameFr("Lille");
		lille.setCountry(france);

		brussels = new City();
		brussels.setId(7l);
		brussels.setNameEn("Brussels");
		brussels.setNameFr("Bruxelles");
		brussels.setCountry(belgium);
	}

	private void createCompetitions() {
		worldCup2018 = new Competition();
		worldCup2018.setId(1l);
		worldCup2018.setNameEn("Russia 2018");
		worldCup2018.setNameFr("Russie 2018");
		worldCup2018.setType(CompetitionType.FIFA_WORLD_CUP);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2018);
		calendar.set(Calendar.MONTH, Calendar.JUNE);
		calendar.set(Calendar.DAY_OF_MONTH, 10);
		worldCup2018.setStartDate(calendar.getTime());

		nbaPlayoffs2018 = new Competition();
		nbaPlayoffs2018.setId(2l);
		nbaPlayoffs2018.setNameEn("NBA Playoffs 2018");
		nbaPlayoffs2018.setNameFr("NBA Playoffs 2018");
		nbaPlayoffs2018.setType(CompetitionType.NBA_PLAYOFFS);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.set(Calendar.YEAR, 2018);
		calendar2.set(Calendar.MONTH, Calendar.APRIL);
		calendar2.set(Calendar.DAY_OF_MONTH, 15);
		nbaPlayoffs2018.setStartDate(calendar.getTime());
	}

	private void createCountries() {
		france = new Country();
		france.setId(1l);
		france.setNameEn("France");
		france.setNameFr("France");

		belgium = new Country();
		belgium.setId(6l);
		belgium.setNameEn("Belgium");
		belgium.setNameFr("Belgique");
	}

	private void createVenues() {
		parcDesPrinces = new Venue();
		parcDesPrinces.setId(4l);
		parcDesPrinces.setNameEn("Parc des Princes");
		parcDesPrinces.setNameFr("Parc des Princes");
		parcDesPrinces.setCity(paris);

		stadeCharlety = new Venue();
		stadeCharlety.setId(5l);
		stadeCharlety.setNameEn("Stade Charléty");
		stadeCharlety.setNameFr("Stade Charléty");
		stadeCharlety.setCity(paris);

		stadeDuRoiBaudouin = new Venue();
		stadeDuRoiBaudouin.setId(8l);
		stadeDuRoiBaudouin.setNameEn("Stade du Roi Baudouin");
		stadeDuRoiBaudouin.setNameFr("Stade du Roi Baudouin");
		stadeDuRoiBaudouin.setCity(brussels);
	}
}