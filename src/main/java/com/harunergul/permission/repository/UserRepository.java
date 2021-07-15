package com.harunergul.permission.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.harunergul.permission.model.User;


public interface UserRepository extends CrudRepository<User, Integer>, JpaSpecificationExecutor<User> {

	List<User> findByUsername(String lastname);

	Optional<User> findUserById(Long id);
}
