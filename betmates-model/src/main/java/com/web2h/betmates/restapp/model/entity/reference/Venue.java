package com.web2h.betmates.restapp.model.entity.reference;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.web2h.betmates.restapp.model.validation.Field;
import com.web2h.tools.StringTools;

/**
 * Venue entity class.
 * 
 * @author web2h
 */
@Entity
@Table(name = "references")
@DiscriminatorValue("VENUE")
public class Venue extends Reference {

	/** CITY - City where the venue is located. */
	@ManyToOne
	@JoinColumn(name = "city_id", nullable = false)
	@NotNull
	private City city;

	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(getNameEn());
		hcb.append(getNameFr());
		hcb.append(city);
		return hcb.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Venue)) {
			return false;
		}
		Venue that = (Venue) obj;
		EqualsBuilder eb = new EqualsBuilder();
		eb.append(getNameEn(), that.getNameEn());
		eb.append(getNameFr(), that.getNameFr());
		eb.append(city, that.city);
		return eb.isEquals();
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(super.toString());
		buffer.append(StringTools.getFieldAndValueToString(Field.CITY, city));
		return buffer.toString();
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}
}