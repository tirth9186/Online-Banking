package com.example.banking.data.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface PrimaryTransactionRepository extends CrudRepository<PrimaryAccountRepository, Long> {

	@Override
	public List<PrimaryAccountRepository> findAll();

}
