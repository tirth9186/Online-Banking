package com.example.banking.business.service;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.banking.data.entity.PrimaryAccount;
import com.example.banking.data.entity.PrimaryTransaction;
import com.example.banking.data.entity.Recipient;
import com.example.banking.data.entity.SavingsAccount;
import com.example.banking.data.entity.SavingsTransaction;
import com.example.banking.data.entity.User;
import com.example.banking.data.repository.PrimaryAccountRepository;
import com.example.banking.data.repository.PrimaryTransactionRepository;
import com.example.banking.data.repository.RecipientRepository;
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

	@Autowired
	private RecipientRepository recipientRepository;

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

	public List<Recipient> findRecipientList(Principal principal) {
		String username  = principal.getName();
		List<Recipient> recipients = new ArrayList<>();
		List<Recipient> allRecipients = recipientRepository.findAll();
		for (Recipient itr : allRecipients) {
			if (itr.getUser().getUsername().equals(username)) {
				recipients.add(itr);
			}
		}
		return recipients;
	}

	public Recipient saveRecipient(Recipient rec) {
		return recipientRepository.save(rec);
	}

	public Recipient findRecipientByName(String name) {
		return recipientRepository.findByName(name);
	}
	
	public void deleteRecipientByName(String name) {
		recipientRepository.deleteByName(name);
	}

	public void recipientTransfer(Recipient recipient, String accountType, String amount, PrimaryAccount primaryAccount,
			SavingsAccount savingsAccount) throws Exception {
		if (accountType.equalsIgnoreCase("Primary") && recipient != null) {
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			Date date = new Date();

			PrimaryTransaction transaction = new PrimaryTransaction(date, "Transfer to " + recipient.getName(),
					"Transfer", "Finished", Double.parseDouble(amount), primaryAccount.getAccountBalance(),
					primaryAccount);

			primaryAccountRepository.save(primaryAccount);
			primaryTransactionRepository.save(transaction);

		} else if (accountType.equalsIgnoreCase("Savings") && recipient != null) {
			savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));

			Date date = new Date();

			SavingsTransaction transaction = new SavingsTransaction(date, "Transfer to " + recipient.getName(),
					"Transfer", "Finished", Double.parseDouble(amount), savingsAccount.getAccountBalance(),
					savingsAccount);

			savingsAccountRepository.save(savingsAccount);
			savingsTransactionRepository.save(transaction);

		} else {
			throw new Exception("Invalid Transfer!!!");
		}
	}

}
