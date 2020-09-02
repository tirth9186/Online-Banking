package com.example.banking.web;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.banking.business.service.AccountService;
import com.example.banking.business.service.TransactionService;
import com.example.banking.business.service.UserService;
import com.example.banking.data.entity.PrimaryAccount;
import com.example.banking.data.entity.PrimaryTransaction;
import com.example.banking.data.entity.SavingsAccount;
import com.example.banking.data.entity.SavingsTransaction;
import com.example.banking.data.entity.User;

@Controller
@RequestMapping("/account")
public class AccountController {

	@Autowired
	private UserService userServie;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private AccountService accountService;

	@RequestMapping("/primaryaccount")
	public String primaryAccount(Model model, Principal principal) {
		String username = principal.getName();
		User user = userServie.findByUsername(username);
		PrimaryAccount primaryAccount = user.getPrimaryAccount();
		List<PrimaryTransaction> primaryTransactionList = transactionService.findPrimaryTransactionList(username);
		model.addAttribute("primaryAccount", primaryAccount);
		model.addAttribute("primaryTransactionList", primaryTransactionList);
		return "primaryaccount";
	}

	@RequestMapping("/savingsaccount")
	public String savingsAccount(Model model, Principal principal) {
		String username = principal.getName();
		User user = userServie.findByUsername(username);
		SavingsAccount savingsAccount = user.getSavingsAccount();
		List<SavingsTransaction> savingsTransactionList = transactionService.findSavingsTransactionList(username);
		model.addAttribute("savingsAccount", savingsAccount);
		model.addAttribute("savingsTransactionList", savingsTransactionList);
		return "savingsaccount";
	}

	@RequestMapping(value = "/deposit", method = RequestMethod.GET)
	public String deposit(Model model) {

		model.addAttribute("amount", "");
		model.addAttribute("accountType", "");

		return "deposit";
	}

	@RequestMapping(value = "/deposit",method = RequestMethod.POST)
	public String depositPost(@ModelAttribute("amount") String amount,
			@ModelAttribute("accountType") String accountType, Principal principal, Model model)
			throws NumberFormatException, Exception {

		accountService.deposit(accountType, Double.parseDouble(amount), principal);

		if(accountType.equalsIgnoreCase("Primary")) {
			PrimaryAccount primaryAccount = userServie.findByUsername(principal.getName()).getPrimaryAccount();
			model.addAttribute("primaryAccount", primaryAccount);
			return "primaryaccount";
		}
		else if (accountType.equalsIgnoreCase("Savings")) {
			SavingsAccount savingsAccount = userServie.findByUsername(principal.getName()).getSavingsAccount();
			model.addAttribute("savingsAccount", savingsAccount);
			return "savingsaccount";
		}
		return "redirect:/user/accounts";
	}

	@RequestMapping(value = "/withdraw", method = RequestMethod.GET)
	public String withdraw(Model model) {

		model.addAttribute("amount", "");
		model.addAttribute("accountType", "");

		return "withdraw";
	}

	@RequestMapping(value = "/withdraw", method = RequestMethod.POST)
	public String withdrawPost(@ModelAttribute("amount") String amount,
			@ModelAttribute("accountType") String accountType, Principal principal, Model model)
			throws NumberFormatException, Exception {

		accountService.withdraw(accountType, Double.parseDouble(amount), principal);

		if (accountType.equalsIgnoreCase("Primary")) {
			PrimaryAccount primaryAccount = userServie.findByUsername(principal.getName()).getPrimaryAccount();
			model.addAttribute("primaryAccount", primaryAccount);
			return "primaryaccount";
		} else if (accountType.equalsIgnoreCase("Savings")) {
			SavingsAccount savingsAccount = userServie.findByUsername(principal.getName()).getSavingsAccount();
			model.addAttribute("savingsAccount", savingsAccount);
			return "savingsaccount";
		}
		return "redirect:/user/accounts";
	}

}
