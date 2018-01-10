package com.widzin.controllers;

import com.widzin.models.*;
import com.widzin.services.Club2Service;
import com.widzin.services.ClubSeasonService;
import com.widzin.services.SeasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ClubSeasonController {

    private ClubSeasonService clubSeasonService;
    private Club2Service club2Service;
    private SeasonService seasonService;

    @Autowired
    public void setClubSeasonService(ClubSeasonService clubSeasonService) {
        this.clubSeasonService = clubSeasonService;
    }

    @Autowired
    public void setClub2Service(Club2Service club2Service) {
        this.club2Service = club2Service;
    }

    @Autowired
    public void setSeasonService(SeasonService seasonService) {
        this.seasonService = seasonService;
    }

    @RequestMapping("/club/show/{id}")
    public String showClub(@PathVariable Integer id, Model model) {
        Club2 club2 = club2Service.getClub2ById(id);
        model.addAttribute("club", club2);

        ClubSeason lastClubSeason = clubSeasonService.getLastClubSeason(id);
        model.addAttribute("clubSeason", lastClubSeason);

        Double fullValueOfPlayers = 0.0;
        for (PlayerSeason playerSeason: lastClubSeason.getPlayers()) {
            fullValueOfPlayers += playerSeason.getValue();
        }

        fullValueOfPlayers /= Math.pow(10, 6);

        model.addAttribute("valueOfPlayers", fullValueOfPlayers);
        model.addAttribute("numberOfPlayers", lastClubSeason.getPlayers().size());

        /*List<Match> allThisClubMatches = new ArrayList<>();


        model.addAttribute("lastGames", clubSeasonService.getLastFiveMatches(clubSeasonService.getClubById(id)));
        model.addAttribute("nextGames", clubSeasonService.getNextMatchesForClub(clubSeasonService.getClubById(id)));*/
        return "clubshow";
    }
}
