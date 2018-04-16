package com.web2h.betmates.restapp.core.service.competition;

import java.util.List;

import com.web2h.betmates.restapp.model.entity.competition.Competition;
import com.web2h.betmates.restapp.model.entity.competition.log.CompetitionLogEvent;
import com.web2h.betmates.restapp.model.entity.user.AppUser;
import com.web2h.betmates.restapp.model.exception.AlreadyExistsException;
import com.web2h.betmates.restapp.model.exception.InvalidDataException;
import com.web2h.betmates.restapp.model.exception.NotFoundException;

/**
 * Competition service interface.
 * 
 * @author web2h
 */
public interface CompetitionService {

	Competition addOrRemoveTeams(Competition competition, AppUser editor) throws NotFoundException, InvalidDataException;

	Competition addOrRemoveVenues(Competition competition, AppUser editor) throws NotFoundException, InvalidDataException;

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

	/**
	 * Gets a competition.
	 * 
	 * @param competitionId
	 *            Id of the competition to get.
	 * @return The retrieved competition, null if none could be found
	 */
	Competition get(Long competitionId);

	/**
	 * Gets the event log for the given competition.
	 * 
	 * @param competitionId
	 *            Id of the competition we want to get the log for
	 * @return The list of log events
	 */
	List<CompetitionLogEvent> getLog(Long competitionId);
}