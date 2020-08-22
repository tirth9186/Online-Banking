package com.example.banking.business.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.banking.data.entity.User;
import com.example.banking.data.entity.UserRole;
import com.example.banking.data.repository.UserRepository;
import com.example.banking.data.repository.UserRoleRepository;

@Service
public class UserService {

	private static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(11);
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserRoleRepository userRoleRepository;

	public boolean isUserExist(User user) {
		return (isUsernameExist(user.getUsername()) || isEmailExist(user.getEmail()) || isPhoneExist(user.getPhone()));
	}

	public boolean isUsernameExist(String username) {

		return (userRepository.findByUsername(username) == null) ? false : true;
	}

	public boolean isEmailExist(String email) {

		return (userRepository.findByEmail(email) == null) ? false : true;
	}

	public boolean isPhoneExist(String phone) {

		return (userRepository.findByPhone(phone) == null) ? false : true;
	}

	public User createUser(User user, Set<UserRole> userRoles) {
		User localUser = null;
		String encryptedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encryptedPassword);

		for (UserRole ur : userRoles) {
			userRoleRepository.save(ur);
		}

		localUser = userRepository.save(user);
		return localUser;
	}

}
