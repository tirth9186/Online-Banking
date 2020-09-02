package com.example.banking.data.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.banking.data.entity.PrimaryTransaction;

public interface PrimaryTransactionRepository extends CrudRepository<PrimaryTransaction, Long> {

	@Override
	public List<PrimaryTransaction> findAll();
}
