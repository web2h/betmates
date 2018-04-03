package com.web2h.betmates.restapp.model.entity.reference.log;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.web2h.betmates.restapp.model.entity.log.LogEvent;
import com.web2h.betmates.restapp.model.entity.log.LogEventType;
import com.web2h.betmates.restapp.model.entity.reference.Reference;
import com.web2h.betmates.restapp.model.entity.user.AppUser;

@Entity
@Table(name = "reference_log_events")
public class ReferenceLogEvent extends LogEvent {

	/** REFERENCE - Reference concerned by the event. */
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "reference_id", nullable = false)
	private Reference reference;

	/** CHANGES - Changes made on this element. */
	@OneToMany(mappedBy = "logEvent", cascade = CascadeType.ALL)
	private List<ReferenceLogEventChange> changes;

	public ReferenceLogEvent() {

	}

	public ReferenceLogEvent(Reference reference, LogEventType type, AppUser appUser) {
		super(type, appUser);
		this.reference = reference;

		changes = new ArrayList<>();
	}

	public Reference getReference() {
		return reference;
	}

	public void setReference(Reference reference) {
		this.reference = reference;
	}

	public List<ReferenceLogEventChange> getChanges() {
		return changes;
	}
}
