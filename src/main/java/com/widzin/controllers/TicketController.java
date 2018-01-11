package com.widzin.controllers;

import com.widzin.models.*;
import com.widzin.services.*;
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
	//private GameService gameService;
    private MatchService matchService;
	private BetService betService;
	private UserService userService;

	@Autowired
	public void setTicketService (TicketService ticketService) {
		this.ticketService = ticketService;
	}

//	@Autowired
//	public void setGameService (GameService gameService) {
//		this.gameService = gameService;
//	}

    @Autowired
    public void setMatchService(MatchService matchService) {
        this.matchService = matchService;
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
		model.addAttribute("matches", matchService.listAllNextMatches());
		model.addAttribute("betting", true);
		Checked checked = new Checked();
		List<Integer> list = new ArrayList<>();
		//list.add(0);
		checked.setCheckedGames(list);
		model.addAttribute("checked", checked);
		return "nextmatches";
	}

	private Logger log = Logger.getLogger(GameController.class);

	@RequestMapping(value = "/ticket/make", method = RequestMethod.POST)
	public String showChosenMatches(@ModelAttribute(value = "checked") Checked checked, Model model,
									Principal principal){
		List<Integer> checkedGames = checked.getCheckedGames();
		User user = userService.findByUsername(principal.getName()).get();
		Ticket ticket = new Ticket();
		for (Integer id: checkedGames) {
			BetGame betGame = new BetGame();
			betGame.setMatch(matchService.getMatchById(id));
            matchService.getMatchById(id).addBetGameList(betGame);
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
		model.addAttribute("user", user);
		if (user.getMoneyNow() == 0.0)
		    model.addAttribute("disabled", true);
		else
            model.addAttribute("disabled", false);
		return "chosenmatches";
	}

	@RequestMapping("/user/{id}/tickets")
	public String showUserTickets(@PathVariable("id") Integer id, Model model){
		User user = userService.getById(id);
		Iterable<Ticket> list;
		if (user.getId() != 1) {
			list = ticketService.getAllTicketsFromUser(user.getId());
		} else {
			list = ticketService.getAllTickets();
		}
		List<Ticket> tickets = new ArrayList<>();
		for(Ticket t: list){
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

	@RequestMapping("/user/{userId}/ticket/{ticketId}")
	public String showUserBetsDetails(@PathVariable("userId") Integer userId,
                                      @PathVariable("ticketId") Integer ticketId, Model model){
		User user = userService.getById(userId);
		for (Ticket t: user.getTickets()) {
			if (t.getId() == ticketId) {
				Ticket ticket = ticketService.findById(ticketId);
				model.addAttribute("bets", ticket.getBets());
				return "betshow";
			}
		}
		if (user.getId() == 1) {
			Ticket ticket = ticketService.findById(ticketId);
			model.addAttribute("bets", ticket.getBets());
			return "betshow";
		}
		return "redirect:/historyOfBets?error";
	}
}
