package com.harunergul.permission.util;

import org.springframework.data.jpa.domain.Specification;

import com.harunergul.permission.model.PartialUser;
import com.harunergul.permission.model.PermissionRequest;

public class PermissionRequestSpecs {
	public static Specification<PermissionRequest> exist() {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), "1");
	}
	
	public static Specification<PermissionRequest> userIs(PartialUser user) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user"), user);
	}

	public static Specification<PermissionRequest> notIncludeRequest(Long permissionRequestId) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get("id"), permissionRequestId);
	}
	
 

	
	
}
