package com.web2h.betmates.restapp.core.service.log;

import com.web2h.betmates.restapp.model.entity.log.LogEventType;
import com.web2h.betmates.restapp.model.entity.reference.City;
import com.web2h.betmates.restapp.model.entity.reference.Reference;
import com.web2h.betmates.restapp.model.entity.reference.log.ReferenceLogEvent;
import com.web2h.betmates.restapp.model.entity.reference.log.ReferenceLogEventChange;
import com.web2h.betmates.restapp.model.entity.user.AppUser;
import com.web2h.betmates.restapp.model.validation.Field;
import com.web2h.betmates.restapp.persistence.repository.log.ReferenceLogEventRepository;

/**
 * Log service implementation class for events on references.
 * 
 * @author web2h
 */
public class ReferenceLogServiceImpl implements ReferenceLogService {

	private ReferenceLogEventRepository referenceLogEventRepository;

	public ReferenceLogServiceImpl(ReferenceLogEventRepository referenceLogEventRepository) {
		this.referenceLogEventRepository = referenceLogEventRepository;
	}

	@Override
	public void logCreation(Reference reference, AppUser creator) {
		ReferenceLogEvent event = new ReferenceLogEvent(LogEventType.CREATION, creator);

		ReferenceLogEventChange changeNameEn = new ReferenceLogEventChange();
		changeNameEn.setField(Field.NAME_EN);
		changeNameEn.setNewValue(reference.getNameEn());
		changeNameEn.setLogEvent(event);

		ReferenceLogEventChange changeNameFr = new ReferenceLogEventChange();
		changeNameFr.setField(Field.NAME_FR);
		changeNameFr.setNewValue(reference.getNameFr());
		changeNameFr.setLogEvent(event);

		if (reference instanceof City) {
			ReferenceLogEventChange changeCountry = new ReferenceLogEventChange();
			changeCountry.setField(Field.COUNTRY);
			changeCountry.setNewValue(((City) reference).getCountry().getLogValue());
			changeCountry.setLogEvent(event);
		}

		event.getChanges().add(changeNameEn);
		event.getChanges().add(changeNameFr);

		referenceLogEventRepository.save(event);
	}

}