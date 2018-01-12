package com.widzin.controllers;

import com.widzin.models.*;
import com.widzin.services.Club2Service;
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
    private Club2Service club2Service;
    private MatchService matchService;
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
    public void setMatchService(MatchService matchService) {
        this.matchService = matchService;
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
        System.out.println("");
        model.addAttribute("clubs", clubSeasonService.sortListForTable(clubSeasons));
        model.addAttribute("season", season.getPeriod());
        return "table";
    }

    @RequestMapping(value = "/club/{id}/history", method = RequestMethod.POST)
    public String showHistory(@PathVariable Integer id, Model model){
        List<Match> allThisClubMatches = matchService.listAllMatchesWithClub(id);
        Collections.reverse(allThisClubMatches);
        model.addAttribute("club", club2Service.getClub2ById(id));
        model.addAttribute("allMatches", allThisClubMatches);
        return "history";
    }
}
