package com.web2h.betmates.restapp.core.service.reference;

import com.google.common.base.Preconditions;
import com.web2h.betmates.restapp.model.entity.reference.Reference;
import com.web2h.betmates.restapp.model.entity.user.AppUser;
import com.web2h.betmates.restapp.model.exception.AlreadyExistsException;
import com.web2h.betmates.restapp.model.exception.InvalidDataException;
import com.web2h.betmates.restapp.persistence.repository.reference.ReferenceRepository;

/**
 * Common reference service interface.
 * 
 * @author web2h
 */
public abstract class ReferenceServiceImpl<R extends Reference> implements ReferenceService<R> {

	@Override
	public R create(R reference, AppUser creator) throws AlreadyExistsException, InvalidDataException {
		Preconditions.checkNotNull(reference);
		Preconditions.checkNotNull(creator);

		checkIfLinkedReferenceExists(reference);
		checkIfExists(reference);

		// TODO Log creation

		getRepository().save(reference);
		return reference;
	}

	/**
	 * Checks if the reference already exists to prevent inserting duplicates.
	 * 
	 * @param reference
	 *            The reference to check
	 * @throws AlreadyExistsException
	 *             When the reference already exists
	 */
	public abstract void checkIfExists(R reference) throws AlreadyExistsException;

	/**
	 * Checks if the linked reference exists (The country for a city, the city for a venue, ...).
	 * 
	 * @param reference
	 *            The reference to check
	 * @throws InvalidDataException
	 *             When the linked reference does not exist
	 */
	public void checkIfLinkedReferenceExists(R reference) throws InvalidDataException {
		return;
	}

	/**
	 * Gets the current repository depending on the reference type.
	 * 
	 * @return The current repository
	 */
	public abstract ReferenceRepository<R> getRepository();
}
