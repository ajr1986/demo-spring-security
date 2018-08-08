package com.springboot.billing.app.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(@RequestParam(name = "error", required = false) String error,
			@RequestParam(name = "logout", required = false) String logout, Model model, Principal principal,
			RedirectAttributes flash) {

		if (principal != null) {
			// The user is currently logged
			flash.addFlashAttribute("info", "Session initialized previously");
			return "redirect:/";
		}

		if (error != null) {
			model.addAttribute("errorLogin", "Username or password invalid, please try again");
		}
		
		if (logout != null) {
			flash.addFlashAttribute("success", "Session was closed successfully");
			return "redirect:/";
		}

		return "login";
	}
}
