package com.example.banking.data.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.banking.data.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {

	User findByUsername(String username);

	User findByEmail(String email);

	@Override
	List<User> findAll();

	User findByPhone(String phone);
}
