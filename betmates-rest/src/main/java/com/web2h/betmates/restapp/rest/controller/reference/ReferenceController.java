package com.web2h.betmates.restapp.rest.controller.reference;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import com.web2h.betmates.restapp.core.service.reference.ReferenceService;
import com.web2h.betmates.restapp.core.service.user.UserService;
import com.web2h.betmates.restapp.model.entity.reference.Reference;
import com.web2h.betmates.restapp.model.exception.AlreadyExistsException;
import com.web2h.betmates.restapp.model.exception.InternalErrorException;
import com.web2h.betmates.restapp.model.exception.InvalidDataException;
import com.web2h.betmates.restapp.model.exception.NotFoundException;
import com.web2h.betmates.restapp.rest.controller.CommonController;

/**
 * Common reference controller.
 * 
 * @author web2h
 * 
 * @param <R>
 *            The reference type
 */
public abstract class ReferenceController<R extends Reference> extends CommonController {

	private ReferenceService<R> referenceService;

	public ReferenceController(UserService userService, ReferenceService<R> referenceService) {
		super(userService);
		this.referenceService = referenceService;
	}

	/**
	 * Creates a new reference.
	 * 
	 * @param reference
	 *            The reference to create
	 * @param result
	 *            The binding result for data validation
	 * @return The response entity
	 */
	public ResponseEntity<Object> create(R reference, BindingResult result) {
		if (result.hasErrors()) {
			InvalidDataException ide = InvalidDataException.createWithErrorList(result.getAllErrors());
			getLogger().warn("Invalid data");
			return ide.getResponseEntity();
		}

		R createdReference = null;
		try {
			createdReference = referenceService.create(reference, getLoggedInUser());
		} catch (AlreadyExistsException aee) {
			getLogger().warn(aee.getMessage());
			return aee.getResponseEntity();
		} catch (InvalidDataException ide) {
			getLogger().warn(ide.getMessage());
			return ide.getResponseEntity();
		} catch (Exception e) {
			getLogger().error(e.getMessage(), e);
			return new InternalErrorException(e.getMessage()).getResponseEntity();
		}

		return new ResponseEntity<Object>(createdReference, HttpStatus.OK);
	}

	/**
	 * Edits a reference.
	 * 
	 * @param reference
	 *            The reference to edit
	 * @param result
	 *            The binding result for data validation
	 * @return The response entity
	 */
	public ResponseEntity<Object> edit(R reference, BindingResult result) {
		if (result.hasErrors()) {
			InvalidDataException ide = InvalidDataException.createWithErrorList(result.getAllErrors());
			getLogger().warn("Invalid data");
			return ide.getResponseEntity();
		}

		R editedReference = null;
		try {
			editedReference = referenceService.edit(reference, getLoggedInUser());
		} catch (NotFoundException nfe) {
			getLogger().warn(nfe.getMessage());
			return nfe.getResponseEntity();
		} catch (InvalidDataException ide) {
			getLogger().warn(ide.getMessage());
			return ide.getResponseEntity();
		} catch (AlreadyExistsException aee) {
			getLogger().warn(aee.getMessage());
			return aee.getResponseEntity();
		} catch (Exception e) {
			getLogger().error(e.getMessage(), e);
			return new InternalErrorException(e.getMessage()).getResponseEntity();
		}

		return new ResponseEntity<Object>(editedReference, HttpStatus.OK);
	}

	protected abstract Logger getLogger();
}