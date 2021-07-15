package com.harunergul.permission.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class PartialUser extends AbstractUser {

	public PartialUser() {
		super(null, null);
	}
	public PartialUser(String userName, List<Role> roles) {
		super(userName, roles);

	}

}
