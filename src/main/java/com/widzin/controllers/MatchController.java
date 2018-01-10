package com.widzin.controllers;

import com.widzin.models.ClubSeason;
import com.widzin.models.Match;
import com.widzin.models.TeamMatchDetails;
import com.widzin.services.ClubSeasonService;
import com.widzin.services.MatchService;
import com.widzin.services.SeasonService;
import com.widzin.services.TeamMatchDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class MatchController {

    private final static int LAST_ROUND = 34;
    private final static int MAX_MATCHES_IN_ROUND = 9;

    private MatchService matchService;
    private TeamMatchDetailsService teamMatchDetailsService;
    private ClubSeasonService clubSeasonService;
    private SeasonService seasonService;

    @Autowired
    public void setMatchService(MatchService matchService) {
        this.matchService = matchService;
    }

    @Autowired
    public void setTeamMatchDetailsService(TeamMatchDetailsService teamMatchDetailsService) {
        this.teamMatchDetailsService = teamMatchDetailsService;
    }

    @Autowired
    public void setClubSeasonService(ClubSeasonService clubSeasonService) {
        this.clubSeasonService = clubSeasonService;
    }

    @Autowired
    public void setSeasonService(SeasonService seasonService) {
        this.seasonService = seasonService;
    }

    @RequestMapping("/game/new")
    public String newGame(Model model){
        List<ClubSeason> lastClubSeasons = seasonService.getLastSeason().getClubs();

        model.addAttribute("match", new Match());
        model.addAttribute("clubSeasons", lastClubSeasons);
        return "gameform";
    }

    @RequestMapping(value = "/createGame", method = RequestMethod.POST)
    public String saveGame(@RequestParam("dataText") String text,
                           @RequestParam("homeTeam") Integer homeTeamId,
                           @RequestParam("awayTeam") Integer awayTeamId,
                           Match match){
        if (homeTeamId == awayTeamId){
            return "redirect:/game/new?errorId";
        } else {
            try {
                match.getHome().setClubSeason(
                        clubSeasonService.getClubSeasonById(homeTeamId));
                match.getAway().setClubSeason(
                        clubSeasonService.getClubSeasonById(awayTeamId));

                teamMatchDetailsService.saveTeamMatchDetails(match.getHome());
                teamMatchDetailsService.saveTeamMatchDetails(match.getAway());

                Match lastMatch = matchService.getLastMatch();
                if (lastMatch.getRound() < LAST_ROUND)
                    match.setPeriod(lastMatch.getPeriod());

                Integer matchesInThisRound = matchService
                        .listAllMatchesFromPeriodAndRoundSize(
                                lastMatch.getPeriod(),
                                lastMatch.getRound());

                if (matchesInThisRound == MAX_MATCHES_IN_ROUND)
                    match.setRound(lastMatch.getRound() + 1);
                else
                    match.setRound(lastMatch.getRound());

                DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                Date date = format.parse(text);
                match.setDate(date);

                matchService.saveMatch(match);
                return "redirect:/game/next";
            } catch (ParseException ex) {
                return "redirect:/game/new?errorDt";
            }
        }
    }


}