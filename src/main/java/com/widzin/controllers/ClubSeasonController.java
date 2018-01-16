package com.widzin.controllers;

import com.widzin.models.*;
import com.widzin.services.ClubService;
import com.widzin.services.ClubSeasonService;
import com.widzin.services.MatchService;
import com.widzin.services.SeasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;

@Controller
public class ClubSeasonController {

    private ClubSeasonService clubSeasonService;
    private ClubService clubService;
    private MatchService matchService;
    private SeasonService seasonService;

    @Autowired
    public void setClubSeasonService(ClubSeasonService clubSeasonService) {
        this.clubSeasonService = clubSeasonService;
    }

    @Autowired
    public void setClubService(ClubService clubService) {
        this.clubService = clubService;
    }

    @Autowired
    public void setMatchService(MatchService matchService) {
        this.matchService = matchService;
    }

    @Autowired
    public void setSeasonService(SeasonService seasonService) {
        this.seasonService = seasonService;
    }

    @RequestMapping("/club/show/{id}")
    public String showClub(@PathVariable Integer id, Model model) {
        Club club = clubService.getClubById(id);
        model.addAttribute("club", club);

        ClubSeason lastClubSeason = clubSeasonService.getLastClubSeason(id);
        model.addAttribute("clubSeason", lastClubSeason);

        Double fullValueOfPlayers = 0.0;
        if (lastClubSeason.getPlayers().size() > 0) {
            for (PlayerSeason playerSeason: lastClubSeason.getPlayers()) {
                fullValueOfPlayers += playerSeason.getValue();
            }
        }
        fullValueOfPlayers /= Math.pow(10, 6);

        model.addAttribute("valueOfPlayers", fullValueOfPlayers);
        model.addAttribute("numberOfPlayers", lastClubSeason.getPlayers().size());

        List<Match> allThisClubMatches = matchService.listAllPlayedMatchesWithClub(id);
        if (allThisClubMatches.size() > 5) {
            List<Match> lastFiveGames = allThisClubMatches.subList(
                    allThisClubMatches.size() - 5,
                    allThisClubMatches.size());
            Collections.reverse(lastFiveGames);
            model.addAttribute("lastMatches", lastFiveGames);
        } else {
            if (allThisClubMatches.size() > 1) {
                Collections.reverse(allThisClubMatches);
            }
            model.addAttribute("lastMatches", allThisClubMatches);
        }

        model.addAttribute("nextMatches", matchService.listAllNotPlayedMatchesWithClub(id));
        return "clubshow";
    }

    @RequestMapping("/table")
    public String showTable(Model model){
        Season lastSeason = seasonService.getLastSeason();
        List<ClubSeason> clubSeasons = lastSeason.getClubs();
        model.addAttribute("clubs", clubSeasonService.sortListForTable(clubSeasons));
        model.addAttribute("season", lastSeason.getPeriod());
        return "table";
    }

    @RequestMapping("/table/{seasonId}")
    public String showTableFromSeason(@PathVariable int seasonId, Model model){
        Season season = seasonService.getSeasonById(seasonId);
        Set<ClubSeason> clubSeasonsSet = new HashSet<>(season.getClubs());
        List<ClubSeason> clubSeasons = new ArrayList<>(clubSeasonsSet);
        model.addAttribute("clubs", clubSeasonService.sortListForTable(clubSeasons));
        model.addAttribute("season", season.getPeriod());
        return "table";
    }

    @RequestMapping(value = "/club/{id}/history", method = RequestMethod.POST)
    public String showHistory(@PathVariable Integer id, Model model){
        List<Match> allThisClubMatches = matchService.listAllMatchesWithClub(id);
        Collections.reverse(allThisClubMatches);
        model.addAttribute("club", clubService.getClubById(id));
        model.addAttribute("allMatches", allThisClubMatches);
        return "history";
    }
}
