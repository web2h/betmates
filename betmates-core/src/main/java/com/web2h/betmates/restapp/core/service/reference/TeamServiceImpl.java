package com.web2h.betmates.restapp.core.service.reference;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web2h.betmates.restapp.model.entity.reference.Team;
import com.web2h.betmates.restapp.model.exception.AlreadyExistsException;
import com.web2h.betmates.restapp.model.validation.Field;
import com.web2h.betmates.restapp.persistence.repository.reference.ReferenceRepository;
import com.web2h.betmates.restapp.persistence.repository.reference.TeamRepository;

/**
 * Team service implementation class.
 * 
 * @author web2h
 */
@Service
@Transactional
public class TeamServiceImpl extends ReferenceServiceImpl<Team> implements TeamService {

	private TeamRepository teamRepository;

	public TeamServiceImpl(TeamRepository teamRepository, ReferenceLogService referenceLogService) {
		super(referenceLogService);
		this.teamRepository = teamRepository;
	}

	@Override
	public void checkIfExists(Team team) throws AlreadyExistsException {
		Team existingTeam = teamRepository.findByNameEnAndSport(team.getNameEn(), team.getSport());
		if (existingTeam != null && (team.isBeingCreated() || !team.getId().equals(existingTeam.getId()))) {
			throw new AlreadyExistsException(Field.NAME_EN, Team.class.getName());
		}
		existingTeam = teamRepository.findByNameFrAndSport(team.getNameFr(), team.getSport());
		if (existingTeam != null && (team.isBeingCreated() || !team.getId().equals(existingTeam.getId()))) {
			throw new AlreadyExistsException(Field.NAME_FR, Team.class.getName());
		}
	}

	@Override
	public ReferenceRepository<Team> getRepository() {
		return teamRepository;
	}

	@Override
	public void merge(Team existingTeam, Team newTeam) {
		super.merge(existingTeam, newTeam);
		existingTeam.setSport(newTeam.getSport());
		existingTeam.setShortNameEn(newTeam.getShortNameEn());
		existingTeam.setShortNameFr(newTeam.getShortNameFr());
	}
}