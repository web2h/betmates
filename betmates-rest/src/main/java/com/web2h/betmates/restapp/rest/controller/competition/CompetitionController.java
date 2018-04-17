package com.web2h.betmates.restapp.rest.controller.competition;

import static com.web2h.betmates.restapp.rest.controller.UrlConstants.ADD_OR_REMOVE_TEAM_ACTION;
import static com.web2h.betmates.restapp.rest.controller.UrlConstants.ADD_OR_REMOVE_VENUE_ACTION;
import static com.web2h.betmates.restapp.rest.controller.UrlConstants.ADMIN_PREFIX;
import static com.web2h.betmates.restapp.rest.controller.UrlConstants.COMPETITION_PREFIX;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web2h.betmates.restapp.core.service.competition.CompetitionService;
import com.web2h.betmates.restapp.core.service.user.UserService;
import com.web2h.betmates.restapp.model.entity.competition.Competition;
import com.web2h.betmates.restapp.model.exception.AlreadyExistsException;
import com.web2h.betmates.restapp.model.exception.InternalErrorException;
import com.web2h.betmates.restapp.model.exception.InvalidDataException;
import com.web2h.betmates.restapp.model.exception.NotFoundException;
import com.web2h.betmates.restapp.model.validation.group.CreationChecks;
import com.web2h.betmates.restapp.model.validation.group.EditionChecks;
import com.web2h.betmates.restapp.rest.controller.CommonController;

/**
 * Competition administration controller.
 * 
 * @author web2h
 */
@RestController
@RequestMapping(ADMIN_PREFIX + COMPETITION_PREFIX)
@PreAuthorize("hasRole('ADMINISTRATOR')")
public class CompetitionController extends CommonController {

	private Logger logger = LoggerFactory.getLogger(CompetitionController.class);

	private CompetitionService competitionService;

	public CompetitionController(CompetitionService competitionService, UserService userService) {
		super(userService);
		this.competitionService = competitionService;
	}

	@PostMapping(ADD_OR_REMOVE_TEAM_ACTION)
	public ResponseEntity<Object> addOrRemoveTeams(@RequestBody @Validated(EditionChecks.class) Competition competition, BindingResult result) {
		logger.info("Team addition or removal for " + competition);
		if (result.hasErrors()) {
			InvalidDataException ide = InvalidDataException.createWithErrorList(result.getAllErrors());
			logger.warn("Invalid data");
			return ide.getResponseEntity();
		}

		Competition updatedCompetition = null;
		try {
			updatedCompetition = competitionService.addOrRemoveTeams(competition, getLoggedInUser());
		} catch (NotFoundException nfe) {
			logger.warn(nfe.getMessage());
			return nfe.getResponseEntity();
		} catch (InvalidDataException ide) {
			logger.warn(ide.getMessage());
			return ide.getResponseEntity();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new InternalErrorException(e.getMessage()).getResponseEntity();
		}

		return new ResponseEntity<Object>(updatedCompetition, HttpStatus.OK);
	}

	@PostMapping(ADD_OR_REMOVE_VENUE_ACTION)
	public ResponseEntity<Object> addOrRemoveVenues(@RequestBody @Validated(EditionChecks.class) Competition competition, BindingResult result) {
		logger.info("Venue addition or removal for " + competition);
		if (result.hasErrors()) {
			InvalidDataException ide = InvalidDataException.createWithErrorList(result.getAllErrors());
			logger.warn("Invalid data");
			return ide.getResponseEntity();
		}

		Competition updatedCompetition = null;
		try {
			updatedCompetition = competitionService.addOrRemoveVenues(competition, getLoggedInUser());
		} catch (NotFoundException nfe) {
			logger.warn(nfe.getMessage());
			return nfe.getResponseEntity();
		} catch (InvalidDataException ide) {
			logger.warn(ide.getMessage());
			return ide.getResponseEntity();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new InternalErrorException(e.getMessage()).getResponseEntity();
		}

		return new ResponseEntity<Object>(updatedCompetition, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Object> create(@RequestBody @Validated(CreationChecks.class) Competition competition, BindingResult result) {
		logger.info("Competition creation" + competition);
		if (result.hasErrors()) {
			InvalidDataException ide = InvalidDataException.createWithErrorList(result.getAllErrors());
			logger.warn("Invalid data");
			return ide.getResponseEntity();
		}

		Competition createdCompetition = null;
		try {
			createdCompetition = competitionService.create(competition, getLoggedInUser());
		} catch (AlreadyExistsException aee) {
			logger.warn(aee.getMessage());
			return aee.getResponseEntity();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new InternalErrorException(e.getMessage()).getResponseEntity();
		}

		return new ResponseEntity<Object>(createdCompetition, HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<Object> edit(@RequestBody @Validated(EditionChecks.class) Competition competition, BindingResult result) {
		logger.info("Competition edition" + competition);
		if (result.hasErrors()) {
			InvalidDataException ide = InvalidDataException.createWithErrorList(result.getAllErrors());
			logger.warn("Invalid data");
			return ide.getResponseEntity();
		}

		Competition editedCompetition = null;
		try {
			editedCompetition = competitionService.edit(competition, getLoggedInUser());
		} catch (NotFoundException nfe) {
			logger.error(nfe.getMessage());
			return nfe.getResponseEntity();
		} catch (AlreadyExistsException aee) {
			logger.warn(aee.getMessage());
			return aee.getResponseEntity();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new InternalErrorException(e.getMessage()).getResponseEntity();
		}

		return new ResponseEntity<Object>(editedCompetition, HttpStatus.OK);
	}
}