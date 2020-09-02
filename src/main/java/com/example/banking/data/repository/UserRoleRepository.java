package com.example.banking.data.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.banking.data.entity.User;
import com.example.banking.data.entity.UserRole;

public interface UserRoleRepository extends CrudRepository<UserRole, Long> {

	List<UserRole> findByUser(User user);

}
