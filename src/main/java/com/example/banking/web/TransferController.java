package com.example.banking.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.banking.business.service.TransactionService;
import com.example.banking.business.service.UserService;
import com.example.banking.data.entity.PrimaryAccount;
import com.example.banking.data.entity.SavingsAccount;
import com.example.banking.data.entity.User;

@Controller
@RequestMapping("/transfer")
public class TransferController {

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/betweenAccounts", method = RequestMethod.GET)
	public String betweenAccounts(Model model) {
		model.addAttribute("transferFrom", "");
		model.addAttribute("transferTo", "");
		model.addAttribute("amount", "");

		return "betweenAccounts";
	}

	@RequestMapping(value = "/betweenAccounts", method = RequestMethod.POST)
	public String betweenAccountsPost(@ModelAttribute("transferFrom") String transferFrom,
			@ModelAttribute("transferTo") String transferTo, @ModelAttribute("amount") String amount,
			Principal principal) throws Exception {
		User user = userService.findByUsername(principal.getName());
		PrimaryAccount primaryAccount = user.getPrimaryAccount();
		SavingsAccount savingsAccount = user.getSavingsAccount();
		transactionService.betweenAccountsTransfer(transferFrom, transferTo, amount, primaryAccount, savingsAccount);

		return "redirect:/user/accounts";
	}


	@RequestMapping(value = "/toSomeoneElse", method = RequestMethod.GET)
	public String toSomeoneElse(Model model, Principal principal) {

		model.addAttribute("accountNumber", "");
		model.addAttribute("accountType", "");
		model.addAttribute("error", false);
		model.addAttribute("errormsg", "");
		return "toSomeoneElse";
	}

	@RequestMapping(value = "/toSomeoneElse", method = RequestMethod.POST)
	public String toSomeoneElsePost(@ModelAttribute("accountNumber") String accountNumber,
			@ModelAttribute("accountType") String accountType, @ModelAttribute("amount") String amount,
			Principal principal, Model model) {
		User user = userService.findByUsername(principal.getName());
		PrimaryAccount destinationPrimaryAccount = null;
		SavingsAccount destinationSavingsAccount = null;
		if (transactionService.findPrimaryAccount(accountNumber) != null) {
			destinationPrimaryAccount = transactionService.findPrimaryAccount(accountNumber);
		}
		if (transactionService.findSavingsAccount(accountNumber) != null) {
			destinationSavingsAccount = transactionService.findSavingsAccount(accountNumber);
		}
		if (destinationPrimaryAccount == null && destinationSavingsAccount == null) {
			model.addAttribute("error", true);
			model.addAttribute("errormsg", "Invalid Account Number!!");
			model.addAttribute("accountNumber", accountNumber);
			model.addAttribute("accountType", "");

			return "toSomeoneElse";
		}
		transactionService.toSomeoneElseTransfer(destinationPrimaryAccount, destinationSavingsAccount, accountType,
				amount, user.getPrimaryAccount(),
				user.getSavingsAccount());

		return "redirect:/user/accounts";
	}
}
