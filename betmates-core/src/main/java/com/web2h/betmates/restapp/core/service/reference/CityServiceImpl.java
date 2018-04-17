package com.web2h.betmates.restapp.core.service.reference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web2h.betmates.restapp.model.entity.reference.City;
import com.web2h.betmates.restapp.model.entity.reference.Country;
import com.web2h.betmates.restapp.model.exception.AlreadyExistsException;
import com.web2h.betmates.restapp.model.exception.InvalidDataException;
import com.web2h.betmates.restapp.model.validation.ErrorCode;
import com.web2h.betmates.restapp.model.validation.Field;
import com.web2h.betmates.restapp.persistence.repository.reference.CityRepository;
import com.web2h.betmates.restapp.persistence.repository.reference.CountryRepository;
import com.web2h.betmates.restapp.persistence.repository.reference.ReferenceRepository;

/**
 * City service implementation class.
 * 
 * @author web2h
 */
@Service
@Transactional
public class CityServiceImpl extends ReferenceServiceImpl<City> implements CityService {

	private CityRepository cityRepository;

	private CountryRepository countryRepository;

	private Logger logger = LoggerFactory.getLogger(CityServiceImpl.class);

	public CityServiceImpl(CityRepository cityRepository, CountryRepository countryRepository, ReferenceLogService referenceLogService) {
		super(referenceLogService);
		this.cityRepository = cityRepository;
		this.countryRepository = countryRepository;
	}

	@Override
	public void checkIfExists(City city) throws AlreadyExistsException {
		City existingCity = cityRepository.findByNameEnAndCountry(city.getNameEn(), city.getCountry());
		if (existingCity != null && (city.isBeingCreated() || !city.getId().equals(existingCity.getId()))) {
			throw new AlreadyExistsException(Field.NAME_EN, City.class.getName());
		}
		existingCity = cityRepository.findByNameFrAndCountry(city.getNameFr(), city.getCountry());
		if (existingCity != null && (city.isBeingCreated() || !city.getId().equals(existingCity.getId()))) {
			throw new AlreadyExistsException(Field.NAME_FR, City.class.getName());
		}
	}

	@Override
	public void checkIfLinkedReferenceExists(City city) throws InvalidDataException {
		Country country = countryRepository.findOne(city.getCountry().getId());
		if (country == null) {
			logger.warn("No country exists with the given ID [" + city.getCountry().getId() + "]");
			throw InvalidDataException.createWithFieldAndErrorCode(Field.COUNTRY, ErrorCode.NOT_FOUND);
		}
		city.setCountry(country);
	}

	@Override
	public ReferenceRepository<City> getRepository() {
		return cityRepository;
	}

	@Override
	public void merge(City existingCity, City newCity) {
		super.merge(existingCity, newCity);
		if (!existingCity.getCountry().equals(newCity.getCountry())) {
			Country newCountry = countryRepository.findOne(newCity.getCountry().getId());
			existingCity.setCountry(newCountry);
		}
	}
}