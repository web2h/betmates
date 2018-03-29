package com.web2h.betmates.restapp.model.entity.log;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.web2h.betmates.restapp.model.entity.user.AppUser;

/**
 * Log event entity class. Log table to store every event.
 *
 * @author web2h
 */
@MappedSuperclass
public abstract class LogEvent {

	/** ID - Internal event ID. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/** APP_USER - User who performed the action. */
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "app_user_id")
	private AppUser appUser;

	/** TIMESTAMP - When the event occurred. */
	@Column(name = "event_ts")
	private Date timestamp = new Date();

	/** TYPE - Type of event. */
	@Enumerated(EnumType.STRING)
	@Column(name = "event_type", length = 32)
	private LogEventType type;

	/** DESCRIPTION - Complementary description. */
	@Column(name = "description", length = 512, nullable = true)
	private String description;

	public LogEvent() {

	}

	public LogEvent(LogEventType type, AppUser appUser) {
		this.type = type;
		this.appUser = appUser;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AppUser getAppUser() {
		return appUser;
	}

	public void setAppUser(AppUser appUser) {
		this.appUser = appUser;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public LogEventType getType() {
		return type;
	}

	public void setType(LogEventType type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}