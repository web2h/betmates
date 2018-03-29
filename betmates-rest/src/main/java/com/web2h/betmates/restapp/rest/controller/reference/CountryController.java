package com.web2h.betmates.restapp.rest.controller.reference;

import static com.web2h.betmates.restapp.rest.controller.UrlConstants.ADMIN_PREFIX;
import static com.web2h.betmates.restapp.rest.controller.UrlConstants.COUNTRY_PREFIX;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web2h.betmates.restapp.core.service.reference.ReferenceService;
import com.web2h.betmates.restapp.core.service.user.UserService;
import com.web2h.betmates.restapp.model.entity.reference.Country;
import com.web2h.betmates.restapp.model.validation.group.CreationChecks;

/**
 * Country administration controller.
 * 
 * @author web2h
 */
@RestController
@RequestMapping(ADMIN_PREFIX + COUNTRY_PREFIX)
public class CountryController extends ReferenceController<Country> {

	private Logger logger = LoggerFactory.getLogger(CountryController.class);

	public CountryController(UserService userService, ReferenceService<Country> countryService) {
		super(userService, countryService);
	}

	@PostMapping
	public ResponseEntity<Object> create(@RequestBody @Validated(CreationChecks.class) Country country, BindingResult result) {
		logger.info("Country creation" + country);
		return super.create(country, result);
	}

	@Override
	public Logger getLogger() {
		return logger;
	}
}