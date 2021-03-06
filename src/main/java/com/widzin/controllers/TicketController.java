package com.widzin.controllers;

import com.google.common.collect.Lists;
import com.widzin.models.*;
import com.widzin.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TicketController {

    private final static int ADMIN_ID = 1;

	private TicketService ticketService;
    private MatchService matchService;
	private BetService betService;
	private UserService userService;
	private SeasonService seasonService;

	@Autowired
	public void setTicketService (TicketService ticketService) {
		this.ticketService = ticketService;
	}

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

	@Autowired
    public void setSeasonService(SeasonService seasonService) {
        this.seasonService = seasonService;
    }

    @RequestMapping("/ticket/new")
	public String showMatchesToBet(Model model){
		model.addAttribute("matches", matchService.listAllNextMatches
                (Lists.newArrayList(seasonService.listAllSeasons())));
		model.addAttribute("betting", true);
		Checked checked = new Checked(new ArrayList<>());
		model.addAttribute("checked", checked);
		return "nextmatches";
	}

	@RequestMapping(value = "/ticket/prepare", method = RequestMethod.POST)
	public String showChosenMatches(@ModelAttribute(value = "checked") Checked checked,
                                    Model model, Principal principal){
		List<Integer> chosenMatchesId = checked.getCheckedGames();
		User user = userService.findByUsername(principal.getName()).get();
		Ticket ticket = new Ticket();
		for (Integer id: chosenMatchesId) {
			BetGame betGame = new BetGame();
			betGame.setMatch(matchService.getMatchById(id));
            matchService.getMatchById(id).addBetGameToList(betGame);
			betGame.setTicket(ticket);
			ticket.addBet(betGame);
			ticket.setTicketOwner(user);
		}
		ticketService.saveTicket(ticket);
        for (BetGame bg: ticket.getBets()){
            betService.saveBet(bg);
        }
		model.addAttribute("bets", ticket.getBets());
        model.addAttribute("ticket", ticket);
		model.addAttribute("options", ticketService.getAllOptions());
		model.addAttribute("user", user);
		if (user.getMoneyNow() == 0.0)
		    model.addAttribute("disabled", true);
		else
            model.addAttribute("disabled", false);
		return "chosenmatches";
	}

    @RequestMapping(value = "/ticket/create/{id}", method = RequestMethod.POST)
    public ModelAndView createTicket(@RequestParam("result") List<Result> results,
                                     @RequestParam("money") String text,
                                     @PathVariable Integer id,
                                     Principal principal){
        ModelAndView model = new ModelAndView("redirect:/?cannotParseMoney");
        try {
            Double money = Double.parseDouble(text);
            User user = userService.findByUsername(principal.getName()).get();
            Ticket ticket = ticketService.findById(id);
            if (money <= user.getMoneyNow()) {
                User admin = userService.getById(ADMIN_ID);
                ticket.setMoneyInserted(money * 0.88);
                admin.addMoneyNow(money * 0.12);
                userService.saveOrUpdate(admin);
                for (int i = 0; i < ticket.getBets().size(); i++) {
                    ticket.getBets().get(i).setBet(results.get(i));
                    betService.saveBet(ticket.getBets().get(i));
                }
                for (Match match: ticketService.getAllMatchesFromTicket(ticket)){
                    matchService.saveMatch(match);
                }
                ticket.calculateTicket();
                ticket.setTicketOwner(user);
                ticketService.saveTicket(ticket);
                user.addMoneyNow((-1)*money);
                user.addTickets(ticket);
                userService.saveOrUpdate(user);
                model = new ModelAndView("redirect:/?success");
            } else {
                model = new ModelAndView("redirect:/?toMuchMoney");
            }
        } finally {
            return model;
        }
    }
}
