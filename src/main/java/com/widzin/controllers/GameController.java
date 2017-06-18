package com.widzin.controllers;

import com.widzin.domain.*;
import com.widzin.services.BetService;
import com.widzin.services.ClubService;
import com.widzin.services.GameService;
import com.widzin.services.TicketService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class GameController {

	private GameService gameService;
	private ClubService clubService;
	private TicketService ticketService;
	private BetService betService;

	@Autowired
	public void setGameService (GameService gameService) {
		this.gameService = gameService;
	}

	@Autowired
	public void setClubService (ClubService clubService) {
		this.clubService = clubService;
	}

	@Autowired
	public void setTicketService (TicketService ticketService) {
		this.ticketService = ticketService;
	}

	@Autowired
	public void setBetService (BetService betService) {
		this.betService = betService;
	}

	@RequestMapping("/game/new")
	public String newGame(Model model){
		model.addAttribute("game", new Game());
		model.addAttribute("clubs", clubService.getListOfCurrentClubs());
		return "gameform";
	}

	@RequestMapping(value = "/createGame", method = RequestMethod.POST)
	public String saveGame(@RequestParam("text") String text, Game game){
		if (game.getHome().getId() == game.getAway().getId()){
			return "redirect:/game/new?errorId";
		} else {
			try {
				DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
				Date date = format.parse(text);
				game.setDate(date);
				gameService.saveMatch(game);
				Club home = clubService.getClubById(game.getHome().getId());
				Club away = clubService.getClubById(game.getAway().getId());
				home.addGameAtHome(game);
				away.addGameAway(game);
				clubService.saveClub(home);
				clubService.saveClub(away);
				return "redirect:/game/next";
			} catch (ParseException ex) {
				return "redirect:/game/new?errorDt";
			}
		}
	}

	@RequestMapping("/game/next")
	public String showNextGames(Model model){
		model.addAttribute("games", gameService.getNextMatches());
		model.addAttribute("betting", false);
		model.addAttribute("ticket", new Ticket());
		return "nextgames";
	}

	@RequestMapping("/game/play/{id}")
	public String playGame(@PathVariable Integer id, Model model){
		model.addAttribute("game", gameService.findById(id));
		return "gameplay";
	}

	@RequestMapping(value = "/game/play/{id}", method = RequestMethod.POST)
	public String setScoresInGame(@PathVariable Integer id, @RequestParam("homeScore") Integer homeScore, @RequestParam("awayScore") Integer awayScore) {
		if (homeScore != null && awayScore != null) {
			updateClubsAfterMatch(id, homeScore, awayScore);
			return "redirect:/?success";
		} else {
			return "redirect:/game/play/" + id + "?error";
		}
	}

	private void updateClubsAfterMatch(Integer id, Integer homeScore, Integer awayScore){
		Game game = gameService.findById(id);
		game.setHomeScore(homeScore);
		game.setAwayScore(awayScore);
		game.setPlayed(true);
		updateBets(game);
		gameService.saveMatch(game);
		Club home = clubService.getClubById(game.getHome().getId());
		Club away = clubService.getClubById(game.getAway().getId());
		home.updateStats(homeScore, awayScore);
		away.updateStats(awayScore, homeScore);
		clubService.saveClub(home);
		clubService.saveClub(away);
		Calculations calculations = Calculations.getInstance();
		calculations.addNumberOfAllMatches();
		calculations.addAllGoalsScoredAtHome(homeScore);
		calculations.addAllGoalsLostAtHome(awayScore);
	}

	private void updateBets(Game game){
		List<BetGame> bets = new ArrayList<>();
		for (Ticket t: ticketService.getAllTicketsWithMatch(game)){
			bets.addAll(betService.getBetsFromGameAndTicket(game, t));
		}
		for (BetGame bg: bets){
			switch (bg.getResult()){
				case home:
					if (game.getHomeScore() > game.getAwayScore())
						bg.setMatched(true);
					else
						bg.setMatched(false);
					break;
				case guest:
					if (game.getHomeScore() < game.getAwayScore())
						bg.setMatched(true);
					else
						bg.setMatched(false);
					break;
				case draw:
					if (game.getHomeScore() == game.getAwayScore())
						bg.setMatched(true);
					else
						bg.setMatched(false);
					break;
			}
			betService.saveBet(bg);
		}

	}

	private Logger log = Logger.getLogger(GameController.class);

	@RequestMapping("/game/between/{text}")
	public String showHistoryBetween(@PathVariable("text") String between, Model model){
		String[] parts = between.split("i");
		Integer idHome = Integer.parseInt(parts[0]);
		Integer idAway = Integer.parseInt(parts[1]);
		model.addAttribute("games", gameService.listMatchesBetween(clubService.getClubById(idHome), clubService.getClubById(idAway)));
		model.addAttribute("home", clubService.getClubById(idHome));
		model.addAttribute("away", clubService.getClubById(idAway));
		return "between";
	}

}
