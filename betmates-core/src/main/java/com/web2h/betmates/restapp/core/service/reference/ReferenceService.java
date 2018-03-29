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

	R create(R reference, AppUser creator) throws AlreadyExistsException, InvalidDataException;
}