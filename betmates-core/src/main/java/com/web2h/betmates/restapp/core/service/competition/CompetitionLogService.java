package com.web2h.betmates.restapp.core.service.competition;

import java.util.List;

import com.web2h.betmates.restapp.core.service.competition.helper.AddedAndRemovedTeams;
import com.web2h.betmates.restapp.core.service.competition.helper.AddedAndRemovedVenues;
import com.web2h.betmates.restapp.model.entity.competition.Competition;
import com.web2h.betmates.restapp.model.entity.competition.log.CompetitionLogEvent;
import com.web2h.betmates.restapp.model.entity.user.AppUser;

/**
 * Log service interface for events on competitions.
 * 
 * @author web2h
 */
public interface CompetitionLogService {

	/**
	 * Gets the event log for the given competition.
	 * 
	 * @param competitionId
	 *            Id of the competition we want to get the log for
	 * @return The list of log events
	 */
	List<CompetitionLogEvent> getLog(Long competitionId);

	/**
	 * Logs a competition creation.
	 * 
	 * @param competition
	 *            The created competition
	 * @param creator
	 *            The user who created the competition
	 */
	void logCreation(Competition competition, AppUser creator);

	/**
	 * Logs a competition edition.
	 * 
	 * @param oldCompetition
	 *            The current competition
	 * @param newCompetition
	 *            The new competition values
	 * @param editor
	 *            The user who edited the competition
	 */
	void logEdition(Competition oldCompetition, Competition newCompetition, AppUser editor);

	void logTeamAdditionOrRemoval(Competition competition, AddedAndRemovedTeams addedAndRemovedTeams, AppUser editor);

	void logVenueAdditionOrRemoval(Competition competition, AddedAndRemovedVenues addedAndRemovedVenues, AppUser editor);
}
