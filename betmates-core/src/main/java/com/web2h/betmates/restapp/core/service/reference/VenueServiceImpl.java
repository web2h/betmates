package com.web2h.betmates.restapp.core.service.reference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private Logger logger = LoggerFactory.getLogger(VenueServiceImpl.class);

	public VenueServiceImpl(VenueRepository venueRepository, CityRepository cityRepository) {
		this.venueRepository = venueRepository;
		this.cityRepository = cityRepository;
	}

	@Override
	public void checkIfExists(Venue venue) throws AlreadyExistsException {
		Venue existingVenue = venueRepository.findByNameEnAndCity(venue.getNameEn(), venue.getCity());
		if (existingVenue != null) {
			throw new AlreadyExistsException(Field.NAME_EN, Venue.class.getName());
		}
		existingVenue = venueRepository.findByNameFrAndCity(venue.getNameFr(), venue.getCity());
		if (existingVenue != null) {
			throw new AlreadyExistsException(Field.NAME_FR, Venue.class.getName());
		}
	}

	@Override
	public void checkIfLinkedReferenceExists(Venue venue) throws InvalidDataException {
		City city = cityRepository.findOne(venue.getCity().getId());
		if (city == null) {
			logger.warn("No city exists with the given ID [" + venue.getCity().getId() + "]");
			throw new InvalidDataException(Field.CITY, ErrorCode.NOT_FOUND);
		}
	}

	@Override
	public ReferenceRepository<Venue> getRepository() {
		return venueRepository;
	}
}