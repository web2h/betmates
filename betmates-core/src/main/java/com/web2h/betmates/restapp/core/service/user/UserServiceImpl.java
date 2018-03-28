package com.web2h.betmates.restapp.core.service.user;

import static java.util.Collections.emptyList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web2h.betmates.restapp.model.entity.user.AppUser;
import com.web2h.betmates.restapp.model.entity.user.AppUserStatus;
import com.web2h.betmates.restapp.model.exception.AlreadyExistsException;
import com.web2h.betmates.restapp.model.validation.Field;
import com.web2h.betmates.restapp.persistence.repository.user.AppUserRepository;

/**
 * User service interface.
 * 
 * @author web2h
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

	private AppUserRepository appUserRepository;

	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public UserServiceImpl(AppUserRepository appUserRepository) {
		this.appUserRepository = appUserRepository;
	}

	@Override
	public AppUser getAppUserByEmail(String userEmail) {
		return appUserRepository.findByEmail(userEmail);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser appUser = appUserRepository.findByEmail(username);
		if (appUser == null || !AppUserStatus.ACTIVE.equals(appUser.getStatus())) {
			throw new UsernameNotFoundException(username);
		}
		return new User(appUser.getEmail(), appUser.getPassword(), emptyList());
	}

	@Override
	public AppUser signUpAppUser(AppUser appUser) throws AlreadyExistsException {

		// Checking if the user does not already exists
		AppUser existingUser = appUserRepository.findByEmail(appUser.getEmail());
		if (existingUser != null) {
			throw new AlreadyExistsException(Field.EMAIL);
		}
		existingUser = appUserRepository.findByAlias(appUser.getAlias());
		if (existingUser != null) {
			throw new AlreadyExistsException(Field.ALIAS);
		}

		AppUser userToCreate = new AppUser();
		// Encrypting password
		userToCreate.setPassword(bCryptPasswordEncoder.encode(appUser.getPassword()));

		appUserRepository.save(userToCreate);

		return null;
	}

	@Override
	public void setbCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
}