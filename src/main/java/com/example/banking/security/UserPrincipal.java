package com.example.banking.security;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.banking.data.entity.User;
import com.example.banking.data.entity.UserRole;

public class UserPrincipal implements UserDetails {

	private User user;
	private List<UserRole> userRoles;
	@Autowired
	public UserPrincipal(User user, List<UserRole> userRoles) {
		super();
		this.user = user;
		this.userRoles = userRoles;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (userRoles == null)
			return Collections.emptySet();
		Set<SimpleGrantedAuthority> grantedAuthority = new HashSet<>();
		userRoles.forEach(userRole -> {
			grantedAuthority.add(new SimpleGrantedAuthority(userRole.getRole()));
		});
		return grantedAuthority;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
