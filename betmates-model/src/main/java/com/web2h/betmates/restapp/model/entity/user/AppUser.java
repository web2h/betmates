package com.web2h.betmates.restapp.model.entity.user;

import static com.web2h.betmates.restapp.model.entity.FieldLength.EMAIL_MAX_LENGTH;
import static com.web2h.betmates.restapp.model.entity.FieldLength.NAME_MAX_LENGTH;
import static com.web2h.betmates.restapp.model.entity.FieldLength.PASSWORD_MAX_LENGTH;
import static com.web2h.betmates.restapp.model.entity.FieldLength.PASSWORD_MIN_LENGTH;
import static com.web2h.betmates.restapp.model.entity.FieldLength.ROLE_MAX_LENGTH;
import static com.web2h.betmates.restapp.model.entity.FieldLength.STATUS_MAX_LENGTH;
import static com.web2h.betmates.restapp.model.entity.FieldLength.TEXT_MIN_LENGTH;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.web2h.betmates.restapp.model.deserializer.JsonTrimmerDeserializer;
import com.web2h.betmates.restapp.model.validation.Field;
import com.web2h.betmates.restapp.model.validation.group.Creatable;
import com.web2h.betmates.restapp.model.validation.group.Editable;
import com.web2h.tools.StringTools;
import com.web2h.tools.authentication.PasswordFactory;
import com.web2h.utils.form.validator.annotation.Email;

/**
 * Application user entity class.
 * 
 * @author web2h
 */
@Entity
@Table(name = "app_users")
public class AppUser {

	/** ID - Internal person ID. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@NotNull(groups = Editable.class)
	@Null(groups = Creatable.class)
	protected Long id;

	/** EMAIL - User email address. */
	@Column(name = "email", length = EMAIL_MAX_LENGTH, unique = true)
	@NotNull
	@Email
	@Size(max = EMAIL_MAX_LENGTH)
	@JsonDeserialize(using = JsonTrimmerDeserializer.class)
	private String email;

	/** ALIAS - User's alias. */
	@Column(name = "alias", length = NAME_MAX_LENGTH, unique = true)
	@NotNull
	@Size(min = TEXT_MIN_LENGTH, max = NAME_MAX_LENGTH)
	@JsonDeserialize(using = JsonTrimmerDeserializer.class)
	private String alias;

	/** PASSWORD - User password. */
	@Column(name = "password", length = PASSWORD_MAX_LENGTH)
	@NotNull
	@Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
	private String password;

	/** STATUS - Status of the user. */
	@Enumerated(EnumType.STRING)
	@Column(name = "status", length = STATUS_MAX_LENGTH)
	@JsonIgnore
	private AppUserStatus status = AppUserStatus.NOT_CONFIRMED;

	/** ROLES - User role. */
	@Enumerated(EnumType.STRING)
	@Column(name = "role", length = ROLE_MAX_LENGTH)
	@NotNull
	private AppUserRole role = AppUserRole.ROLE_PLAYER;

	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(email);
		return hcb.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof AppUser)) {
			return false;
		}
		AppUser that = (AppUser) obj;
		EqualsBuilder eb = new EqualsBuilder();
		eb.append(email, that.email);
		return eb.isEquals();
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("APP USER");
		buffer.append(StringTools.getFieldAndValueToString(Field.ID, id));
		buffer.append(StringTools.getFieldAndValueToString(Field.EMAIL, email));
		buffer.append(StringTools.getFieldAndValueToString(Field.PASSWORD, PasswordFactory.maskPassword(password)));
		buffer.append(StringTools.getFieldAndValueToString(Field.ALIAS, alias));
		return buffer.toString();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public AppUserStatus getStatus() {
		return status;
	}

	public void setStatus(AppUserStatus status) {
		this.status = status;
	}

	public AppUserRole getRole() {
		return role;
	}

	public void setRole(AppUserRole role) {
		this.role = role;
	}
}