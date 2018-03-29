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
 * City entity class.
 * 
 * @author web2h
 */
@Entity
@Table(name = "references")
@DiscriminatorValue("CITY")
public class City extends Reference {

	/** COUNTRY - Country where the city is located. */
	@ManyToOne
	@JoinColumn(name = "country_id")
	@NotNull
	private Country country;

	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(getNameEn());
		hcb.append(getNameFr());
		hcb.append(country);
		return hcb.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof City)) {
			return false;
		}
		City that = (City) obj;
		EqualsBuilder eb = new EqualsBuilder();
		eb.append(getNameEn(), that.getNameEn());
		eb.append(getNameFr(), that.getNameFr());
		eb.append(country, that.country);
		return eb.isEquals();
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("CITY");
		buffer.append(super.toString());
		buffer.append(StringTools.getFieldAndValueToString(Field.COUNTRY, country));
		return buffer.toString();
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}
}