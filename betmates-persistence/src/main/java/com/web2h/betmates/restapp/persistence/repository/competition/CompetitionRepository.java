package com.web2h.betmates.restapp.persistence.repository.competition;

import org.springframework.data.repository.CrudRepository;

import com.web2h.betmates.restapp.model.entity.competition.Competition;

/**
 * Competition repository interface.
 * 
 * @author web2h
 */
public interface CompetitionRepository extends CrudRepository<Competition, Long> {

	/**
	 * Retrieves a competition by its English name
	 * 
	 * @param nameEn
	 *            The English name to look for
	 * @return The retrieved competition, null if none could be found
	 */
	Competition findByNameEn(String nameEn);

	/**
	 * Retrieves a competition by its French name
	 * 
	 * @param nameFr
	 *            The French name to look for
	 * @return The retrieved competition, null if none could be found
	 */
	Competition findByNameFr(String nameFr);
}