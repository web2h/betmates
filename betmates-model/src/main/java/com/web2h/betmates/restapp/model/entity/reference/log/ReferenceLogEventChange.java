package com.web2h.betmates.restapp.model.entity.reference.log;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.web2h.betmates.restapp.model.entity.log.LogEventChange;
import com.web2h.betmates.restapp.model.validation.Field;

@Entity
@Table(name = "reference_log_event_changes")
public class ReferenceLogEventChange extends LogEventChange {

	/** LOG_EVENT - Reference log event concerned by the change. */
	@ManyToOne
	@JoinColumn(name = "log_event_id", nullable = false)
	private ReferenceLogEvent logEvent;

	public ReferenceLogEventChange(ReferenceLogEvent logEvent, Field field, String newValue) {
		this.logEvent = logEvent;
		setField(field);
		setNewValue(newValue);
	}

	public ReferenceLogEventChange(ReferenceLogEvent logEvent, Field field, String newValue, String oldValue) {
		this.logEvent = logEvent;
		setField(field);
		setNewValue(newValue);
		setOldValue(oldValue);
	}

	public ReferenceLogEvent getLogEvent() {
		return logEvent;
	}

	public void setLogEvent(ReferenceLogEvent logEvent) {
		this.logEvent = logEvent;
	}
}