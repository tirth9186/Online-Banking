package com.example.banking.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USER_ROLE")
public class UserRole {

	@Id
	@Column(name = "ROLE_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Column(name = "USERNAME")
	private String username;
	@Column(name = "ROLE")
	private String role;

	public UserRole(String username, String role) {
		super();
		this.username = username;
		this.role = role;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
