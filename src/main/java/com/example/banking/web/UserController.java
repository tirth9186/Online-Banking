package com.example.banking.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.banking.business.service.UserService;
import com.example.banking.data.entity.PrimaryAccount;
import com.example.banking.data.entity.SavingsAccount;
import com.example.banking.data.entity.User;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public String profile(Principal principal, Model model) {
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);
		return "profile";
	}

	@RequestMapping(value = "/profile", method = RequestMethod.POST)
	public String profilePost(@ModelAttribute("user") User newUser, Model model) {

		User user = userService.findByUsername(newUser.getUsername());

		user.setUsername(newUser.getUsername());
		user.setFirstname(newUser.getFirstname());
		user.setLastname(newUser.getLastname());
		user.setEmail(newUser.getEmail());
		user.setPhone(newUser.getPhone());
		userService.saveUser(user);
		model.addAttribute("user", user);
		model.addAttribute("updated", true);
		return "profile";
	}

	@RequestMapping(value = "/accounts")
	public String userAccounts(Principal principal, Model model) {
		User user = userService.findByUsername(principal.getName());
		PrimaryAccount primaryAccount = user.getPrimaryAccount();
		SavingsAccount savingsAccount = user.getSavingsAccount();
		model.addAttribute("primaryAccount", primaryAccount);
		model.addAttribute("savingsAccount", savingsAccount);
		return "accounts";
	}


}
