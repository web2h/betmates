package com.web2h.betmates.restapp.persistence.repository.reference;

import com.web2h.betmates.restapp.model.entity.reference.City;
import com.web2h.betmates.restapp.model.entity.reference.Venue;

/**
 * Venue repository interface.
 * 
 * @author web2h
 */
public interface VenueRepository extends ReferenceRepository<Venue> {

	/**
	 * Retrieves a venue by its English name and city
	 * 
	 * @param nameEn
	 *            The English name to look for
	 * @param city
	 *            The city to look for
	 * @return The retrieved venue, null if none could be found
	 */
	Venue findByNameEnAndCity(String nameEn, City city);

	/**
	 * Retrieves a venue by its French name and city
	 * 
	 * @param nameFr
	 *            The French name to look for
	 * @param city
	 *            The city to look for
	 * @return The retrieved venue, null if none could be found
	 */
	Venue findByNameFrAndCity(String nameFr, City city);
}
