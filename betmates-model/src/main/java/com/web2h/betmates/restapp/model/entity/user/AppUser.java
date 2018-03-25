package com.web2h.betmates.restapp.model.entity.user;

import static com.web2h.betmates.restapp.model.entity.FieldLength.EMAIL_MAX_LENGTH;
import static com.web2h.betmates.restapp.model.entity.FieldLength.NAME_MAX_LENGTH;
import static com.web2h.betmates.restapp.model.entity.FieldLength.PASSWORD_MAX_LENGTH;
import static com.web2h.betmates.restapp.model.entity.FieldLength.STATUS_MAX_LENGTH;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.web2h.betmates.restapp.model.validation.Field;
import com.web2h.tools.StringTools;
import com.web2h.tools.authentication.PasswordFactory;

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
	protected Long id;

	/** EMAIL - User email address. */
	@Column(name = "email", length = EMAIL_MAX_LENGTH, unique = true)
	private String email;

	/** ALIAS - User's alias. */
	@Column(name = "alias", length = NAME_MAX_LENGTH, unique = true)
	private String alias;

	/** PASSWORD - User password. */
	@Column(name = "password", length = PASSWORD_MAX_LENGTH)
	private String password;

	/** STATUS - Status of the user. */
	@Enumerated(EnumType.STRING)
	@Column(name = "status", length = STATUS_MAX_LENGTH)
	private AppUserStatus status = AppUserStatus.NOT_CONFIRMED;
	
	/** ROLES - User role. */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private Set<AppUserRole> roles = new HashSet<>();

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

	public Set<AppUserRole> getRoles() {
		return roles;
	}
}