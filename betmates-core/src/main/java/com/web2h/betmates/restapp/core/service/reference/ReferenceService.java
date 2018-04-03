package com.web2h.betmates.restapp.core.service.reference;

import java.util.List;

import com.web2h.betmates.restapp.model.entity.reference.Reference;
import com.web2h.betmates.restapp.model.entity.reference.log.ReferenceLogEvent;
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

	/**
	 * Gets a reference.
	 * 
	 * @param referenceId
	 *            Id of the reference to get.
	 * @return The retrieved reference, null if none could be found
	 */
	R get(Long referenceId);

	/**
	 * Gets the event log for the given reference.
	 * 
	 * @param referenceId
	 *            Id of the reference we want to get the log for
	 * @return The list of log events
	 */
	List<ReferenceLogEvent> getLog(Long referenceId);
}