package com.web2h.betmates.restapp.core.service.reference;

import com.web2h.betmates.restapp.model.entity.reference.Reference;
import com.web2h.betmates.restapp.model.entity.user.AppUser;
import com.web2h.betmates.restapp.model.exception.AlreadyExistsException;
import com.web2h.betmates.restapp.model.exception.InvalidDataException;
import com.web2h.betmates.restapp.model.exception.NotFoundException;

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

	/**
	 * Edits an existing reference.
	 * 
	 * @param reference
	 *            The reference to edit
	 * @param editor
	 *            The user who requested the edition
	 * @return The edited reference
	 * @throws NotFoundException
	 *             When the reference to edit does not exist
	 * @throws AlreadyExistsException
	 *             When the new values are already used by another reference
	 * @throws InvalidDataException
	 *             When wrong data was provided
	 */
	R edit(R reference, AppUser editor) throws NotFoundException, AlreadyExistsException, InvalidDataException;
}