package com.example.banking.business.service;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.banking.data.entity.PrimaryAccount;
import com.example.banking.data.entity.PrimaryTransaction;
//import com.example.banking.data.entity.PrimaryAccount;
//import com.example.banking.data.entity.PrimaryTransaction;
import com.example.banking.data.entity.SavingsAccount;
import com.example.banking.data.entity.SavingsTransaction;
import com.example.banking.data.entity.User;
import com.example.banking.data.repository.PrimaryAccountRepository;
//import com.example.banking.data.repository.PrimaryAccountRepository;
import com.example.banking.data.repository.SavingsAccountRepository;
import com.example.banking.data.repository.UserRepository;

@Service
public class AccountService {

	@Autowired
	PrimaryAccountRepository primaryAccountRepository;

	@Autowired
	SavingsAccountRepository savingsAccountRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	TransactionService transactionService;

	public static long nextAccountNumber = 1000;

	public long getAccountNumber() {
		return nextAccountNumber++;
	}

	public PrimaryAccount createPrimaryAccount() {
		long accountNumber = getAccountNumber();
		PrimaryAccount primaryAccount = new PrimaryAccount();
		primaryAccount.setAccountBalance(new BigDecimal(0));
		primaryAccount.setAccountNumber(accountNumber);

		return primaryAccountRepository.save(primaryAccount);

	}

	public SavingsAccount createSavingsAccount() {
		long accountNumber = getAccountNumber();
		SavingsAccount savingsAccount = new SavingsAccount();
		savingsAccount.setAccountBalance(new BigDecimal(0));
		savingsAccount.setAccountNumber(accountNumber);

		return savingsAccountRepository.save(savingsAccount);

	}

	public void deposit(String accountType, double amount, Principal principal) throws Exception {

		User user = userRepository.findByUsername(principal.getName());
		
		if(accountType.equalsIgnoreCase("Primary")) {
			PrimaryAccount primaryAccount = user.getPrimaryAccount();
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().add(new BigDecimal(amount)));
			primaryAccountRepository.save(primaryAccount);

			Date date = new Date();

			PrimaryTransaction primaryTransaction = new PrimaryTransaction(date, "Deposit in Primary account",
					"Account", "Finished", amount, primaryAccount.getAccountBalance(), primaryAccount);
			transactionService.savePrimaryDepositeTransaction(primaryTransaction);

		}
		else if(accountType.equalsIgnoreCase("Savings")) {
			SavingsAccount savingsAccount = user.getSavingsAccount();
			savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().add(new BigDecimal(amount)));
			savingsAccountRepository.save(savingsAccount);

			Date date = new Date();

			SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Deposit in Savings account",
					"Finished", "Account", amount, savingsAccount.getAccountBalance(), savingsAccount);
			transactionService.saveSavingsDepositeTransaction(savingsTransaction);
		}
		else {
			throw new Exception("Invalid Account Type..");
		}
		
	}

	public void withdraw(String accountType, double amount, Principal principal) throws Exception {

		User user = userRepository.findByUsername(principal.getName());

		if (accountType.equalsIgnoreCase("Primary")) {
			PrimaryAccount primaryAccount = user.getPrimaryAccount();
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			primaryAccountRepository.save(primaryAccount);

			Date date = new Date();

			PrimaryTransaction primaryTransaction = new PrimaryTransaction(date, "Withdraw from Primary account",
					"Account", "Finished", amount, primaryAccount.getAccountBalance(), primaryAccount);
			transactionService.savePrimaryWithdrawTransaction(primaryTransaction);

		} else if (accountType.equalsIgnoreCase("Savings")) {
			SavingsAccount savingsAccount = user.getSavingsAccount();
			savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			savingsAccountRepository.save(savingsAccount);

			Date date = new Date();

			SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Withdraw from Savings account",
					"Finished", "Account", amount, savingsAccount.getAccountBalance(), savingsAccount);
			transactionService.saveSavingsWithdrawTransaction(savingsTransaction);
		} else {
			throw new Exception("Invalid Account Type..");
		}
	}


}
