package com.web2h.betmates.restapp.core.service.competition;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.web2h.betmates.restapp.model.entity.competition.Competition;
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

	private CompetitionRepository competitionRepository;

	public CompetitionServiceImpl(CompetitionRepository competitionRepository) {
		this.competitionRepository = competitionRepository;
	}

	@Override
	public Competition create(Competition competition, AppUser creator) throws AlreadyExistsException {
		Preconditions.checkNotNull(competition);
		Preconditions.checkNotNull(creator);

		checkIfExists(competition);

		// TODO log creation

		competitionRepository.save(competition);
		return competition;
	}

	@Override
	public Competition edit(Competition competition, AppUser editor) throws NotFoundException, AlreadyExistsException {
		Preconditions.checkNotNull(competition);
		Preconditions.checkNotNull(editor);

		Competition existingCompetition = competitionRepository.findOne(competition.getId());
		if (existingCompetition == null) {
			throw new NotFoundException(Field.ID, Competition.class.getName());
		}

		checkIfExists(competition);

		// TODO log edition

		merge(existingCompetition, competition);
		competitionRepository.save(competition);
		return competition;
	}

	@Override
	public Competition get(Long competitionId) {
		return competitionRepository.findOne(competitionId);
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
	 * @param existingCompetition
	 *            The existing competition in DB before the edition
	 * @param newCompetition
	 *            The new competition values
	 */
	public void merge(Competition existingCompetition, Competition newCompetition) {
		existingCompetition.setNameEn(newCompetition.getNameEn());
		existingCompetition.setNameFr(newCompetition.getNameFr());
		existingCompetition.setType(newCompetition.getType());
		existingCompetition.setStartDate(newCompetition.getStartDate());
	}
}