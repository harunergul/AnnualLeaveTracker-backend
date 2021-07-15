package com.harunergul.permission.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.harunergul.permission.model.PartialUser; 

public interface PartialUserRepository extends CrudRepository<PartialUser, Long>, JpaSpecificationExecutor<PartialUser>{

	@Query(value = "SELECT * FROM user where username = ?1 AND is_Exist =TRUE ", nativeQuery = true)
	Optional<PartialUser> findUserByUsername(String username);

}
