package com.web2h.betmates.restapp.core.service.competition;

import com.web2h.betmates.restapp.model.entity.competition.Competition;
import com.web2h.betmates.restapp.model.entity.user.AppUser;
import com.web2h.betmates.restapp.model.exception.AlreadyExistsException;
import com.web2h.betmates.restapp.model.exception.NotFoundException;

/**
 * Competition service interface.
 * 
 * @author web2h
 */
public interface CompetitionService {

	/**
	 * Creates a new competition.
	 * 
	 * @param competition
	 *            The competition to create
	 * @param creator
	 *            The user who requested the creation
	 * @return The created competition
	 * @throws AlreadyExistsException
	 *             When the competition already exists
	 */
	Competition create(Competition competition, AppUser creator) throws AlreadyExistsException;

	/**
	 * Edits an existing competition.
	 * 
	 * @param competition
	 *            The competition to edit
	 * @param editor
	 *            The user who requested the edition
	 * @return The edited competition
	 * @throws NotFoundException
	 *             When the competition to edit does not exist
	 * @throws AlreadyExistsException
	 *             When the new values are already used by another competition
	 */
	Competition edit(Competition competition, AppUser editor) throws NotFoundException, AlreadyExistsException;
}