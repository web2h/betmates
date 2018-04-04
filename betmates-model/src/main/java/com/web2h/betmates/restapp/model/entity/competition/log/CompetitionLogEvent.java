package com.web2h.betmates.restapp.model.entity.competition.log;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.web2h.betmates.restapp.model.entity.competition.Competition;
import com.web2h.betmates.restapp.model.entity.log.LogEvent;
import com.web2h.betmates.restapp.model.entity.log.LogEventType;
import com.web2h.betmates.restapp.model.entity.user.AppUser;

@Entity
@Table(name = "competition_log_events")
public class CompetitionLogEvent extends LogEvent {

	/** COMPETITION - Competition concerned by the event. */
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "competition_id", nullable = false)
	private Competition competition;

	/** CHANGES - Changes made on this element. */
	@OneToMany(mappedBy = "logEvent", cascade = CascadeType.ALL)
	private List<CompetitionLogEventChange> changes;

	public CompetitionLogEvent() {

	}

	public CompetitionLogEvent(Competition competition, LogEventType type, AppUser appUser) {
		super(type, appUser);
		this.competition = competition;

		changes = new ArrayList<>();
	}

	public Competition getCompetition() {
		return competition;
	}

	public void setCompetition(Competition competition) {
		this.competition = competition;
	}

	public List<CompetitionLogEventChange> getChanges() {
		return changes;
	}
}