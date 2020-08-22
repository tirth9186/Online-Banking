package com.example.banking.web;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.banking.business.service.UserService;
import com.example.banking.data.entity.User;
import com.example.banking.data.entity.UserRole;


@Controller
public class HomeController {
	
	@Autowired
	private UserService userService;

	@RequestMapping(value= {"/","/index"})
	public String getHomePage() {
		return "index";
	}

	@RequestMapping(value = { "/signup" }, method = RequestMethod.GET)
	public String signup(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		model.addAttribute("registrationSuccess", false);
		model.addAttribute("registrationFail", false);

		return "signup";
	}

	@RequestMapping(value = { "/login" }, method = RequestMethod.GET)
	public String login(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		return "login";
	}

	@RequestMapping(value = { "/logout-success" }, method = RequestMethod.GET)
	public String logout() {
		return "logout-success";
	}

	@RequestMapping(value = { "/signup" }, method = RequestMethod.POST)
	public String postSignup(@ModelAttribute("user") User user, Model model) throws Exception {
		
		if (userService.isUserExist(user)) {
			if (userService.isUsernameExist(user.getUsername()))
				model.addAttribute("usernameExist", true);
			if (userService.isEmailExist(user.getEmail()))
				model.addAttribute("emailExist", true);
			if (userService.isPhoneExist(user.getPhone()))
				model.addAttribute("phoneExist", true);
			model.addAttribute("registrationFail", true);
			return "signup";
		}
		else {
			Set<UserRole> userRoles = new HashSet<>();
			userRoles.add(new UserRole(user.getUsername(), "USER"));
			User createdUser = userService.createUser(user, userRoles);
			if (createdUser == null)
				throw new Exception("Registration Failed!!!!");
			model.addAttribute("registrationSuccess", true);
			model.addAttribute("username", user.getUsername());
			return "index";
		}
	}

}

