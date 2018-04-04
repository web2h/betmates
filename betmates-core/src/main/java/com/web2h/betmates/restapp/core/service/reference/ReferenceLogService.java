package com.web2h.betmates.restapp.core.service.reference;

import java.util.List;

import com.web2h.betmates.restapp.model.entity.reference.Reference;
import com.web2h.betmates.restapp.model.entity.reference.log.ReferenceLogEvent;
import com.web2h.betmates.restapp.model.entity.user.AppUser;

/**
 * Log service interface for events on references.
 * 
 * @author web2h
 */
public interface ReferenceLogService {

	/**
	 * Gets the event log for the given reference.
	 * 
	 * @param referenceId
	 *            Id of the reference we want to get the log for
	 * @return The list of log events
	 */
	List<ReferenceLogEvent> getLog(Long referenceId);

	/**
	 * Logs a reference creation.
	 * 
	 * @param reference
	 *            The created reference
	 * @param creator
	 *            The user who created the reference
	 */
	void logCreation(Reference reference, AppUser creator);

	/**
	 * Logs a reference edition.
	 * 
	 * @param oldReference
	 *            The current reference
	 * @param newReference
	 *            The new reference values
	 * @param editor
	 *            The user who edited the reference
	 */
	void logEdition(Reference oldReference, Reference newReference, AppUser editor);
}
