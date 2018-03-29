package com.web2h.betmates.restapp.rest.controller.reference;

import static com.web2h.betmates.restapp.rest.controller.UrlConstants.ADMIN_PREFIX;
import static com.web2h.betmates.restapp.rest.controller.UrlConstants.VENUE_PREFIX;

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
import com.web2h.betmates.restapp.model.entity.reference.Venue;
import com.web2h.betmates.restapp.model.validation.group.CreationChecks;
import com.web2h.betmates.restapp.model.validation.group.EditionChecks;

/**
 * Venue administration controller.
 * 
 * @author web2h
 */
@RestController
@RequestMapping(ADMIN_PREFIX + VENUE_PREFIX)
@PreAuthorize("hasRole('ADMINISTRATOR')")
public class VenueController extends ReferenceController<Venue> {

	private Logger logger = LoggerFactory.getLogger(VenueController.class);

	public VenueController(UserService userService, ReferenceService<Venue> venueService) {
		super(userService, venueService);
	}

	@PostMapping
	public ResponseEntity<Object> create(@RequestBody @Validated(CreationChecks.class) Venue venue, BindingResult result) {
		logger.info("Venue creation" + venue);
		return super.create(venue, result);
	}

	@PutMapping
	public ResponseEntity<Object> edit(@RequestBody @Validated(EditionChecks.class) Venue venue, BindingResult result) {
		logger.info("Venue edition" + venue);
		return super.edit(venue, result);
	}

	@Override
	public Logger getLogger() {
		return logger;
	}
}