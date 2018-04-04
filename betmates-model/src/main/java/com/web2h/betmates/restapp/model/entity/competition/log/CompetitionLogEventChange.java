package com.web2h.betmates.restapp.model.entity.competition.log;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.web2h.betmates.restapp.model.entity.log.LogEventChange;
import com.web2h.betmates.restapp.model.validation.Field;

@Entity
@Table(name = "competition_log_event_changes")
public class CompetitionLogEventChange extends LogEventChange {

	/** LOG_EVENT - Competition log event concerned by the change. */
	@ManyToOne
	@JoinColumn(name = "log_event_id", nullable = false)
	private CompetitionLogEvent logEvent;

	public CompetitionLogEventChange(CompetitionLogEvent logEvent, Field field, String newValue) {
		this.logEvent = logEvent;
		setField(field);
		setNewValue(newValue);
	}

	public CompetitionLogEventChange(CompetitionLogEvent logEvent, Field field, String newValue, String oldValue) {
		this.logEvent = logEvent;
		setField(field);
		setNewValue(newValue);
		setOldValue(oldValue);
	}

	public CompetitionLogEvent getLogEvent() {
		return logEvent;
	}

	public void setLogEvent(CompetitionLogEvent logEvent) {
		this.logEvent = logEvent;
	}
}