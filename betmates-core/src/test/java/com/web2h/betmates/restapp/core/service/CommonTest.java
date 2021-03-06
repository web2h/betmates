package com.web2h.betmates.restapp.core.service;

import java.util.Calendar;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import com.web2h.betmates.restapp.model.entity.competition.Competition;
import com.web2h.betmates.restapp.model.entity.competition.CompetitionGroup;
import com.web2h.betmates.restapp.model.entity.competition.CompetitionType;
import com.web2h.betmates.restapp.model.entity.reference.City;
import com.web2h.betmates.restapp.model.entity.reference.Country;
import com.web2h.betmates.restapp.model.entity.reference.Sport;
import com.web2h.betmates.restapp.model.entity.reference.Team;
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
	protected City boston;
	protected City chicago;
	protected City miami;

	protected Competition worldCup2018;
	protected Competition nbaPlayoffs2018;

	protected Country france;
	protected Country belgium;
	protected Country canada;
	protected Country usa;

	protected Team bostonCeltics;
	protected Team miamiHeat;
	protected Team orlandoMagic;
	protected Team chicagoBulls;
	protected Team unknownTeam;

	protected Venue parcDesPrinces;
	protected Venue stadeCharlety;
	protected Venue stadeDuRoiBaudouin;
	protected Venue tdGarden;
	protected Venue aaArena;
	protected Venue uCenter;
	protected Venue unknownVenue;

	@Before
	public void before() {
		admin = appUserRepository.findById(1l).get();

		createCountries();
		createCities();
		createVenues();

		createCompetitions();
		createTeams();
		addTeamsToCompetitions();
		addVenuesToCompetitions();
	}

	private void addTeamsToCompetitions() {
		nbaPlayoffs2018.addTeam(bostonCeltics, CompetitionGroup.EAST, 2);
		nbaPlayoffs2018.addTeam(miamiHeat, CompetitionGroup.EAST, 6);
	}

	private void addVenuesToCompetitions() {
		nbaPlayoffs2018.getVenues().add(aaArena);
		nbaPlayoffs2018.getVenues().add(tdGarden);
	}

	private void createCities() {
		paris = new City();
		paris.setId(11l);
		paris.setNameEn("Paris");
		paris.setNameFr("Paris");
		paris.setCountry(france);

		lille = new City();
		lille.setId(12l);
		lille.setNameEn("Lille");
		lille.setNameFr("Lille");
		lille.setCountry(france);

		brussels = new City();
		brussels.setId(13l);
		brussels.setNameEn("Brussels");
		brussels.setNameFr("Bruxelles");
		brussels.setCountry(belgium);

		boston = new City();
		boston.setId(14l);
		boston.setNameEn("Boston");
		boston.setNameFr("Boston");
		boston.setCountry(usa);

		chicago = new City();
		chicago.setId(15l);
		chicago.setNameEn("Chicago");
		chicago.setNameFr("Chicago");
		chicago.setCountry(usa);

		miami = new City();
		miami.setId(16l);
		miami.setNameEn("Miami");
		miami.setNameFr("Miami");
		miami.setCountry(usa);
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
		belgium.setId(2l);
		belgium.setNameEn("Belgium");
		belgium.setNameFr("Belgique");

		canada = new Country();
		canada.setId(3l);
		canada.setNameEn("Canada");
		canada.setNameFr("Canada");

		usa = new Country();
		usa.setId(4l);
		usa.setNameEn("USA");
		usa.setNameFr("EUA");
	}

	private void createTeams() {
		miamiHeat = new Team();
		miamiHeat.setId(31l);
		miamiHeat.setNameEn("Miami Heat");
		miamiHeat.setNameFr("Miami Heat");
		miamiHeat.setSport(Sport.BASKET_BALL);
		miamiHeat.setShortNameEn("Heat");
		miamiHeat.setShortNameFr("Heat");

		bostonCeltics = new Team();
		bostonCeltics.setId(32l);
		bostonCeltics.setNameEn("Boston Celtics");
		bostonCeltics.setNameFr("Boston Celtics");
		bostonCeltics.setSport(Sport.BASKET_BALL);
		bostonCeltics.setShortNameEn("Celtics");
		bostonCeltics.setShortNameFr("Celtics");

		orlandoMagic = new Team();
		orlandoMagic.setId(33l);
		orlandoMagic.setNameEn("Orlando Magic");
		orlandoMagic.setNameFr("Orlando Magic");
		orlandoMagic.setSport(Sport.BASKET_BALL);
		orlandoMagic.setShortNameEn("Magic");
		orlandoMagic.setShortNameFr("Magic");

		chicagoBulls = new Team();
		chicagoBulls.setId(34l);
		chicagoBulls.setNameEn("Chicago Bulls");
		chicagoBulls.setNameFr("Chicago Bulls");
		chicagoBulls.setSport(Sport.BASKET_BALL);
		chicagoBulls.setShortNameEn("Bulls");
		chicagoBulls.setShortNameFr("Bulls");

		unknownTeam = new Team();
		unknownTeam.setId(999l);
		unknownTeam.setNameEn("Las Vegas Unknowns");
		unknownTeam.setNameFr("Las Vegas Unknowns");
		unknownTeam.setSport(Sport.BASKET_BALL);
		unknownTeam.setShortNameEn("Unknowns");
		unknownTeam.setShortNameFr("Unknowns");
	}

	private void createVenues() {
		parcDesPrinces = new Venue();
		parcDesPrinces.setId(21l);
		parcDesPrinces.setNameEn("Parc des Princes");
		parcDesPrinces.setNameFr("Parc des Princes");
		parcDesPrinces.setCity(paris);

		stadeCharlety = new Venue();
		stadeCharlety.setId(22l);
		stadeCharlety.setNameEn("Stade Charléty");
		stadeCharlety.setNameFr("Stade Charléty");
		stadeCharlety.setCity(paris);

		stadeDuRoiBaudouin = new Venue();
		stadeDuRoiBaudouin.setId(23l);
		stadeDuRoiBaudouin.setNameEn("Stade du Roi Baudouin");
		stadeDuRoiBaudouin.setNameFr("Stade du Roi Baudouin");
		stadeDuRoiBaudouin.setCity(brussels);

		tdGarden = new Venue();
		tdGarden.setId(24l);
		tdGarden.setNameEn("TD Garden");
		tdGarden.setNameFr("TD Garden");
		tdGarden.setCity(boston);

		uCenter = new Venue();
		uCenter.setId(25l);
		uCenter.setNameEn("United Center");
		uCenter.setNameFr("United Center");
		uCenter.setCity(chicago);

		aaArena = new Venue();
		aaArena.setId(26l);
		aaArena.setNameEn("American Airlines Arena");
		aaArena.setNameFr("American Airlines Arena");
		aaArena.setCity(miami);

		unknownVenue = new Venue();
		unknownVenue.setId(9999l);
		unknownVenue.setNameEn("Unknown Stadium");
		unknownVenue.setNameFr("Stade Inconnu");
		unknownVenue.setCity(paris);
	}
}