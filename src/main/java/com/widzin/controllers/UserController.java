package com.widzin.controllers;

import com.widzin.domain.Pager;
import com.widzin.domain.User;
import com.widzin.services.RoleService;
import com.widzin.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.swing.text.html.Option;
import java.security.Principal;
import java.util.Optional;

@Controller
public class UserController {

	private static final int BUTTONS_TO_SHOW = 5;
	private static final int INITIAL_PAGE = 0;
	private static final int INITIAL_PAGE_SIZE = 5;

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

	/*@RequestMapping(value = "/users", method = RequestMethod.GET)
	public String listUsers(Model model){
		model.addAttribute("users", userService.listAll());
		return "users";
	}*/

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public ModelAndView showUsersPage(@RequestParam("pagesize") Optional<Integer> pageSize,
									  @RequestParam("page") Optional<Integer> page) {
		ModelAndView modelAndView = new ModelAndView("users");

		// Evaluate page size. If requested parameter is null, return initial
		// page size
		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
		// Evaluate page. If requested parameter is null or less than 0 (to
		// prevent exception), return initial size. Otherwise, return value of
		// param. decreased by 1.
		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

		Page<User> users = userService.findAllPageable(new PageRequest(evalPage, evalPageSize));
		Pager pager = new Pager(users.getTotalPages(), users.getNumber(), BUTTONS_TO_SHOW);

		modelAndView.addObject("users", users);
		modelAndView.addObject("selectedPageSize", evalPageSize);
		modelAndView.addObject("pager", pager);
		return modelAndView;
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
