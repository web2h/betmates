package com.web2h.betmates.restapp.rest.controller.reference;

import static com.web2h.betmates.restapp.rest.controller.UrlConstants.ADMIN_PREFIX;
import static com.web2h.betmates.restapp.rest.controller.UrlConstants.CITY_PREFIX;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web2h.betmates.restapp.core.service.reference.ReferenceService;
import com.web2h.betmates.restapp.core.service.user.UserService;
import com.web2h.betmates.restapp.model.entity.reference.City;
import com.web2h.betmates.restapp.model.validation.group.CreationChecks;
import com.web2h.betmates.restapp.model.validation.group.EditionChecks;

/**
 * City administration controller.
 * 
 * @author web2h
 */
@RestController
@RequestMapping(ADMIN_PREFIX + CITY_PREFIX)
@PreAuthorize("hasRole('ADMINISTRATOR')")
public class CityController extends ReferenceController<City> {

	private Logger logger = LoggerFactory.getLogger(CityController.class);

	public CityController(UserService userService, ReferenceService<City> cityService) {
		super(userService, cityService);
	}

	@PostMapping
	public ResponseEntity<Object> create(@RequestBody @Validated(CreationChecks.class) City city, BindingResult result) {
		logger.info("City creation" + city);
		return super.create(city, result);
	}

	@PutMapping
	public ResponseEntity<Object> edit(@RequestBody @Validated(EditionChecks.class) City city, BindingResult result) {
		logger.info("City edition" + city);
		return super.edit(city, result);
	}

	@Override
	public Logger getLogger() {
		return logger;
	}
}