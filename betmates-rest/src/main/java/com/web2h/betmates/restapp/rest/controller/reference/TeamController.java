package com.web2h.betmates.restapp.rest.controller.reference;

import static com.web2h.betmates.restapp.rest.controller.UrlConstants.ADMIN_PREFIX;
import static com.web2h.betmates.restapp.rest.controller.UrlConstants.TEAM_PREFIX;

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
import com.web2h.betmates.restapp.model.entity.reference.Team;
import com.web2h.betmates.restapp.model.validation.group.CreationChecks;
import com.web2h.betmates.restapp.model.validation.group.EditionChecks;

/**
 * Team administration controller.
 * 
 * @author web2h
 */
@RestController
@RequestMapping(ADMIN_PREFIX + TEAM_PREFIX)
@PreAuthorize("hasRole('ADMINISTRATOR')")
public class TeamController extends ReferenceController<Team> {

	private Logger logger = LoggerFactory.getLogger(TeamController.class);

	public TeamController(UserService userService, ReferenceService<Team> referenceService) {
		super(userService, referenceService);
	}

	@PostMapping
	public ResponseEntity<Object> create(@RequestBody @Validated(CreationChecks.class) Team team, BindingResult result) {
		logger.info("Team creation" + team);
		return super.create(team, result);
	}

	@PutMapping
	public ResponseEntity<Object> edit(@RequestBody @Validated(EditionChecks.class) Team team, BindingResult result) {
		logger.info("Team edition" + team);
		return super.edit(team, result);
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}
}