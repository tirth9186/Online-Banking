package com.example.banking.business.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.banking.data.entity.User;
import com.example.banking.data.entity.UserRole;
import com.example.banking.data.repository.UserRepository;

@Service
@Transactional
public class UserService {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	AccountService accountService;

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

		user.setUserRoles(userRoles);

		user.setPrimaryAccount(accountService.createPrimaryAccount());
		user.setSavingsAccount(accountService.createSavingsAccount());
		user.getPrimaryAccount().setUser(user);
		user.getSavingsAccount().setUser(user);
		localUser = userRepository.save(user);
		return localUser;
	}

	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public User saveUser(User user) {
		return userRepository.save(user);
	}

	public void deleteUser(User user) {
		userRepository.delete(user);
	}

	public List<User> findAllUsers() {
		return userRepository.findAll();
	}


}
