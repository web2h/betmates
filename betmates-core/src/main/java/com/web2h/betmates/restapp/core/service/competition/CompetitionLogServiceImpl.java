package com.web2h.betmates.restapp.core.service.competition;

import java.util.List;

import org.springframework.stereotype.Service;

import com.web2h.betmates.restapp.core.service.competition.helper.AddedAndRemovedTeams;
import com.web2h.betmates.restapp.core.service.competition.helper.AddedAndRemovedVenues;
import com.web2h.betmates.restapp.model.entity.competition.Competition;
import com.web2h.betmates.restapp.model.entity.competition.log.CompetitionLogEvent;
import com.web2h.betmates.restapp.model.entity.competition.log.CompetitionLogEventChange;
import com.web2h.betmates.restapp.model.entity.log.LogEventType;
import com.web2h.betmates.restapp.model.entity.reference.Team;
import com.web2h.betmates.restapp.model.entity.reference.Venue;
import com.web2h.betmates.restapp.model.entity.user.AppUser;
import com.web2h.betmates.restapp.model.validation.Field;
import com.web2h.betmates.restapp.persistence.repository.competition.CompetitionLogEventRepository;
import com.web2h.tools.DateTools;

/**
 * Log service implementation class for events on competitions.
 * 
 * @author web2h
 */
@Service
public class CompetitionLogServiceImpl implements CompetitionLogService {

	private CompetitionLogEventRepository competitionLogEventRepository;

	public CompetitionLogServiceImpl(CompetitionLogEventRepository competitionLogEventRepository) {
		this.competitionLogEventRepository = competitionLogEventRepository;
	}

	@Override
	public List<CompetitionLogEvent> getLog(Long competitionId) {
		return competitionLogEventRepository.findByCompetition_idOrderByTimestampDesc(competitionId);
	}

	@Override
	public void logCreation(Competition competition, AppUser creator) {
		CompetitionLogEvent event = new CompetitionLogEvent(competition, LogEventType.CREATION, creator);

		event.getChanges().add(new CompetitionLogEventChange(event, Field.NAME_EN, competition.getNameEn()));
		event.getChanges().add(new CompetitionLogEventChange(event, Field.NAME_FR, competition.getNameFr()));
		event.getChanges().add(new CompetitionLogEventChange(event, Field.TYPE, competition.getType().toString()));
		event.getChanges().add(new CompetitionLogEventChange(event, Field.START_DATE, DateTools.toString(competition.getStartDate())));

		competitionLogEventRepository.save(event);
	}

	@Override
	public void logEdition(Competition oldCompetition, Competition newCompetition, AppUser editor) {
		CompetitionLogEvent event = new CompetitionLogEvent(oldCompetition, LogEventType.EDITION, editor);

		if (!oldCompetition.getNameEn().equals(newCompetition.getNameEn())) {
			event.getChanges().add(new CompetitionLogEventChange(event, Field.NAME_EN, newCompetition.getNameEn(), oldCompetition.getNameEn()));
		}
		if (!oldCompetition.getNameFr().equals(newCompetition.getNameFr())) {
			event.getChanges().add(new CompetitionLogEventChange(event, Field.NAME_FR, newCompetition.getNameFr(), oldCompetition.getNameFr()));
		}
		if (!oldCompetition.getType().equals(newCompetition.getType())) {
			event.getChanges().add(new CompetitionLogEventChange(event, Field.TYPE, newCompetition.getType().toString(), oldCompetition.getType().toString()));
		}
		if (!oldCompetition.getStartDate().equals(newCompetition.getStartDate())) {
			event.getChanges().add(new CompetitionLogEventChange(event, Field.START_DATE, DateTools.toString(newCompetition.getStartDate()), DateTools.toString(oldCompetition.getStartDate())));
		}

		competitionLogEventRepository.save(event);
	}

	@Override
	public void logTeamAdditionOrRemoval(Competition competition, AddedAndRemovedTeams addedAndRemovedTeams, AppUser editor) {
		for (Team removedTeam : addedAndRemovedTeams.getRemovedTeams()) {
			CompetitionLogEvent event = new CompetitionLogEvent(competition, LogEventType.TEAM_REMOVAL, editor);
			event.setDescription("Team " + removedTeam.getLogValue() + " has been removed");
			competitionLogEventRepository.save(event);
		}

		for (Team addedTeam : addedAndRemovedTeams.getAddedTeams()) {
			CompetitionLogEvent event = new CompetitionLogEvent(competition, LogEventType.TEAM_ADDITION, editor);
			event.setDescription("Team " + addedTeam.getLogValue() + " has been added");
			competitionLogEventRepository.save(event);
		}
	}

	@Override
	public void logVenueAdditionOrRemoval(Competition competition, AddedAndRemovedVenues addedAndRemovedVenues, AppUser editor) {
		for (Venue removedVenue : addedAndRemovedVenues.getRemovedVenues()) {
			CompetitionLogEvent event = new CompetitionLogEvent(competition, LogEventType.VENUE_REMOVAL, editor);
			event.setDescription("Venue " + removedVenue.getLogValue() + " has been removed");
			competitionLogEventRepository.save(event);
		}

		for (Venue addedVenue : addedAndRemovedVenues.getAddedVenues()) {
			CompetitionLogEvent event = new CompetitionLogEvent(competition, LogEventType.VENUE_ADDITION, editor);
			event.setDescription("Venue " + addedVenue.getLogValue() + " has been added");
			competitionLogEventRepository.save(event);
		}
	}
}