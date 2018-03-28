package com.web2h.betmates.restapp.rest.controller;

import org.springframework.security.core.context.SecurityContextHolder;

import com.web2h.betmates.restapp.core.service.user.UserService;
import com.web2h.betmates.restapp.model.entity.user.AppUser;

/**
 * Common controller class. Contains methods common to all controllers.
 * 
 * @author web2h
 */
public class CommonController {

	protected UserService userService;

	public CommonController(UserService userService) {
		this.userService = userService;
	}

	public AppUser getLoggedInUser() {
		String userName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return userService.getAppUserByEmail(userName);
	}
}