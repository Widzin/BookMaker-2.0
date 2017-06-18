package com.widzin.controllers;

import com.widzin.domain.Ticket;
import com.widzin.services.GameService;
import com.widzin.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class TicketController {

	private TicketService ticketService;
	private GameService gameService;

	@Autowired
	public void setTicketService (TicketService ticketService) {
		this.ticketService = ticketService;
	}

	@Autowired
	public void setGameService (GameService gameService) {
		this.gameService = gameService;
	}

	@RequestMapping("/ticket/new")
	public String showMatchesToBet(Model model){
		model.addAttribute("ticket", new Ticket());
		model.addAttribute("games", gameService.getNextMatches());
		model.addAttribute("betting", true);
		model.addAttribute("options", ticketService.getAllOptions());
		return "nextgames";
	}

	@RequestMapping(value = "/ticket/make", method = RequestMethod.POST)
	public String showChosenMatches(Model model, Ticket ticket){

		return "";
	}
}
