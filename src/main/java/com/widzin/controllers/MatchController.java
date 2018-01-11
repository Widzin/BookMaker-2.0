package com.widzin.controllers;

import com.widzin.models.ClubSeason;
import com.widzin.models.Match;
import com.widzin.services.*;
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
import java.util.List;

@Controller
public class MatchController {

    private final static int LAST_ROUND = 34;
    private final static int MAX_MATCHES_IN_ROUND = 9;

    private MatchService matchService;
    private TeamMatchDetailsService teamMatchDetailsService;
    private Club2Service club2Service;
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
    public void setClub2Service(Club2Service club2Service) {
        this.club2Service = club2Service;
    }

    @Autowired
    public void setClubSeasonService(ClubSeasonService clubSeasonService) {
        this.clubSeasonService = clubSeasonService;
    }

    @Autowired
    public void setSeasonService(SeasonService seasonService) {
        this.seasonService = seasonService;
    }

    @RequestMapping("/match/new")
    public String newGame(Model model){
        List<ClubSeason> lastClubSeasons = seasonService.getLastSeason().getClubs();

        model.addAttribute("match", new Match());
        model.addAttribute("clubSeasons", lastClubSeasons);
        return "matchform";
    }

    @RequestMapping(value = "/match/create", method = RequestMethod.POST)
    public String saveGame(@RequestParam("dataText") String text,
                           @RequestParam("homeTeam") Integer homeTeamId,
                           @RequestParam("awayTeam") Integer awayTeamId,
                           Match match){
        if (homeTeamId == awayTeamId){
            return "redirect:/match/new?errorId";
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
                return "redirect:/match/next";
            } catch (ParseException ex) {
                return "redirect:/match/new?errorDt";
            }
        }
    }

    @RequestMapping("/match/next")
    public String showNextGames(Model model){
        model.addAttribute("matches", matchService.listAllNextMatches());
        model.addAttribute("betting", false);
        return "nextmatches";
    }

    @RequestMapping("/match/play/{id}")
    public String playGame(@PathVariable Integer id, Model model){
        model.addAttribute("match", matchService.getMatchById(id));
        return "matchplay";
    }

    @RequestMapping(value = "/match/play/{id}", method = RequestMethod.POST)
    public String setScoresInGame(@PathVariable Integer id, @RequestParam("homeScore") Integer homeScore,
                                  @RequestParam("awayScore") Integer awayScore) {
        if (homeScore != null && awayScore != null) {
            matchService.updateClubsAfterMatch(id, homeScore, awayScore);
            return "redirect:/?matchPlayed";
        } else {
            return "redirect:/game/play/" + id + "?error";
        }
    }

    @RequestMapping("/match/between/{idHome}/{idAway}")
    public String showHistoryBetween(@PathVariable("idHome") Integer homeId,
                                     @PathVariable("idAway") Integer awayId,
                                     Model model){
        model.addAttribute("matches", matchService.listAllMatchesBetween(homeId, awayId));
        model.addAttribute("home", club2Service.getClub2ById(homeId));
        model.addAttribute("away", club2Service.getClub2ById(awayId));
        return "between";
    }
}
