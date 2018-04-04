package com.web2h.betmates.restapp.core.service.reference;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web2h.betmates.restapp.model.entity.reference.Country;
import com.web2h.betmates.restapp.model.exception.AlreadyExistsException;
import com.web2h.betmates.restapp.model.validation.Field;
import com.web2h.betmates.restapp.persistence.repository.reference.CountryRepository;
import com.web2h.betmates.restapp.persistence.repository.reference.ReferenceRepository;

/**
 * Country service implementation class.
 * 
 * @author web2h
 */
@Service
@Transactional
public class CountryServiceImpl extends ReferenceServiceImpl<Country> implements CountryService {

	private CountryRepository countryRepository;

	public CountryServiceImpl(CountryRepository countryRepository, ReferenceLogService referenceLogService) {
		super(referenceLogService);
		this.countryRepository = countryRepository;
	}

	@Override
	public void checkIfExists(Country country) throws AlreadyExistsException {
		Country existingCountry = getRepository().findByNameEn(country.getNameEn());
		if (existingCountry != null && (country.isBeingCreated() || !country.getId().equals(existingCountry.getId()))) {
			throw new AlreadyExistsException(Field.NAME_EN, Country.class.getName());
		}
		existingCountry = getRepository().findByNameFr(country.getNameFr());
		if (existingCountry != null && (country.isBeingCreated() || !country.getId().equals(existingCountry.getId()))) {
			throw new AlreadyExistsException(Field.NAME_FR, Country.class.getName());
		}
	}

	@Override
	public ReferenceRepository<Country> getRepository() {
		return countryRepository;
	}
}