package com.web2h.betmates.restapp.core.service.reference;

import java.util.Optional;

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
		
		Optional<Country> country = countryRepository.findById(city.getCountry().getId());
		country.orElseThrow(() -> InvalidDataException.createWithFieldAndErrorCode(Field.COUNTRY, ErrorCode.NOT_FOUND));
		city.setCountry(country.get());
	}

	@Override
	public ReferenceRepository<City> getRepository() {
		return cityRepository;
	}

	@Override
	public void merge(City existingCity, City newCity) {
		super.merge(existingCity, newCity);
		if (!existingCity.getCountry().equals(newCity.getCountry())) {
			Optional<Country> newCountry = countryRepository.findById(newCity.getCountry().getId());
			existingCity.setCountry(newCountry.get());
		}
	}
}