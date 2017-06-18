package com.widzin.controllers;

import com.widzin.domain.BetGame;
import com.widzin.domain.Checked;
import com.widzin.domain.Result;
import com.widzin.domain.Ticket;
import com.widzin.services.BetService;
import com.widzin.services.GameService;
import com.widzin.services.TicketService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TicketController {

	private TicketService ticketService;
	private GameService gameService;
	private BetService betService;

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
	public String showChosenMatches(@ModelAttribute(value = "checked") Checked checked, Model model){
		List<Integer> checkedGames = checked.getCheckedGames();
		Ticket ticket = new Ticket();
		for (Integer i: checkedGames) {
			BetGame betGame = new BetGame();
			betGame.setOneGame(gameService.findById(i));
			ticket.addBets(betGame);
		}
		model.addAttribute("ticket", ticket);
		model.addAttribute("bets", ticket.getBets());
		model.addAttribute("options", ticketService.getAllOptions());
		return "chosengames";
	}

	@RequestMapping(value = "/ticket/makeFull", method = RequestMethod.POST)
	public ModelAndView createTicket(@RequestParam("result") List<Result> results, @RequestParam("money") String text){
		ModelAndView model = new ModelAndView("redirect:/?error");
		try {
			double money = Double.parseDouble(text);
			if (money > 0) {
				model = new ModelAndView("redirect:/");
			}
		} finally {
			return model;
		}
	}
}
