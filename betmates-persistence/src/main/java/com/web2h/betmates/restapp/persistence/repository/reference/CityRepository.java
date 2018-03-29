package com.web2h.betmates.restapp.persistence.repository.reference;

import com.web2h.betmates.restapp.model.entity.reference.City;
import com.web2h.betmates.restapp.model.entity.reference.Country;

/**
 * City repository interface.
 * 
 * @author web2h
 */
public interface CityRepository extends ReferenceRepository<City> {

	/**
	 * Retrieves a city by its English name and country
	 * 
	 * @param nameEn
	 *            The English name to look for
	 * @param country
	 *            The country to look for
	 * @return The retrieved city, null if none could be found
	 */
	City findByNameEnAndCountry(String nameEn, Country country);

	/**
	 * Retrieves a city by its French name and country
	 * 
	 * @param nameFr
	 *            The French name to look for
	 * @param country
	 *            The country to look for
	 * @return The retrieved city, null if none could be found
	 */
	City findByNameFrAndCountry(String nameFr, Country country);
}