package com.web2h.betmates.restapp.core.service.competition.helper;

import java.util.HashSet;
import java.util.Set;

import com.web2h.betmates.restapp.model.entity.reference.Venue;

public class AddedAndRemovedVenues {

	private Set<Venue> addedVenues;

	private Set<Venue> removedVenues;

	public AddedAndRemovedVenues() {
		addedVenues = new HashSet<>();
		removedVenues = new HashSet<>();
	}

	public Set<Venue> getAddedVenues() {
		return addedVenues;
	}

	public Set<Venue> getRemovedVenues() {
		return removedVenues;
	}
}