package com.harunergul.permission.util;

import org.springframework.data.jpa.domain.Specification;

import com.harunergul.permission.model.User;

public class UserSpecs {

	public static Specification<User> existingUsers() {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isExist"), true);
	}

}
