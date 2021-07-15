package com.harunergul.permission.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.harunergul.permission.model.PermissionRequest;

public interface PermissionRequestRepository extends CrudRepository<PermissionRequest, Long>, JpaSpecificationExecutor<PermissionRequest> {

}
