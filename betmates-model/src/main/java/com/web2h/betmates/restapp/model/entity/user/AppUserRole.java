package com.web2h.betmates.restapp.model.entity.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Application user role entity class.
 * 
 * @author web2h
 */
@Entity
@Table(name = "app_user_roles")
public class AppUserRole {
	
	/** ID - Internal ID. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;
	
	/** USER - Application user. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "app_user_id", nullable = false)
	private AppUser user;

	/** ROLE - Role nqme. */
	@Enumerated(EnumType.STRING)
	@Column(name = "role")
	private Role role;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AppUser getUser() {
		return user;
	}

	public void setUser(AppUser user) {
		this.user = user;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
