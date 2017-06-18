package com.widzin.controllers;

import com.widzin.domain.*;
import com.widzin.services.BetService;
import com.widzin.services.GameService;
import com.widzin.services.TicketService;
import com.widzin.services.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TicketController {

	private TicketService ticketService;
	private GameService gameService;
	private BetService betService;
	private UserService userService;

	@Autowired
	public void setTicketService (TicketService ticketService) {
		this.ticketService = ticketService;
	}

	@Autowired
	public void setGameService (GameService gameService) {
		this.gameService = gameService;
	}

	@Autowired
	public void setBetService (BetService betService) {
		this.betService = betService;
	}

	@Autowired
	public void setUserService (UserService userService) {
		this.userService = userService;
	}

	@RequestMapping("/ticket/new")
	public String showMatchesToBet(Model model){
		model.addAttribute("games", gameService.getNextMatches());
		model.addAttribute("betting", true);
		Checked checked = new Checked();
		List<Integer> list = new ArrayList<>();
		list.add(0);
		checked.setCheckedGames(list);
		model.addAttribute("checked", checked);
		return "nextgames";
	}

	private Logger log = Logger.getLogger(GameController.class);

	@RequestMapping(value = "/ticket/make", method = RequestMethod.POST)
	public String showChosenMatches(@ModelAttribute(value = "checked") Checked checked, Model model,
									Principal principal){
		List<Integer> checkedGames = checked.getCheckedGames();
		User user = userService.findByUsername(principal.getName()).get();
		Ticket ticket = new Ticket();
		for (Integer i: checkedGames) {
			BetGame betGame = new BetGame();
			betGame.setOneGame(gameService.findById(i));
			gameService.findById(i).addBetGameList(betGame);
			betGame.setTicket(ticket);
			ticket.addBets(betGame);
			ticket.setTicketOwner(user);
		}
		ticketService.saveTicket(ticket);
		for (BetGame bg: ticket.getBets()){
			betService.saveBet(bg);
		}
		model.addAttribute("ticket", ticket);
		model.addAttribute("bets", ticket.getBets());
		model.addAttribute("options", ticketService.getAllOptions());
		return "chosengames";
	}

	@RequestMapping("/historyOfBets")
	public String showUserBets(Principal principal, Model model){
		User user = userService.findByUsername(principal.getName()).get();
		List<Ticket> tickets = new ArrayList<>();
		for(Ticket t: ticketService.getAllTicketsFromUser(user.getId())){
			if(t.getRate() != 1.0)
				tickets.add(t);
			else {
				for (BetGame bg: t.getBets()) {
					if (bg.getRate() == null)
						betService.deleteBet(bg);
				}
				ticketService.deleteTicket(t);
			}
		}
		model.addAttribute("tickets", tickets);
		model.addAttribute("user", user);
		return "ticketshow";
	}

	@RequestMapping("/historyOfBets/{id}")
	public String showUserBetsDetails(@PathVariable("id") Integer id, Principal principal, Model model){
		User user = userService.findByUsername(principal.getName()).get();
		for (Ticket t: user.getTickets()) {
			if (t.getId() == id) {
				Ticket ticket = ticketService.findById(id);
				model.addAttribute("bets", ticket.getBets());
				return "betshow";
			}
		}
		return "redirect:/historyOfBets?error";
	}
}
