package com.harunergul.permission.auth;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.harunergul.permission.model.User;
import com.harunergul.permission.repository.UserRepository;
 
 

/**
 * 
 * @author Harun ERGUL
 * @date May 8, 2020
 *
 */
@Service
public class ApplicationUserDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<User> userList = userRepository.findByUsername(username);
		if (userList != null && userList.size() > 0) {
			return userList.get(0);
		} else {
			throw new UsernameNotFoundException("Username not found!");
		}

	}

}
