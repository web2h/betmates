package com.web2h.betmates.restapp.core.service.reference;

import java.util.List;

import com.google.common.base.Preconditions;
import com.web2h.betmates.restapp.model.entity.reference.Reference;
import com.web2h.betmates.restapp.model.entity.reference.log.ReferenceLogEvent;
import com.web2h.betmates.restapp.model.entity.user.AppUser;
import com.web2h.betmates.restapp.model.exception.AlreadyExistsException;
import com.web2h.betmates.restapp.model.exception.InvalidDataException;
import com.web2h.betmates.restapp.model.exception.NotFoundException;
import com.web2h.betmates.restapp.model.validation.Field;
import com.web2h.betmates.restapp.persistence.repository.reference.ReferenceRepository;

/**
 * Common reference service interface.
 * 
 * @author web2h
 */
public abstract class ReferenceServiceImpl<R extends Reference> implements ReferenceService<R> {

	private ReferenceLogService referenceLogService;

	public ReferenceServiceImpl(ReferenceLogService referenceLogService) {
		this.referenceLogService = referenceLogService;
	}

	@Override
	public R create(R reference, AppUser creator) throws AlreadyExistsException, InvalidDataException {
		Preconditions.checkNotNull(reference);
		Preconditions.checkNotNull(creator);

		checkIfLinkedReferenceExists(reference);
		checkIfExists(reference);

		referenceLogService.logCreation(reference, creator);

		getRepository().save(reference);
		return reference;
	}

	@Override
	public R edit(R reference, AppUser editor) throws NotFoundException, AlreadyExistsException, InvalidDataException {
		Preconditions.checkNotNull(reference);
		Preconditions.checkNotNull(editor);

		R existingReference = getRepository().findOne(reference.getId());
		if (existingReference == null) {
			throw new NotFoundException(Field.ID, reference.getClass().getName());
		}

		checkIfLinkedReferenceExists(reference);
		checkIfExists(reference);

		referenceLogService.logEdition(existingReference, reference, editor);

		merge(existingReference, reference);
		getRepository().save(existingReference);
		return existingReference;
	}

	@Override
	public R get(Long referenceId) {
		return getRepository().findOne(referenceId);
	}

	@Override
	public List<ReferenceLogEvent> getLog(Long referenceId) {
		return referenceLogService.getLog(referenceId);
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

	/**
	 * Merges references.
	 * 
	 * @param existingReference
	 *            The existing reference in DB before the edition
	 * @param newReference
	 *            The new reference values
	 */
	public void merge(R existingReference, R newReference) {
		existingReference.setNameEn(newReference.getNameEn());
		existingReference.setNameFr(newReference.getNameFr());
	}
}