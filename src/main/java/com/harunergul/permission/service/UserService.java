package com.harunergul.permission.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harunergul.permission.model.PartialUser;
import com.harunergul.permission.model.User;
import com.harunergul.permission.repository.PartialUserRepository;
import com.harunergul.permission.repository.UserRepository;
import com.harunergul.permission.util.UserNotFoundException;
import com.harunergul.permission.util.UserSpecs;

@Service
public class UserService {

	private final UserRepository userRepo;
	private final PartialUserRepository partialUserRepo;

	@Autowired
	public UserService(UserRepository userRepo, PartialUserRepository partialUserRepo) {
		this.userRepo = userRepo;
		this.partialUserRepo = partialUserRepo;
	}

	public User addUser(User user) {
		user.setExist(true);
		user.setHireDate(new Date());
		return userRepo.save(user);
	}
	
	public User updateUser(User user) {
 		return userRepo.save(user);
	}
	
	public User getUserById(Long id) {
		return userRepo.findUserById(id)
				.orElseThrow(() -> new UserNotFoundException("User by id" + id + " was not found"));
	}
	
	public PartialUser getUserByUsername(String username) {
		return partialUserRepo.findUserByUsername(username)
				.orElseThrow(() -> new UserNotFoundException("User by username: " + username + " was not found"));
	}
	
	public PartialUser getParitalUser(Long id) {
		return partialUserRepo.findById(id).get();
				 
	}
	
	public Iterable<User> getAllUsers() {
		return userRepo.findAll(UserSpecs.existingUsers());
	}
	
	public void deleteUserById(Long id) {
		User user = getUserById(id);
		user.setExist(false);
		updateUser(user);
	}

}
