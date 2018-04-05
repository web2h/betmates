package com.web2h.betmates.restapp.model.entity.reference;

import static com.web2h.betmates.restapp.model.entity.FieldLength.SHORT_NAME_MAX_LENGTH;
import static com.web2h.betmates.restapp.model.entity.FieldLength.SPORT_MAX_LENGTH;
import static com.web2h.betmates.restapp.model.entity.FieldLength.TEXT_MIN_LENGTH;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.web2h.betmates.restapp.model.deserializer.JsonTrimmerDeserializer;
import com.web2h.betmates.restapp.model.validation.Field;
import com.web2h.tools.StringTools;

/**
 * Team entity class.
 * 
 * @author web2h
 */
@Entity
@Table(name = "basic_references")
@DiscriminatorValue("TEAM")
public class Team extends Reference {

	/** SPORT - Sport played by the team. */
	@Enumerated(EnumType.STRING)
	@Column(name = "sport", length = SPORT_MAX_LENGTH)
	@NotNull
	private Sport sport;

	/** SHORT_NAME_EN - Team short name in English. */
	@Column(name = "short_name_en", length = SHORT_NAME_MAX_LENGTH)
	@NotNull
	@Size(min = TEXT_MIN_LENGTH, max = SHORT_NAME_MAX_LENGTH)
	@JsonDeserialize(using = JsonTrimmerDeserializer.class)
	private String shortNameEn;

	/** NAME_FR - Team short name in French. */
	@Column(name = "short_name_fr", length = SHORT_NAME_MAX_LENGTH)
	@NotNull
	@Size(min = TEXT_MIN_LENGTH, max = SHORT_NAME_MAX_LENGTH)
	@JsonDeserialize(using = JsonTrimmerDeserializer.class)
	private String shortNameFr;

	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(getNameEn());
		hcb.append(getNameFr());
		hcb.append(sport);
		return hcb.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Team)) {
			return false;
		}
		Team that = (Team) obj;
		EqualsBuilder eb = new EqualsBuilder();
		eb.append(getNameEn(), that.getNameEn());
		eb.append(getNameFr(), that.getNameFr());
		eb.append(sport, that.sport);
		return eb.isEquals();
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("TEAM");
		buffer.append(super.toString());
		buffer.append(StringTools.getFieldAndValueToString(Field.SPORT, sport));
		return buffer.toString();
	}

	public Sport getSport() {
		return sport;
	}

	public void setSport(Sport sport) {
		this.sport = sport;
	}

	public String getShortNameEn() {
		return shortNameEn;
	}

	public void setShortNameEn(String shortNameEn) {
		this.shortNameEn = shortNameEn;
	}

	public String getShortNameFr() {
		return shortNameFr;
	}

	public void setShortNameFr(String shortNameFr) {
		this.shortNameFr = shortNameFr;
	}
}