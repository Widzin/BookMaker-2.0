package com.widzin.controllers;

import com.google.common.collect.Lists;
import com.widzin.models.*;
import com.widzin.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Controller
public class MatchController {

    private final static int LAST_ROUND = 34;
    private final static int MAX_MATCHES_IN_ROUND = 9;
    private final static int ADMIN_ID = 1;

    private MatchService matchService;
    private MatchEventService matchEventService;
    private TeamMatchDetailsService teamMatchDetailsService;
    private ClubService clubService;
    private ClubSeasonService clubSeasonService;
    private SeasonService seasonService;
    private BetService betService;
    private TicketService ticketService;
    private UserService userService;
    private PlayerSeasonService playerSeasonService;

    private MyNeuralNetwork myNeuralNetwork;

    @Autowired
    public void setMatchService(MatchService matchService) {
        this.matchService = matchService;
    }

    @Autowired
    public void setMatchEventService(MatchEventService matchEventService) {
        this.matchEventService = matchEventService;
    }

    @Autowired
    public void setTeamMatchDetailsService(TeamMatchDetailsService teamMatchDetailsService) {
        this.teamMatchDetailsService = teamMatchDetailsService;
    }

    @Autowired
    public void setClubService(ClubService clubService) {
        this.clubService = clubService;
    }

    @Autowired
    public void setClubSeasonService(ClubSeasonService clubSeasonService) {
        this.clubSeasonService = clubSeasonService;
    }

    @Autowired
    public void setSeasonService(SeasonService seasonService) {
        this.seasonService = seasonService;
    }

    @Autowired
    public void setBetService(BetService betService) {
        this.betService = betService;
    }

    @Autowired
    public void setTicketService(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setPlayerSeasonService(PlayerSeasonService playerSeasonService) {
        this.playerSeasonService = playerSeasonService;
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
        model.addAttribute("matches", matchService.listAllNextMatches
                (Lists.newArrayList(seasonService.listAllSeasons())));
        model.addAttribute("betting", false);
        return "nextmatches";
    }

    @RequestMapping("/match/play/{id}")
    public String playGame(@PathVariable Integer id, Model model){
        model.addAttribute("match", matchService.getMatchById(id));
        return "matchplay";
    }

    @RequestMapping(value = "/match/play/{id}", method = RequestMethod.POST)
    public String setScoresInGame(@PathVariable Integer id) {
        myNeuralNetwork = MyNeuralNetwork.getInstance();
        Match match = matchService.getMatchById(id);
        double[] outputs = myNeuralNetwork.calculateAndReceiveOutputs(match);
        int[] results = generateResults(outputs);
        generateEvents(results, match);
        playerSeasonService.updatePlayersAfterMatch(match, results[0], results[1]);
        matchService.updateClubsAfterMatch(id, results[0], results[1]);
        List<Ticket> ticketsWithMatch = ticketService.getAllTicketsWithMatch
                (matchService.getMatchById(id));
        betService.saveBetsAfterMatch(ticketsWithMatch,
                matchService.getMatchById(id));
        saveTickets(ticketsWithMatch);
        return "redirect:/match/" + id + "/details";
    }

    private int[] generateResults(double[] outputs) {
        int[] results = new int[2];
        double fullAmount = 0.0, temp = 0.0;
        for (double d: outputs) {
            fullAmount += d;
        }

        int option = -1;
        double randValue = Math.random() * fullAmount;
        for (int i = 0; i < outputs.length; i++) {
            temp += outputs[i];
            if (temp >= randValue) {
                option = i;
                break;
            }
        }

        if (option == 0) {
            results[1] = ThreadLocalRandom.current().nextInt(0, 3);
            results[0] = ThreadLocalRandom.current().nextInt(results[1] + 1, 5);
        } else if (option == 2) {
            results[0] = ThreadLocalRandom.current().nextInt(0, 3);
            results[1] = ThreadLocalRandom.current().nextInt(results[0] + 1, 5);
        } else {
            results[0] = ThreadLocalRandom.current().nextInt(0, 4);
            results[1] = results[0];
        }

        return results;
    }

    private void generateEvents(int[] results, Match match) {
        for (int i = 0; i < results.length; i++) {
            int minute = 0;
            if (results[i] > 0) {
                for (int j = 0; j < results[i]; j++) {
                    double line = Math.random();
                    if (minute < 89)
                        minute = ThreadLocalRandom.current().nextInt(minute + 1, 90);
                    else
                        minute = 90;

                    int playerId;
                    MatchEvent matchEvent = new MatchEvent();
                    matchEvent.setAdditionalInformation("goal");
                    matchEvent.setMinute(minute);

                    PlayerSeason playerSeason;

                    if (line < 0.2) {
                        if (i == 0) {
                            playerId = ThreadLocalRandom.current().nextInt(0, match.getHome().getLineupDefense().size());
                            playerSeason = match.getHome().getLineupDefense().get(playerId);
                            matchEvent.setPlayerSeason(playerSeason);
                            matchEventService.saveMatchEvent(matchEvent);
                            match.getHome().getGoalDetails().add(matchEvent);
                        } else {
                            playerId = ThreadLocalRandom.current().nextInt(0, match.getAway().getLineupDefense().size());
                            playerSeason = match.getAway().getLineupDefense().get(playerId);
                            matchEvent.setPlayerSeason(playerSeason);
                            matchEventService.saveMatchEvent(matchEvent);
                            match.getAway().getGoalDetails().add(matchEvent);
                        }
                    } else if (line < 0.55) {
                        if (i == 0) {
                            playerId = ThreadLocalRandom.current().nextInt(0, match.getHome().getLineupMidfield().size());
                            playerSeason = match.getHome().getLineupMidfield().get(playerId);
                            matchEvent.setPlayerSeason(playerSeason);
                            matchEventService.saveMatchEvent(matchEvent);
                            match.getHome().getGoalDetails().add(matchEvent);
                        } else {
                            playerId = ThreadLocalRandom.current().nextInt(0, match.getAway().getLineupMidfield().size());
                            playerSeason = match.getAway().getLineupMidfield().get(playerId);
                            matchEvent.setPlayerSeason(playerSeason);
                            matchEventService.saveMatchEvent(matchEvent);
                            match.getAway().getGoalDetails().add(matchEvent);
                        }
                    } else {
                        if (i == 0) {
                            playerId = ThreadLocalRandom.current().nextInt(0, match.getHome().getLineupForward().size());
                            playerSeason = match.getHome().getLineupForward().get(playerId);
                            matchEvent.setPlayerSeason(playerSeason);
                            matchEventService.saveMatchEvent(matchEvent);
                            match.getHome().getGoalDetails().add(matchEvent);
                        } else {
                            playerId = ThreadLocalRandom.current().nextInt(0, match.getAway().getLineupForward().size());
                            playerSeason = match.getAway().getLineupForward().get(playerId);
                            matchEvent.setPlayerSeason(playerSeason);
                            matchEventService.saveMatchEvent(matchEvent);
                            match.getAway().getGoalDetails().add(matchEvent);
                        }
                    }
                }
            }

            if (i == 0)
                teamMatchDetailsService.saveTeamMatchDetails(match.getHome());
            else
                teamMatchDetailsService.saveTeamMatchDetails(match.getAway());
        }
    }

    private void saveTickets(List<Ticket> tickets) {
        for(Ticket t: tickets){
            int i = 0;
            do {
                if (t.getBets().get(i).isMatched() == null){
                    break;
                }
                i++;
            } while (i < t.getBets().size());
            if (i == t.getBets().size()){
                t.setFinished(true);
                int j;
                for (j = 0; j < t.getBets().size(); j++){
                    if (!t.getBets().get(j).isMatched())
                        break;
                }
                User user = userService.getById(t.getTicketOwner().getId());
                User admin = userService.getById(ADMIN_ID);
                if (j == t.getBets().size()){
                    Double fullMoneyWon = t.getMoneyToWin();
                    Double moneyForUser = fullMoneyWon;
                    Double moneyForAdmin = fullMoneyWon;
                    user.addWinMoney(moneyForUser);
                    user.addMoneyNow(moneyForUser);
                    admin.addLostMoney(moneyForUser - moneyForAdmin);
                    admin.addMoneyNow(moneyForAdmin - moneyForUser);
                    t.setWin(true);
                } else {
                    user.addLostMoney(t.getMoneyInserted());
                    admin.addWinMoney(t.getMoneyInserted());
                    admin.addMoneyNow(t.getMoneyInserted());
                    t.setWin(false);
                }
                ticketService.saveTicket(t);
                userService.saveOrUpdate(user);
                userService.saveOrUpdate(admin);
            }
        }
    }

    @RequestMapping("/match/between/{idHome}/{idAway}")
    public String showHistoryBetween(@PathVariable("idHome") Integer homeId,
                                     @PathVariable("idAway") Integer awayId,
                                     Model model){
        model.addAttribute("matches", matchService.listAllMatchesBetween(homeId, awayId));
        model.addAttribute("home", clubService.getClubById(homeId));
        model.addAttribute("away", clubService.getClubById(awayId));
        return "between";
    }

    @RequestMapping("/match/{matchId}/details")
    public String showMatchDetails(@PathVariable("matchId") Integer matchId, Model model) {
        Match match = matchService.getMatchById(matchId);
        model.addAttribute("match", match);

        Comparator<MatchEvent> c = (p, o) -> p.getMinute().compareTo(o.getMinute());

        List<PlayerSeason> homePlayers = new ArrayList<>();
        homePlayers.add(match.getHome().getLineupGoalkeeper());
        homePlayers.addAll(match.getHome().getLineupDefense());
        homePlayers.addAll(match.getHome().getLineupMidfield());
        homePlayers.addAll(match.getHome().getLineupForward());
        model.addAttribute("homePlayers", homePlayers);

        model.addAttribute("homeSubs", match.getHome().getLineupSubstitutes());

        match.getHome().getGoalDetails().sort(c);

        model.addAttribute("homeGoals", match.getHome().getGoalDetails());

        List<PlayerSeason> awayPlayers = new ArrayList<>();
        awayPlayers.add(match.getAway().getLineupGoalkeeper());
        awayPlayers.addAll(match.getAway().getLineupDefense());
        awayPlayers.addAll(match.getAway().getLineupMidfield());
        awayPlayers.addAll(match.getAway().getLineupForward());
        model.addAttribute("awayPlayers", awayPlayers);

        model.addAttribute("awaySubs", match.getAway().getLineupSubstitutes());

        match.getAway().getGoalDetails().sort(c);

        model.addAttribute("awayGoals", match.getAway().getGoalDetails());

        return "matchdetails";
    }

    @RequestMapping("/match/{matchId}/addSquad/{clubSeasonId}")
    public String addPlayerSeasonToMatch(@PathVariable("matchId") Integer matchId,
                                         @PathVariable("clubSeasonId") Integer clubSeasonId,
                                         Model model) {
        model.addAttribute("match", matchService.getMatchById(matchId));
        model.addAttribute("club", clubSeasonService.getClubSeasonById(clubSeasonId));
        Set<PlayerSeason> goalkeepersFromClub = new HashSet<>(clubSeasonService.getPlayersFromLine(clubSeasonId, "GK"));
        model.addAttribute("goalkeepers", goalkeepersFromClub);
        List<PlayerSeason> otherPlayersFromClub = new ArrayList<>();
        otherPlayersFromClub.addAll(clubSeasonService.getPlayersFromLine(clubSeasonId, "RB"));
        otherPlayersFromClub.addAll(clubSeasonService.getPlayersFromLine(clubSeasonId, "CB"));
        otherPlayersFromClub.addAll(clubSeasonService.getPlayersFromLine(clubSeasonId, "LB"));
        otherPlayersFromClub.addAll(clubSeasonService.getPlayersFromLine(clubSeasonId, "CDM"));
        otherPlayersFromClub.addAll(clubSeasonService.getPlayersFromLine(clubSeasonId, "RM"));
        otherPlayersFromClub.addAll(clubSeasonService.getPlayersFromLine(clubSeasonId, "CM"));
        otherPlayersFromClub.addAll(clubSeasonService.getPlayersFromLine(clubSeasonId, "LM"));
        otherPlayersFromClub.addAll(clubSeasonService.getPlayersFromLine(clubSeasonId, "CAM"));
        otherPlayersFromClub.addAll(clubSeasonService.getPlayersFromLine(clubSeasonId, "RW"));
        otherPlayersFromClub.addAll(clubSeasonService.getPlayersFromLine(clubSeasonId, "LW"));
        otherPlayersFromClub.addAll(clubSeasonService.getPlayersFromLine(clubSeasonId, "CF"));
        otherPlayersFromClub.addAll(clubSeasonService.getPlayersFromLine(clubSeasonId, "ST"));
        model.addAttribute("otherPlayers", otherPlayersFromClub);
        Checked checked = new Checked(new ArrayList<>());
        model.addAttribute("checked", checked);
        model.addAttribute("title", "Choose exactly 10 other players for main squad of " + clubSeasonService.getClubSeasonById(clubSeasonId).getClub().getName());
        model.addAttribute("action", "addSquad");
        return "fillsquad";
    }

    @RequestMapping(value = "/match/{matchId}/addSquad/{clubSeasonId}", method = RequestMethod.POST)
    public String savePlayerSeasonToMatch(@PathVariable("matchId") Integer matchId,
                                          @PathVariable("clubSeasonId") Integer clubSeasonId,
                                          @ModelAttribute("chosenGK") Integer goalkeeperSeasonId,
                                          @ModelAttribute("checked") Checked checked) {
        Match match = matchService.getMatchById(matchId);
        PlayerSeason playerSeason = clubSeasonService.getPlayerSeasonFromClubSeason(clubSeasonId, goalkeeperSeasonId);
        if (match.getHome().getClubSeason().getId().equals(clubSeasonId)) {
            match.getHome().setLineupGoalkeeper(playerSeason);
            for (Integer i: checked.getCheckedGames()) {
                playerSeasonService.setPositionOfPlayerSeasonId(i, match.getHome());
            }
            teamMatchDetailsService.saveTeamMatchDetails(match.getHome());
        } else {
            match.getAway().setLineupGoalkeeper(playerSeason);
            for (Integer i: checked.getCheckedGames()) {
                playerSeasonService.setPositionOfPlayerSeasonId(i, match.getAway());
            }
            teamMatchDetailsService.saveTeamMatchDetails(match.getAway());
        }
        matchService.saveMatch(match);

        return "redirect:/match/next";
    }

    @RequestMapping("/match/{matchId}/addSubs/{clubSeasonId}")
    public String addSubsToMatch(@PathVariable("matchId") Integer matchId,
                                         @PathVariable("clubSeasonId") Integer clubSeasonId,
                                         Model model) {
        model.addAttribute("match", matchService.getMatchById(matchId));
        model.addAttribute("club", clubSeasonService.getClubSeasonById(clubSeasonId));
        model.addAttribute("goalkeepers", new ArrayList<>());
        List<PlayerSeason> otherPlayers = clubSeasonService.getNotPickedPlayersForMatch(matchService.getMatchById(matchId), clubSeasonId);
        model.addAttribute("otherPlayers", otherPlayers);
        Checked checked = new Checked(new ArrayList<>());
        model.addAttribute("checked", checked);
        model.addAttribute("title", "Choose maximum 7 other players for substitutes of " + clubSeasonService.getClubSeasonById(clubSeasonId).getClub().getName());
        model.addAttribute("action", "addSubs");
        return "fillsquad";
    }

    @RequestMapping(value = "/match/{matchId}/addSubs/{clubSeasonId}", method = RequestMethod.POST)
    public String saveSubsToMatch(@PathVariable("matchId") Integer matchId,
                                          @PathVariable("clubSeasonId") Integer clubSeasonId,
                                          @ModelAttribute("checked") Checked checked) {
        Match match = matchService.getMatchById(matchId);
        if (match.getHome().getClubSeason().getId().equals(clubSeasonId)) {
            for (Integer i: checked.getCheckedGames()) {
                match.getHome().getLineupSubstitutes().add(playerSeasonService.getPlayerSeasonById(i));
            }
            teamMatchDetailsService.saveTeamMatchDetails(match.getHome());
        } else {
            for (Integer i: checked.getCheckedGames()) {
                match.getAway().getLineupSubstitutes().add(playerSeasonService.getPlayerSeasonById(i));
            }
            teamMatchDetailsService.saveTeamMatchDetails(match.getAway());
        }
        matchService.saveMatch(match);

        return "redirect:/match/next";
    }
}
