package com.widzin.controllers;

import com.widzin.bootstrap.SpringJpaBootstrap;
import com.widzin.domain.User;
import com.widzin.services.RoleService;
import com.widzin.services.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class UserController {

	Logger log = Logger.getLogger(SpringJpaBootstrap.class);

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

	@RequestMapping(value = "/newUser", method = RequestMethod.POST)
	public String saveUser(User user){
		user.addRole(roleService.getById(2));
		user.setMainRole(roleService.getById(2).getRole());
		userService.saveOrUpdate(user);
		return "redirect:/";
	}

	@RequestMapping("/user/show/{id}")
	public String showUser(@PathVariable Integer id, Model model) {
		model.addAttribute("user", userService.getById(id));
		return "usershow";
	}

	@RequestMapping("/profile")
	public String showProfile(Principal principal, Model model) {
		model.addAttribute("user", userService.findByUsername(principal.getName()));
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
	public String insertMoney(@RequestParam("insert") double money, @PathVariable Integer id){
		User user = userService.getById(id);
		user.setInsertedMoney(money);
		user.setMoneyNow(money);
		userService.saveOrUpdate(user);
		return "redirect:/profile";
	}
}
