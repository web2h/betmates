package com.web2h.betmates.restapp.core.service.competition;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.web2h.betmates.restapp.core.service.competition.helper.AddedAndRemovedTeams;
import com.web2h.betmates.restapp.core.service.competition.helper.AddedAndRemovedVenues;
import com.web2h.betmates.restapp.model.entity.competition.Competition;
import com.web2h.betmates.restapp.model.entity.competition.CompetitionTeam;
import com.web2h.betmates.restapp.model.entity.competition.log.CompetitionLogEvent;
import com.web2h.betmates.restapp.model.entity.reference.Venue;
import com.web2h.betmates.restapp.model.entity.user.AppUser;
import com.web2h.betmates.restapp.model.exception.AlreadyExistsException;
import com.web2h.betmates.restapp.model.exception.InvalidDataException;
import com.web2h.betmates.restapp.model.exception.NotFoundException;
import com.web2h.betmates.restapp.model.validation.Field;
import com.web2h.betmates.restapp.persistence.repository.competition.CompetitionRepository;
import com.web2h.betmates.restapp.persistence.repository.reference.TeamRepository;
import com.web2h.betmates.restapp.persistence.repository.reference.VenueRepository;

/**
 * Competition service implementation class.
 * 
 * @author web2h
 */
@Service
@Transactional
public class CompetitionServiceImpl implements CompetitionService {

	private Logger logger = LoggerFactory.getLogger(CompetitionServiceImpl.class);

	private CompetitionRepository competitionRepository;

	private TeamRepository teamRepository;

	private VenueRepository venueRepository;

	private CompetitionLogService competitionLogService;

	public CompetitionServiceImpl(CompetitionRepository competitionRepository,
			TeamRepository teamRepository,
			VenueRepository venueRepository,
			CompetitionLogService competitionLogService) {
		this.competitionRepository = competitionRepository;
		this.teamRepository = teamRepository;
		this.venueRepository = venueRepository;
		this.competitionLogService = competitionLogService;
	}

	@Override
	public Competition addOrRemoveTeams(Competition competition, AppUser editor) throws NotFoundException, InvalidDataException {
		Preconditions.checkNotNull(competition);
		Preconditions.checkNotNull(editor);

		Competition currentCompetition = checkIfCompetitionExists(competition);
		checkIfTeamsExist(competition.getTeams());

		AddedAndRemovedTeams addedAndRemovedTeams = mergeTeams(currentCompetition, competition);
		competitionLogService.logTeamAdditionOrRemoval(currentCompetition, addedAndRemovedTeams, editor);

		competitionRepository.save(currentCompetition);
		return currentCompetition;
	}

	@Override
	public Competition addOrRemoveVenues(Competition competition, AppUser editor) throws NotFoundException, InvalidDataException {
		Preconditions.checkNotNull(competition);
		Preconditions.checkNotNull(editor);

		Competition currentCompetition = checkIfCompetitionExists(competition);
		checkIfVenuesExist(competition.getVenues());

		AddedAndRemovedVenues addedAndRemovedVenues = mergeVenues(currentCompetition, competition);
		competitionLogService.logVenueAdditionOrRemoval(currentCompetition, addedAndRemovedVenues, editor);

		competitionRepository.save(currentCompetition);
		return currentCompetition;
	}

	@Override
	public Competition create(Competition competition, AppUser creator) throws AlreadyExistsException {
		Preconditions.checkNotNull(competition);
		Preconditions.checkNotNull(creator);

		checkIfExistWithSameNames(competition);

		competitionLogService.logCreation(competition, creator);

		competitionRepository.save(competition);
		return competition;
	}

	@Override
	public Competition edit(Competition competition, AppUser editor) throws NotFoundException, AlreadyExistsException {
		Preconditions.checkNotNull(competition);
		Preconditions.checkNotNull(editor);

		Competition currentCompetition = checkIfCompetitionExists(competition);
		checkIfExistWithSameNames(competition);

		competitionLogService.logEdition(currentCompetition, competition, editor);

		merge(currentCompetition, competition);
		competitionRepository.save(competition);
		return competition;
	}

	@Override
	public Competition get(Long competitionId) {
		return competitionRepository.findOne(competitionId);
	}

	@Override
	public List<CompetitionLogEvent> getLog(Long competitionId) {
		return competitionLogService.getLog(competitionId);
	}

	private Competition checkIfCompetitionExists(Competition competition) throws NotFoundException {
		Competition currentCompetition = competitionRepository.findOne(competition.getId());
		if (currentCompetition == null) {
			throw new NotFoundException(Field.ID, Competition.class.getName());
		}
		return currentCompetition;
	}

	/**
	 * Checks if the competition already exists to prevent inserting duplicates.
	 * 
	 * @param competition
	 *            The competition to check
	 * @throws AlreadyExistsException
	 *             When the competition already exists
	 */
	private void checkIfExistWithSameNames(Competition competition) throws AlreadyExistsException {
		Competition existingCompetition = competitionRepository.findByNameEn(competition.getNameEn());
		if (existingCompetition != null && (competition.isBeingCreated() || !competition.getId().equals(existingCompetition.getId()))) {
			throw new AlreadyExistsException(Field.NAME_EN, Competition.class.getName());
		}
		existingCompetition = competitionRepository.findByNameFr(competition.getNameFr());
		if (existingCompetition != null && (competition.isBeingCreated() || !competition.getId().equals(existingCompetition.getId()))) {
			throw new AlreadyExistsException(Field.NAME_FR, Competition.class.getName());
		}
	}

	/**
	 * Merges competitions.
	 * 
	 * @param currentCompetition
	 *            The existing competition in DB before the edition
	 * @param newCompetition
	 *            The new competition values
	 */
	public void merge(Competition currentCompetition, Competition newCompetition) {
		currentCompetition.setNameEn(newCompetition.getNameEn());
		currentCompetition.setNameFr(newCompetition.getNameFr());
		currentCompetition.setType(newCompetition.getType());
		currentCompetition.setStartDate(newCompetition.getStartDate());
	}

	public AddedAndRemovedTeams mergeTeams(Competition currentCompetition, Competition newCompetition) {
		AddedAndRemovedTeams addedAndRemovedTeams = new AddedAndRemovedTeams();

		// Teams removed from the competition
		Iterator<CompetitionTeam> currentTeamsIterator = currentCompetition.getTeams().iterator();
		while (currentTeamsIterator.hasNext()) {
			CompetitionTeam currentTeam = currentTeamsIterator.next();
			if (!newCompetition.getTeams().contains(currentTeam)) {
				logger.info("The team " + currentTeam.getTeam().getLogValue() + " has been removed from the competition " + currentCompetition.getLogValue());
				currentTeamsIterator.remove();
				addedAndRemovedTeams.getRemovedTeams().add(currentTeam);
			}
		}

		// New teams to add
		for (CompetitionTeam newTeam : newCompetition.getTeams()) {
			if (!currentCompetition.getTeams().contains(newTeam)) {
				logger.info("The team " + newTeam.getTeam().getLogValue() + " has been added to the competition " + currentCompetition.getLogValue());
				currentCompetition.getTeams().add(newTeam);
				addedAndRemovedTeams.getAddedTeams().add(newTeam);
			}
		}

		return addedAndRemovedTeams;
	}

	public AddedAndRemovedVenues mergeVenues(Competition currentCompetition, Competition newCompetition) {
		AddedAndRemovedVenues addedAndRemovedVenues = new AddedAndRemovedVenues();

		// Venues removed from the competition
		Iterator<Venue> currentVenuesIterator = currentCompetition.getVenues().iterator();
		while (currentVenuesIterator.hasNext()) {
			Venue currentVenue = currentVenuesIterator.next();
			if (!newCompetition.getVenues().contains(currentVenue)) {
				logger.info("The venue " + currentVenue.getLogValue() + " has been removed from the competition " + currentCompetition.getLogValue());
				currentVenuesIterator.remove();
				addedAndRemovedVenues.getRemovedVenues().add(currentVenue);
			}
		}

		// New venues to add
		for (Venue newVenue : newCompetition.getVenues()) {
			if (!currentCompetition.getVenues().contains(newVenue)) {
				logger.info("The venue " + newVenue.getLogValue() + " has been added to the competition " + currentCompetition.getLogValue());
				currentCompetition.getVenues().add(newVenue);
				addedAndRemovedVenues.getAddedVenues().add(newVenue);
			}
		}

		return addedAndRemovedVenues;
	}

	private void checkIfTeamsExist(Set<CompetitionTeam> teams) throws InvalidDataException {
		if (teams == null || teams.isEmpty()) {
			return;
		}
		Set<CompetitionTeam> notFoundTeams = teams.stream()
				.filter(team -> teamRepository.findOne(team.getTeam().getId()) == null)
				.collect(Collectors.toSet());
		if (!notFoundTeams.isEmpty()) {
			throw InvalidDataException.createWithUnfoundTeams(notFoundTeams);
		}
	}

	private void checkIfVenuesExist(Set<Venue> venues) throws InvalidDataException {
		if (venues == null || venues.isEmpty()) {
			return;
		}
		Set<Venue> notFoundVenues = venues.stream()
				.filter(venue -> venueRepository.findOne(venue.getId()) == null)
				.collect(Collectors.toSet());
		if (!notFoundVenues.isEmpty()) {
			throw InvalidDataException.createWithUnfoundVenues(notFoundVenues);
		}
	}
}