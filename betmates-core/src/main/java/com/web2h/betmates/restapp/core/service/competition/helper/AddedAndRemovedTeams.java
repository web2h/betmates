package com.web2h.betmates.restapp.core.service.competition.helper;

import java.util.HashSet;
import java.util.Set;

import com.web2h.betmates.restapp.model.entity.reference.Team;

public class AddedAndRemovedTeams {

	private Set<Team> addedTeams;

	private Set<Team> removedTeams;

	public AddedAndRemovedTeams() {
		addedTeams = new HashSet<>();
		removedTeams = new HashSet<>();
	}

	public Set<Team> getAddedTeams() {
		return addedTeams;
	}

	public Set<Team> getRemovedTeams() {
		return removedTeams;
	}
}