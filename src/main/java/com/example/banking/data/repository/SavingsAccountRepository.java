package com.example.banking.data.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.banking.data.entity.SavingsAccount;

public interface SavingsAccountRepository extends CrudRepository<SavingsAccount, Long> {

	public SavingsAccount findByAccountNumber(long accountNumber);
}
