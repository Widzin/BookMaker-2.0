package com.widzin.controllers;

import com.widzin.domain.*;
import com.widzin.services.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
public class UserController {

	private UserService userService;
	private RoleService roleService;
	private TicketService ticketService;
	private BetService betService;
	private GameService gameService;

	@Autowired
	public void setUserService (UserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setRoleService (RoleService roleService) {
		this.roleService = roleService;
	}

	@Autowired
	public void setTicketService (TicketService ticketService) {
		this.ticketService = ticketService;
	}

	@Autowired
	public void setBetService (BetService betService) {
		this.betService = betService;
	}

	@Autowired
	public void setGameService (GameService gameService) {
		this.gameService = gameService;
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
			Double money = Double.parseDouble(text);
			if (money > 0) {
				User user = userService.getById(id);
				user.setInsertedMoney(money);
				user.addMoneyNow(money);
				userService.saveOrUpdate(user);
			}
		} finally {
			return model;
		}
	}

	private Logger log = Logger.getLogger(GameController.class);

	@RequestMapping(value = "/ticket/makeFull", method = RequestMethod.POST)
	public ModelAndView createTicket(@RequestParam("result") List<Result> results, @RequestParam("money") String text,
									 Principal principal, Ticket ticket){
		ModelAndView model = new ModelAndView("redirect:/?error");
		try {
			Double money = Double.parseDouble(text);
			User user = userService.findByUsername(principal.getName()).get();
			log.info("Pieniadze na koncie: " + user.getMoneyNow());
			if (money < user.getMoneyNow()) {
				ticket.setMoneyInserted(money);
				for (int i = 0; i < ticket.getBets().size(); i++) {
					ticket.getBets().get(i).setBet(results.get(i));
					betService.saveBet(ticket.getBets().get(i));
				}
				for (Game g: ticketService.getAllGamesFromTicket(ticket)){
					gameService.saveMatch(g);
				}
				ticket.calculateTicket();
				ticket.setTicketOwner(user);
				ticketService.saveTicket(ticket);
				user.setMoneyNow(user.getMoneyNow() - money);
				user.addTickets(ticket);
				userService.saveOrUpdate(user);
				model = new ModelAndView("redirect:/?success");
			}
		} finally {
			return model;
		}
	}
}
