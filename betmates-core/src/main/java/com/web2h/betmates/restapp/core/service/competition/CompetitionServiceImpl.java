package com.web2h.betmates.restapp.core.service.competition;

import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.web2h.betmates.restapp.core.service.competition.helper.AddedAndRemovedTeams;
import com.web2h.betmates.restapp.model.entity.competition.Competition;
import com.web2h.betmates.restapp.model.entity.competition.log.CompetitionLogEvent;
import com.web2h.betmates.restapp.model.entity.reference.Team;
import com.web2h.betmates.restapp.model.entity.user.AppUser;
import com.web2h.betmates.restapp.model.exception.AlreadyExistsException;
import com.web2h.betmates.restapp.model.exception.NotFoundException;
import com.web2h.betmates.restapp.model.validation.Field;
import com.web2h.betmates.restapp.persistence.repository.competition.CompetitionRepository;

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

	private CompetitionLogService competitionLogService;

	public CompetitionServiceImpl(CompetitionRepository competitionRepository, CompetitionLogService competitionLogService) {
		this.competitionRepository = competitionRepository;
		this.competitionLogService = competitionLogService;
	}

	@Override
	public Competition addOrRemoveTeams(Competition competition, AppUser editor) throws NotFoundException {
		Preconditions.checkNotNull(competition);
		Preconditions.checkNotNull(editor);

		Competition currentCompetition = competitionRepository.findOne(competition.getId());
		if (currentCompetition == null) {
			throw new NotFoundException(Field.ID, Competition.class.getName());
		}

		AddedAndRemovedTeams addedAndRemovedTeams = mergeTeams(currentCompetition, competition);
		competitionLogService.LogTeamAdditionOrRemoval(currentCompetition, addedAndRemovedTeams, editor);

		competitionRepository.save(currentCompetition);
		return currentCompetition;
	}

	@Override
	public Competition create(Competition competition, AppUser creator) throws AlreadyExistsException {
		Preconditions.checkNotNull(competition);
		Preconditions.checkNotNull(creator);

		checkIfExists(competition);

		competitionLogService.logCreation(competition, creator);

		competitionRepository.save(competition);
		return competition;
	}

	@Override
	public Competition edit(Competition competition, AppUser editor) throws NotFoundException, AlreadyExistsException {
		Preconditions.checkNotNull(competition);
		Preconditions.checkNotNull(editor);

		Competition currentCompetition = competitionRepository.findOne(competition.getId());
		if (currentCompetition == null) {
			throw new NotFoundException(Field.ID, Competition.class.getName());
		}

		checkIfExists(competition);

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

	/**
	 * Checks if the competition already exists to prevent inserting duplicates.
	 * 
	 * @param competition
	 *            The competition to check
	 * @throws AlreadyExistsException
	 *             When the competition already exists
	 */
	private void checkIfExists(Competition competition) throws AlreadyExistsException {
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
		Iterator<Team> currentTeamsIterator = currentCompetition.getTeams().iterator();
		while (currentTeamsIterator.hasNext()) {
			Team currentTeam = currentTeamsIterator.next();
			if (!newCompetition.getTeams().contains(currentTeam)) {
				logger.info("The team " + currentTeam.getLogValue() + " has been removed from the competition " + currentCompetition.getLogValue());
				currentTeamsIterator.remove();
				addedAndRemovedTeams.getRemovedTeams().add(currentTeam);
			}
		}

		// New teams to add
		for (Team newTeam : newCompetition.getTeams()) {
			if (!currentCompetition.getTeams().contains(newTeam)) {
				logger.info("The team " + newTeam.getLogValue() + " has been added to the competition " + currentCompetition.getLogValue());
				currentCompetition.getTeams().add(newTeam);
				addedAndRemovedTeams.getAddedTeams().add(newTeam);
			}
		}

		return addedAndRemovedTeams;
	}
}