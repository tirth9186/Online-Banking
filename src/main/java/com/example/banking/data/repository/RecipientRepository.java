package com.example.banking.data.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.banking.data.entity.Recipient;

public interface RecipientRepository extends CrudRepository<Recipient, Long> {

	@Override
	public List<Recipient> findAll();

	public Recipient findByName(String name);

	public void deleteByName(String name);

}
