package com.example.banking.business.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.banking.data.entity.PrimaryAccount;
import com.example.banking.data.entity.PrimaryTransaction;
import com.example.banking.data.entity.SavingsAccount;
import com.example.banking.data.entity.SavingsTransaction;
import com.example.banking.data.entity.User;
import com.example.banking.data.repository.PrimaryAccountRepository;
import com.example.banking.data.repository.PrimaryTransactionRepository;
import com.example.banking.data.repository.SavingsAccountRepository;
import com.example.banking.data.repository.SavingsTransactionRepository;
import com.example.banking.data.repository.UserRepository;

@Service
public class TransactionService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PrimaryAccountRepository primaryAccountRepository;

	@Autowired
	private PrimaryTransactionRepository primaryTransactionRepository;

	@Autowired
	private SavingsAccountRepository savingsAccountRepository;

	@Autowired
	private SavingsTransactionRepository savingsTransactionRepository;

	public List<PrimaryTransaction> findAllPrimaryTransactions() {
		return primaryTransactionRepository.findAll();
	}

	public List<SavingsTransaction> findAllSavingsTransactions() {
		return savingsTransactionRepository.findAll();
	}

	public List<PrimaryTransaction> findPrimaryTransactionList(String username) {
		User user = userRepository.findByUsername(username);
		List<PrimaryTransaction> list = user.getPrimaryAccount().getPrimaryTransactionList();
		return list;
	}

	public List<SavingsTransaction> findSavingsTransactionList(String username) {
		User user = userRepository.findByUsername(username);
		List<SavingsTransaction> list = user.getSavingsAccount().getSavingsTransactionList();
		return list;
	}

	public void savePrimaryDepositeTransaction(PrimaryTransaction primaryTransaction) {
		primaryTransactionRepository.save(primaryTransaction);
	}

	public void savePrimaryWithdrawTransaction(PrimaryTransaction primaryTransaction) {
		primaryTransactionRepository.save(primaryTransaction);
	}

	public void saveSavingsDepositeTransaction(SavingsTransaction savingsTransaction) {
		savingsTransactionRepository.save(savingsTransaction);
	}

	public void saveSavingsWithdrawTransaction(SavingsTransaction savingsTransaction) {
		savingsTransactionRepository.save(savingsTransaction);
	}

	public void betweenAccountsTransfer(String transferFrom, String transferTo, String amount,
			PrimaryAccount primaryAccount, SavingsAccount savingsAccount) throws Exception {
		if (transferFrom.equalsIgnoreCase("Primary") && transferTo.equalsIgnoreCase("Savings")) {
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().add(new BigDecimal(amount)));

			Date date = new Date();

			PrimaryTransaction transaction = new PrimaryTransaction(date,
					"Accounts Transfer from " + transferFrom + " to " + transferTo + ".", "Account", "Finished",
					Double.parseDouble(amount), primaryAccount.getAccountBalance(), primaryAccount);

			primaryAccountRepository.save(primaryAccount);
			savingsAccountRepository.save(savingsAccount);
			primaryTransactionRepository.save(transaction);

		} else if (transferFrom.equalsIgnoreCase("Savings") && transferTo.equalsIgnoreCase("Primary")) {
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().add(new BigDecimal(amount)));
			savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));

			Date date = new Date();

			SavingsTransaction transaction = new SavingsTransaction(date,
					"Accounts Transfer from " + transferFrom + " to " + transferTo + ".", "Account", "Finished",
					Double.parseDouble(amount), savingsAccount.getAccountBalance(), savingsAccount);

			primaryAccountRepository.save(primaryAccount);
			savingsAccountRepository.save(savingsAccount);
			savingsTransactionRepository.save(transaction);

		} else {
			throw new Exception("Invalid Transfer!!!");
		}
	}




	public PrimaryAccount findPrimaryAccount(String accountNumber) {
		return primaryAccountRepository.findByAccountNumber(Long.parseLong(accountNumber));
	}

	public SavingsAccount findSavingsAccount(String accountNumber) {
		return savingsAccountRepository.findByAccountNumber(Long.parseLong(accountNumber));
	}


	public void toSomeoneElseTransfer(PrimaryAccount destinationPrimaryAccount,
			SavingsAccount destinationSavingsAccount, String accountType, String amount,
			PrimaryAccount primaryAccount, SavingsAccount savingsAccount) {

		if (accountType.equalsIgnoreCase("Primary")) {
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));

			Date date = new Date();



			if (destinationPrimaryAccount != null) {

				PrimaryTransaction primaryTransaction = new PrimaryTransaction(date,
						"Transfer to recipient " + destinationPrimaryAccount.getUser().getUsername(), "Transfer",
						"Finished", Double.parseDouble(amount), primaryAccount.getAccountBalance(), primaryAccount);

				destinationPrimaryAccount
						.setAccountBalance(destinationPrimaryAccount.getAccountBalance().add(new BigDecimal(amount)));
				PrimaryTransaction destTransaction = new PrimaryTransaction(date,
						"Received from " + primaryAccount.getUser().getUsername(), "Transfer", "Finished",
						Double.parseDouble(amount), destinationPrimaryAccount.getAccountBalance(),
						destinationPrimaryAccount);
				primaryAccountRepository.save(destinationPrimaryAccount);
				primaryTransactionRepository.save(destTransaction);
				primaryTransactionRepository.save(primaryTransaction);
			} else {

				PrimaryTransaction primaryTransaction = new PrimaryTransaction(date,
						"Transfer to recipient " + destinationSavingsAccount.getUser().getUsername(), "Transfer",
						"Finished", Double.parseDouble(amount), primaryAccount.getAccountBalance(), primaryAccount);

				destinationSavingsAccount
						.setAccountBalance(destinationSavingsAccount.getAccountBalance().add(new BigDecimal(amount)));
				SavingsTransaction destTransaction = new SavingsTransaction(date,
						"Received from " + primaryAccount.getUser().getUsername(), "Finished", "Transfer",
						Double.parseDouble(amount), destinationSavingsAccount.getAccountBalance(),
						destinationSavingsAccount);
				savingsAccountRepository.save(destinationSavingsAccount);
				savingsTransactionRepository.save(destTransaction);
				primaryTransactionRepository.save(primaryTransaction);
			}
			primaryAccountRepository.save(primaryAccount);

		} else if (accountType.equalsIgnoreCase("Savings")) {
			savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));

			Date date = new Date();

			if (destinationPrimaryAccount != null) {

				SavingsTransaction savingsTransaction = new SavingsTransaction(date,
						"Transfer to recipient " + destinationPrimaryAccount.getUser().getUsername(), "Finished",
						"Transfer", Double.parseDouble(amount), savingsAccount.getAccountBalance(), savingsAccount);

				destinationPrimaryAccount
						.setAccountBalance(destinationPrimaryAccount.getAccountBalance().add(new BigDecimal(amount)));

				PrimaryTransaction destTransaction = new PrimaryTransaction(date,
						"Received from " + savingsAccount.getUser().getUsername(), "Transfer", "Finished",
						Double.parseDouble(amount), destinationPrimaryAccount.getAccountBalance(),
						destinationPrimaryAccount);
				primaryAccountRepository.save(destinationPrimaryAccount);
				primaryTransactionRepository.save(destTransaction);
				savingsTransactionRepository.save(savingsTransaction);
			} else {

				SavingsTransaction savingsTransaction = new SavingsTransaction(date,
						"Transfer to recipient " + destinationSavingsAccount.getUser().getUsername(), "Finished",
						"Transfer", Double.parseDouble(amount), savingsAccount.getAccountBalance(), savingsAccount);

				destinationSavingsAccount
						.setAccountBalance(destinationSavingsAccount.getAccountBalance().add(new BigDecimal(amount)));

				SavingsTransaction destTransaction = new SavingsTransaction(date,
						"Received from " + savingsAccount.getUser().getUsername(), "Finished", "Transfer",
						Double.parseDouble(amount), destinationSavingsAccount.getAccountBalance(),
						destinationSavingsAccount);

				savingsAccountRepository.save(destinationSavingsAccount);
				savingsTransactionRepository.save(destTransaction);
				savingsTransactionRepository.save(savingsTransaction);
			}
			savingsAccountRepository.save(savingsAccount);
		}
	}

}
