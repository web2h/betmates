package com.web2h.betmates.restapp.core.service.reference;

import java.util.List;

import org.springframework.stereotype.Service;

import com.web2h.betmates.restapp.model.entity.log.LogEventType;
import com.web2h.betmates.restapp.model.entity.reference.City;
import com.web2h.betmates.restapp.model.entity.reference.Reference;
import com.web2h.betmates.restapp.model.entity.reference.Venue;
import com.web2h.betmates.restapp.model.entity.reference.log.ReferenceLogEvent;
import com.web2h.betmates.restapp.model.entity.reference.log.ReferenceLogEventChange;
import com.web2h.betmates.restapp.model.entity.user.AppUser;
import com.web2h.betmates.restapp.model.validation.Field;
import com.web2h.betmates.restapp.persistence.repository.reference.ReferenceLogEventRepository;

/**
 * Log service implementation class for events on references.
 * 
 * @author web2h
 */
@Service
public class ReferenceLogServiceImpl implements ReferenceLogService {

	private ReferenceLogEventRepository referenceLogEventRepository;

	public ReferenceLogServiceImpl(ReferenceLogEventRepository referenceLogEventRepository) {
		this.referenceLogEventRepository = referenceLogEventRepository;
	}

	@Override
	public List<ReferenceLogEvent> getLog(Long referenceId) {
		return referenceLogEventRepository.findByReference_idOrderByTimestampDesc(referenceId);
	}

	@Override
	public void logCreation(Reference reference, AppUser creator) {
		ReferenceLogEvent event = new ReferenceLogEvent(reference, LogEventType.CREATION, creator);

		event.getChanges().add(new ReferenceLogEventChange(event, Field.NAME_EN, reference.getNameEn()));
		event.getChanges().add(new ReferenceLogEventChange(event, Field.NAME_FR, reference.getNameFr()));

		if (reference instanceof City) {
			event.getChanges().add(new ReferenceLogEventChange(event, Field.COUNTRY, ((City) reference).getCountry().getLogValue()));
		}

		if (reference instanceof Venue) {
			event.getChanges().add(new ReferenceLogEventChange(event, Field.CITY, ((Venue) reference).getCity().getLogValue()));
		}

		referenceLogEventRepository.save(event);
	}

	@Override
	public void logEdition(Reference oldReference, Reference newReference, AppUser editor) {
		ReferenceLogEvent event = new ReferenceLogEvent(oldReference, LogEventType.EDITION, editor);

		if (!oldReference.getNameEn().equals(newReference.getNameEn())) {
			event.getChanges().add(new ReferenceLogEventChange(event, Field.NAME_EN, newReference.getNameEn(), oldReference.getNameEn()));
		}
		if (!oldReference.getNameFr().equals(newReference.getNameFr())) {
			event.getChanges().add(new ReferenceLogEventChange(event, Field.NAME_FR, newReference.getNameFr(), oldReference.getNameFr()));
		}

		if (oldReference instanceof City && !((City) oldReference).getCountry().equals(((City) newReference).getCountry())) {
			event.getChanges().add(new ReferenceLogEventChange(event, Field.COUNTRY, ((City) newReference).getCountry().getLogValue(), ((City) oldReference).getCountry().getLogValue()));
		}

		if (oldReference instanceof Venue && !((Venue) oldReference).getCity().equals(((Venue) newReference).getCity())) {
			event.getChanges().add(new ReferenceLogEventChange(event, Field.CITY, ((Venue) newReference).getCity().getLogValue(), ((Venue) oldReference).getCity().getLogValue()));
		}

		referenceLogEventRepository.save(event);
	}
}