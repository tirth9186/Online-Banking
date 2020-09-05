package com.example.banking.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.banking.business.service.TransactionService;
import com.example.banking.business.service.UserService;
import com.example.banking.data.entity.User;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private UserService userService;

	@Autowired
	private TransactionService transactionService;

	@RequestMapping(value = "/userList", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String getUserList(Model model) {
		List<User> allUsers = userService.findAllUsers();


		List<User> users = new ArrayList<>();
		allUsers.forEach((user)->{
			user.getUserRoles().forEach((role) -> {
				if (role.getRole().equalsIgnoreCase("user")) {
					users.add(user);
				}
			});
		});

		model.addAttribute("users", users);
		return "userList";
	}

	@RequestMapping(value = "/userDelete", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String delete(@RequestParam("username") String username, Model model) {
		User user = userService.findByUsername(username);
		if (user != null) {
			user.setEnabled(!user.isEnabled());
			userService.saveUser(user);
		}
		return "redirect:/admin/userList";
	}

	@RequestMapping(value = "/transactions", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String transactionsList(Model model) {

		model.addAttribute("primaryTransactions", transactionService.findAllPrimaryTransactions());
		model.addAttribute("savingsTransactions", transactionService.findAllSavingsTransactions());

		return "transactions";
	}

}
