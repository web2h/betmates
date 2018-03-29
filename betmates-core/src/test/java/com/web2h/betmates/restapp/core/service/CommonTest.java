package com.web2h.betmates.restapp.core.service;

import org.junit.Before;

import com.web2h.betmates.restapp.model.entity.reference.City;
import com.web2h.betmates.restapp.model.entity.reference.Country;
import com.web2h.betmates.restapp.model.entity.reference.Venue;

public class CommonTest {

	protected City paris;
	protected City lille;
	protected City brussels;

	protected Country france;
	protected Country belgium;

	protected Venue parcDesPrinces;
	protected Venue stadeCharlety;
	protected Venue stadeDuRoiBaudouin;

	@Before
	public void before() {
		createCountries();
		createCities();
		createVenues();
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