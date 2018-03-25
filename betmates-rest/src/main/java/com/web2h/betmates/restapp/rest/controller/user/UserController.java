package com.web2h.betmates.restapp.rest.controller.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web2h.betmates.restapp.core.service.user.UserService;
import com.web2h.betmates.restapp.model.entity.user.AppUser;
import com.web2h.betmates.restapp.model.exception.AlreadyExistsException;
import com.web2h.betmates.restapp.model.exception.InternalErrorException;
import com.web2h.betmates.restapp.model.exception.InvalidDataException;

@RestController
@RequestMapping("/user")
public class UserController {

	private Logger logger = LoggerFactory.getLogger(UserController.class);

	private UserService userService;

	public UserController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userService = userService;
		this.userService.setbCryptPasswordEncoder(bCryptPasswordEncoder);
	}
	
	@GetMapping("admin")
	@Secured("RGR")
	public ResponseEntity<Object> seeAdmin() {
		AppUser user = new AppUser();
		user.setAlias("Billy the Kid");
		return new ResponseEntity<Object>(user, HttpStatus.OK);
	}

	@PostMapping("/sign-up")
	public ResponseEntity<Object> signUp(@RequestBody @Validated AppUser user,
			BindingResult result) {
		logger.info("User sign up - " + user);

		if (result.hasErrors()) {
			InvalidDataException ide = new InvalidDataException(result.getAllErrors());
			logger.warn("Invalid data");
			return ide.getResponseEntity();
		}

		AppUser createdUSer = null;
		try {
			createdUSer = userService.signUpAppUser(user);
		} catch (AlreadyExistsException aee) {
			logger.warn(aee.getMessage());
			return aee.getResponseEntity();
		} catch (Exception e) {
			return new InternalErrorException(e.getMessage()).getResponseEntity();
		}

		return new ResponseEntity<Object>(createdUSer, HttpStatus.OK);
	}
}