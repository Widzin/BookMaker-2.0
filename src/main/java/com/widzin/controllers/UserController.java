package com.widzin.controllers;

import com.widzin.domain.User;
import com.widzin.services.RoleService;
import com.widzin.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class UserController {

	private UserService userService;
	private RoleService roleService;

	@Autowired
	public void setUserService (UserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setRoleService (RoleService roleService) {
		this.roleService = roleService;
	}

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public String listUsers(Model model){
		model.addAttribute("users", userService.listAll());
		return "users";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(){
		return "login";
	}

	@RequestMapping("/register")
	public String register(Model model) {
		model.addAttribute("user", new User());
		return "register";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ModelAndView saveUser(User user, BindingResult result){
		ModelAndView model = new ModelAndView("register");
		if (userService.findByUsername(user.getUsername()).isPresent()){
			result.rejectValue("username", "error");
			model.addObject("user", user);
			return model;
		} else {
			user.addRole(roleService.getById(2));
			user.setMainRole(roleService.getById(2).getRole());
			userService.saveOrUpdate(user);
			return new ModelAndView("redirect:/login");
		}
	}

	@RequestMapping("/user/show/{id}")
	public String showUser(@PathVariable Integer id, Model model) {
		model.addAttribute("user", userService.getById(id));
		return "usershow";
	}

	@RequestMapping("/profile")
	public String showProfile(Principal principal, Model model) {
		model.addAttribute("user", userService.findByUsername(principal.getName()).get());
		return "usershow";
	}

	@RequestMapping("/user/edit/{id}")
	public String editUser(@PathVariable Integer id, Model model) {
		model.addAttribute("user", userService.getById(id));
		return "userform";
	}

	@RequestMapping(value = "/editUser", method = RequestMethod.POST)
	public String editUser(User user){
		userService.saveOrUpdate(user);
		return "redirect:/user/show/" + user.getId();
	}

	@RequestMapping("user/delete/{id}")
	public String deleteUser(@PathVariable Integer id){
		userService.delete(id);
		return "redirect:/users";
	}

	@RequestMapping(value = "/insertMoney/{id}", method = RequestMethod.POST)
	public ModelAndView insertMoney(@RequestParam("insert") String text, @PathVariable Integer id){
		ModelAndView model = new ModelAndView("redirect:/profile");
		try {
			double money = Double.parseDouble(text);
			if (money > 0) {
				User user = userService.getById(id);
				user.setInsertedMoney(money);
				user.setMoneyNow(money);
				userService.saveOrUpdate(user);
			}
		} finally {
			return model;
		}
	}
}
