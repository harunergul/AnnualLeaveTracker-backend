package com.harunergul.permission.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.harunergul.permission.model.User;
import com.harunergul.permission.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public ResponseEntity<Iterable<User>> getAllUsers() {
		Iterable<User> users = this.userService.getAllUsers();
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@GetMapping("/find/{id}")
	public ResponseEntity<Iterable<User>> getUserById(@PathVariable("id") Long id) {
		this.userService.getUserById(id);
		Iterable<User> users = this.userService.getAllUsers();
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@PostMapping("/add")
	public ResponseEntity<User> addUser(@RequestBody User user) {
		User newUser = this.userService.addUser(user);
		return new ResponseEntity<User>(newUser, HttpStatus.OK);

	}

	@PostMapping("/update")
	public ResponseEntity<User> updateUser(@RequestBody User user) {
		User updatedUser = this.userService.updateUser(user);
		return new ResponseEntity<User>(updatedUser, HttpStatus.OK);

	}

	@PostMapping("/delete/{id}")
	public ResponseEntity<User> updateUser(@PathVariable("id") Long id) {
		this.userService.deleteUserById(id);
		return new ResponseEntity<User>(HttpStatus.OK);

	}

}
