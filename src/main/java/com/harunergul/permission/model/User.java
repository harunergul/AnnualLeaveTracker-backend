package com.harunergul.permission.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User extends AbstractUser implements UserDetails {

	private static final long serialVersionUID = 1L;

	@JsonIgnore
	private String password;

	@Column(name = "isenabled")
	private boolean isEnabled;

	public User() {
		super(null, null);
	}

	public User(String username, String password, ArrayList<Role> roles) {
		super(username, roles);
		this.password = password;

	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public List<Role> getAuthorities() {
		return this.roles;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

}
