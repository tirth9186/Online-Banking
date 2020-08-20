package com.example.banking.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.banking.data.entity.User;
import com.example.banking.data.entity.UserRole;
import com.example.banking.data.repository.UserRepository;
import com.example.banking.data.repository.UserRoleRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private UserRepository userRepository;
	private UserRoleRepository userRoleRepository;
	@Autowired
	public CustomUserDetailsService(UserRepository userRepository, UserRoleRepository userRoleRepository) {
		super();
		this.userRepository = userRepository;
		this.userRoleRepository = userRoleRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user = userRepository.findByUsername(username);
		UserPrincipal userPrinicpal = null;
		
		if(user==null) {
			throw new UsernameNotFoundException("Not able to find the :"+username);
		}
		List<UserRole> userRoles = this.userRoleRepository.findByUsername(username);
		userPrinicpal = new UserPrincipal(user, userRoles);
		
		return userPrinicpal;
	}

}
