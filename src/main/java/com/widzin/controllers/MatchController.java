package com.widzin.controllers;

import com.widzin.models.Club2;
import com.widzin.models.ClubSeason;
import com.widzin.models.Match;
import com.widzin.models.Season;
import com.widzin.services.Club2Service;
import com.widzin.services.ClubSeasonService;
import com.widzin.services.GameService;
import com.widzin.services.SeasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MatchController {

    private GameService gameService;
    private SeasonService seasonService;

    @Autowired
    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }

    @Autowired
    public void setSeasonService(SeasonService seasonService) {
        this.seasonService = seasonService;
    }

    @RequestMapping("/game/new")
    public String newGame(Model model){
        List<ClubSeason> lastClubSeasons = seasonService.getLastSeason().getClubs();
        List<Club2> club2s = new ArrayList<>();

        for (ClubSeason clubSeason: lastClubSeasons) {
            club2s.add(clubSeason.getClub2());
        }

        model.addAttribute("match", new Match());
        model.addAttribute("clubs", club2s);
        return "gameform";
    }
}
