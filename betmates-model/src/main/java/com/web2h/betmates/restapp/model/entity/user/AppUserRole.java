package com.web2h.betmates.restapp.model.entity.user;

/**
 * Application user role enumeration.
 * 
 * @author web2h
 */
public enum AppUserRole {

	ROLE_ADMINISTRATOR("ROLE_ADMINISTRATOR,ROLE_SCORE_KEEPER,ROLE_PLAYER"),
	ROLE_PLAYER("ROLE_PLAYER"),
	ROLE_SCORE_KEEPER("ROLE_SCORE_KEEPER,ROLE_PLAYER");

	private String authorities;

	private AppUserRole(String authorities) {
		this.authorities = authorities;
	}

	public String getAuthorities() {
		return authorities;
	}
}