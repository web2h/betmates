package com.web2h.betmates.restapp.model.entity.reference;

import static com.web2h.betmates.restapp.model.entity.FieldLength.NAME_MAX_LENGTH;
import static com.web2h.betmates.restapp.model.entity.FieldLength.TEXT_MIN_LENGTH;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.web2h.betmates.restapp.model.deserializer.JsonTrimmerDeserializer;
import com.web2h.betmates.restapp.model.validation.Field;
import com.web2h.betmates.restapp.model.validation.group.CreationChecks;
import com.web2h.betmates.restapp.model.validation.group.EditionChecks;
import com.web2h.tools.StringTools;

/**
 * Basic reference entity class. Common class for all reference data.
 * 
 * @author web2h
 */
@Entity
@Table(name = "basic_references")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "discriminator", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue(value = "REFERENCE")
public abstract class Reference {

	/** ID - Internal reference ID. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@NotNull(groups = EditionChecks.class)
	@Null(groups = CreationChecks.class)
	protected Long id;

	/** NAME_EN - Reference name in English. */
	@Column(name = "name_en", length = NAME_MAX_LENGTH, nullable = false)
	@NotNull
	@Size(min = TEXT_MIN_LENGTH, max = NAME_MAX_LENGTH)
	@JsonDeserialize(using = JsonTrimmerDeserializer.class)
	private String nameEn;

	/** NAME_FR - Reference name in French. */
	@Column(name = "name_fr", length = NAME_MAX_LENGTH, nullable = false)
	@NotNull
	@Size(min = TEXT_MIN_LENGTH, max = NAME_MAX_LENGTH)
	@JsonDeserialize(using = JsonTrimmerDeserializer.class)
	private String nameFr;

	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(nameEn);
		hcb.append(nameFr);
		return hcb.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Reference)) {
			return false;
		}
		Reference that = (Reference) obj;
		EqualsBuilder eb = new EqualsBuilder();
		eb.append(nameEn, that.nameEn);
		eb.append(nameFr, that.nameFr);
		return eb.isEquals();
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(StringTools.getFieldAndValueToString(Field.ID, id));
		buffer.append(StringTools.getFieldAndValueToString(Field.NAME_EN, nameEn));
		buffer.append(StringTools.getFieldAndValueToString(Field.NAME_FR, nameFr));
		return buffer.toString();
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
}