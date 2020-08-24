package com.example.banking.data.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.banking.data.entity.PrimaryAccount;

public interface PrimaryAccountRepository extends CrudRepository<PrimaryAccount, Long> {

	public PrimaryAccount findByAccountNumber(int accountNumber);

}
