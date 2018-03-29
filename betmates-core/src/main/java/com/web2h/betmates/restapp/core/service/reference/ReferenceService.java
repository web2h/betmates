package com.web2h.betmates.restapp.core.service.reference;

import com.web2h.betmates.restapp.model.entity.reference.Reference;
import com.web2h.betmates.restapp.model.entity.user.AppUser;
import com.web2h.betmates.restapp.model.exception.AlreadyExistsException;
import com.web2h.betmates.restapp.model.exception.InvalidDataException;

/**
 * Reference service interface.
 * 
 * @author web2h
 *
 * @param <R>
 *            The reference type
 */
public interface ReferenceService<R extends Reference> {

	/**
	 * Creates a new reference.
	 * 
	 * @param reference
	 *            The reference to create
	 * @param creator
	 *            The user who requested the creation
	 * @return The created reference
	 * @throws AlreadyExistsException
	 *             When the reference already exists
	 * @throws InvalidDataException
	 *             When wrong data was provided
	 */
	R create(R reference, AppUser creator) throws AlreadyExistsException, InvalidDataException;
}