package com.web2h.betmates.restapp.model.entity.reference;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Country entity class.
 * 
 * @author web2h
 */
@Entity
@Table(name = "basic_references")
@DiscriminatorValue("COUNTRY")
public class Country extends Reference {

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("COUNTRY");
		buffer.append(super.toString());
		return buffer.toString();
	}
}