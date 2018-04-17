package com.web2h.betmates.restapp.rest.controller.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web2h.betmates.restapp.core.service.user.UserService;
import com.web2h.betmates.restapp.model.entity.user.AppUser;
import com.web2h.betmates.restapp.model.exception.AlreadyExistsException;
import com.web2h.betmates.restapp.model.exception.InternalErrorException;
import com.web2h.betmates.restapp.model.exception.InvalidDataException;
import com.web2h.betmates.restapp.model.validation.group.CreationChecks;
import com.web2h.betmates.restapp.rest.controller.CommonController;
import com.web2h.betmates.restapp.rest.controller.UrlConstants;

@RestController
@RequestMapping(UrlConstants.USER_PREFIX)
public class UserController extends CommonController {

	private Logger logger = LoggerFactory.getLogger(UserController.class);

	public UserController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		super(userService);
		this.userService.setbCryptPasswordEncoder(bCryptPasswordEncoder);
	}

	@PostMapping("/sign-up")
	public ResponseEntity<Object> signUp(@RequestBody @Validated(CreationChecks.class) AppUser user, BindingResult result) {
		logger.info("User sign up - " + user);

		if (result.hasErrors()) {
			InvalidDataException ide = InvalidDataException.createWithErrorList(result.getAllErrors());
			logger.warn("Invalid data");
			return ide.getResponseEntity();
		}

		AppUser createdUser = null;
		try {
			createdUser = userService.signUpAppUser(user);
		} catch (AlreadyExistsException aee) {
			logger.warn(aee.getMessage());
			return aee.getResponseEntity();
		} catch (Exception e) {
			return new InternalErrorException(e.getMessage()).getResponseEntity();
		}

		return new ResponseEntity<Object>(createdUser, HttpStatus.OK);
	}
}