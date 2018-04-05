package com.web2h.betmates.restapp.persistence.repository.reference;

import com.web2h.betmates.restapp.model.entity.reference.Sport;
import com.web2h.betmates.restapp.model.entity.reference.Team;

/**
 * Team repository interface.
 * 
 * @author web2h
 */
public interface TeamRepository extends ReferenceRepository<Team> {

	/**
	 * Retrieves a team by its English name and sport
	 * 
	 * @param nameEn
	 *            The English name to look for
	 * @param sport
	 *            The sport to look for
	 * @return The retrieved city, null if none could be found
	 */
	Team findByNameEnAndSport(String nameEn, Sport sport);

	/**
	 * Retrieves a team by its French name and sport
	 * 
	 * @param nameFr
	 *            The French name to look for
	 * @param sport
	 *            The sport to look for
	 * @return The retrieved city, null if none could be found
	 */
	Team findByNameFrAndSport(String nameFr, Sport sport);
}