package com.web2h.betmates.restapp.persistence.repository.user;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import com.web2h.betmates.restapp.model.entity.user.AppUser;

/**
 * Application user repository interface.
 * 
 * @author web2h
 */
public interface AppUserRepository extends CrudRepository<AppUser, Serializable> {

	AppUser findByEmail(String email);
}