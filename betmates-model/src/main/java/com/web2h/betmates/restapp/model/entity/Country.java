package com.web2h.betmates.restapp.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Country entity class.
 *
 * @author web2h
 */
@Entity
@Table(name = "countries")
public class Country {

	/** ID - Internal country ID. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/** NAME_EN - Country English name. */
	@Column(name = "name_en", length = 128)
	private String nameEn;

	/** NAME_FR - Country French name. */
	@Column(name = "name_fr", length = 128)
	private String nameFr;

	/** ISO_CODE_3 - Country ISO code on 3 characters. */
	@Column(name = "iso_code_3", length = 3)
	private String isoCode3;

	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(nameEn);
		hcb.append(nameFr);
		hcb.append(isoCode3);
		return hcb.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Country)) {
			return false;
		}
		Country that = (Country) obj;
		EqualsBuilder eb = new EqualsBuilder();
		eb.append(nameEn, that.nameEn);
		eb.append(nameFr, that.nameFr);
		eb.append(isoCode3, that.isoCode3);
		return eb.isEquals();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getNameFr() {
		return nameFr;
	}

	public void setNameFr(String nameFr) {
		this.nameFr = nameFr;
	}

	public String getIsoCode3() {
		return isoCode3;
	}

	public void setIsoCode3(String isoCode3) {
		this.isoCode3 = isoCode3;
	}
}