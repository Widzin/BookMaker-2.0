package com.widzin.controllers;

import com.widzin.bootstrap.SpringJpaBootstrap;
import com.widzin.domain.Club;
import com.widzin.domain.Game;
import com.widzin.services.ClubService;
import com.widzin.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
	public String saveGame(Game game){
		if (game.getHome().getId() == game.getAway().getId()){
			return "redirect:/game/new";
		} else {
			gameService.saveMatch(game);
			return "redirect:/";
		}
	}
}
