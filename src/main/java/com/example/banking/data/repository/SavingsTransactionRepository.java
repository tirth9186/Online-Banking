package com.example.banking.data.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.banking.data.entity.SavingsTransaction;

public interface SavingsTransactionRepository extends CrudRepository<SavingsTransaction, Long> {
	@Override
	public List<SavingsTransaction> findAll();
}
