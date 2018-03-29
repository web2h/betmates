package com.web2h.betmates.restapp.model.entity.log;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.web2h.betmates.restapp.model.validation.Field;

/**
 * Log event change entity class. Log table to store every event change (old value vs new value).
 *
 * @author web2h
 */
@MappedSuperclass
public class LogEventChange {

	/** ID - Internal event detail ID. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/** FIELD - Field concerned of event. */
	@Enumerated(EnumType.STRING)
	@Column(name = "field", length = 16)
	private Field field;

	/** OLD_VALUE - Value before the change. */
	@Column(name = "old_value", length = 1024, nullable = true)
	private String oldValue;

	/** NEW_VALUE - Value after the change. */
	@Column(name = "new_value", length = 1024, nullable = true)
	private String newValue;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}
}