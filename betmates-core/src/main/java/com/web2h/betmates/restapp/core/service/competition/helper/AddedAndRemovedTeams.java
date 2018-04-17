package com.web2h.betmates.restapp.core.service.competition.helper;

import java.util.HashSet;
import java.util.Set;

import com.web2h.betmates.restapp.model.entity.competition.CompetitionTeam;

public class AddedAndRemovedTeams {

	private Set<CompetitionTeam> addedTeams;

	private Set<CompetitionTeam> removedTeams;

	public AddedAndRemovedTeams() {
		addedTeams = new HashSet<>();
		removedTeams = new HashSet<>();
	}

	public Set<CompetitionTeam> getAddedTeams() {
		return addedTeams;
	}

	public Set<CompetitionTeam> getRemovedTeams() {
		return removedTeams;
	}
}