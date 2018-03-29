package com.web2h.betmates.restapp.rest.controller;

public interface UrlConstants {

	String ADMIN_PREFIX = "/admin";
	String CITY_PREFIX = "/city";
	String COUNTRY_PREFIX = "/country";
	String USER_PREFIX = "/user";
	String VENUE_PREFIX = "/venue";

	String LOGIN_ACTION = "/login";
	String SIGN_UP_ACTION = "/sign-up";

	String CITY_CREATION_URL = ADMIN_PREFIX + CITY_PREFIX;
	String CITY_EDITION_URL = ADMIN_PREFIX + CITY_PREFIX;
	String COUNTRY_CREATION_URL = ADMIN_PREFIX + COUNTRY_PREFIX;
	String COUNTRY_EDITION_URL = ADMIN_PREFIX + COUNTRY_PREFIX;
	String LOGIN_URL = LOGIN_ACTION;
	String SIGN_UP_URL = USER_PREFIX + SIGN_UP_ACTION;
	String VENUE_CREATION_URL = ADMIN_PREFIX + VENUE_PREFIX;
	String VENUE_EDITION_URL = ADMIN_PREFIX + VENUE_PREFIX;
}
