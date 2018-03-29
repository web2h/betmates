package com.web2h.betmates.restapp.core.service.log;

import com.web2h.betmates.restapp.model.entity.reference.Reference;
import com.web2h.betmates.restapp.model.entity.user.AppUser;

/**
 * Log service interface for events on references.
 * 
 * @author web2h
 */
public interface ReferenceLogService {

	void logCreation(Reference reference, AppUser creator);
}
