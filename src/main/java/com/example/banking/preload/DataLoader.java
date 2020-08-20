package com.example.banking.preload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.banking.business.domain.User;
import com.example.banking.data.repository.UserRepository;

@Component
public class DataLoader implements CommandLineRunner {

	private UserRepository repository;

	@Autowired
	public DataLoader(UserRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		this.repository.save(
				new User("tirth123", "password", "tirth", "patel", "tirth12@gmail.com", "12345"));
	}

}
