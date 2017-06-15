package com.widzin.controllers;

import com.widzin.domain.Club;
import com.widzin.domain.Game;
import com.widzin.services.ClubService;
import com.widzin.services.GameService;
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
import java.util.Date;

@Controller
public class GameController {

	private GameService gameService;
	private ClubService clubService;

	@Autowired
	public void setGameService (GameService gameService) {
		this.gameService = gameService;
	}

	@Autowired
	public void setClubService (ClubService clubService) {
		this.clubService = clubService;
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
				return "redirect:/?success";
			} catch (ParseException ex) {
				return "redirect:/game/new?errorDt";
			}
		}
	}
}
