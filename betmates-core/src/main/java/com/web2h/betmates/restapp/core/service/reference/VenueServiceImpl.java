package com.web2h.betmates.restapp.core.service.reference;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web2h.betmates.restapp.model.entity.reference.City;
import com.web2h.betmates.restapp.model.entity.reference.Venue;
import com.web2h.betmates.restapp.model.exception.AlreadyExistsException;
import com.web2h.betmates.restapp.model.exception.InvalidDataException;
import com.web2h.betmates.restapp.model.validation.ErrorCode;
import com.web2h.betmates.restapp.model.validation.Field;
import com.web2h.betmates.restapp.persistence.repository.reference.CityRepository;
import com.web2h.betmates.restapp.persistence.repository.reference.ReferenceRepository;
import com.web2h.betmates.restapp.persistence.repository.reference.VenueRepository;

/**
 * Venue service implementation class.
 * 
 * @author web2h
 */
@Service
@Transactional
public class VenueServiceImpl extends ReferenceServiceImpl<Venue> implements VenueService {

	private VenueRepository venueRepository;

	private CityRepository cityRepository;

	public VenueServiceImpl(VenueRepository venueRepository, CityRepository cityRepository, ReferenceLogService referenceLogService) {
		super(referenceLogService);
		this.venueRepository = venueRepository;
		this.cityRepository = cityRepository;
	}

	@Override
	public void checkIfExists(Venue venue) throws AlreadyExistsException {
		Venue existingVenue = venueRepository.findByNameEnAndCity(venue.getNameEn(), venue.getCity());
		if (existingVenue != null && (venue.isBeingCreated() || !venue.getId().equals(existingVenue.getId()))) {
			throw new AlreadyExistsException(Field.NAME_EN, Venue.class.getName());
		}
		existingVenue = venueRepository.findByNameFrAndCity(venue.getNameFr(), venue.getCity());
		if (existingVenue != null && (venue.isBeingCreated() || !venue.getId().equals(existingVenue.getId()))) {
			throw new AlreadyExistsException(Field.NAME_FR, Venue.class.getName());
		}
	}

	@Override
	public void checkIfLinkedReferenceExists(Venue venue) throws InvalidDataException {
		Optional<City> city = cityRepository.findById(venue.getCity().getId());
		city.orElseThrow(() -> InvalidDataException.createWithFieldAndErrorCode(Field.CITY, ErrorCode.NOT_FOUND));
		venue.setCity(city.get());
	}

	@Override
	public ReferenceRepository<Venue> getRepository() {
		return venueRepository;
	}

	@Override
	public void merge(Venue existingVenue, Venue newVenue) {
		super.merge(existingVenue, newVenue);
		if (!existingVenue.getCity().equals(newVenue.getCity())) {
			Optional<City> newCity = cityRepository.findById(newVenue.getCity().getId());
			existingVenue.setCity(newCity.get());
		}
	}
}