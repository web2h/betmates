package com.web2h.betmates.restapp.core.service.user;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.web2h.betmates.restapp.model.entity.user.AppUser;
import com.web2h.betmates.restapp.model.exception.AlreadyExistsException;

/**
 * User service interface.
 * 
 * @author web2h
 */
public interface UserService extends UserDetailsService {

	/**
	 * Gets the application user by its email address.
	 * 
	 * @param userEmail
	 *            The user email address
	 * @return The retrieved {@link AppUser} instance, <code>null</code> if none
	 *         could be found
	 */
	AppUser getAppUserByEmail(String userEmail);

	AppUser signUpAppUser(AppUser appUser) throws AlreadyExistsException;

	/**
	 * Sets the BCryptPasswordEncode attribute for this service.
	 * 
	 * @param bCryptPasswordEncoder
	 *            The encoder
	 */
	void setbCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder);
}