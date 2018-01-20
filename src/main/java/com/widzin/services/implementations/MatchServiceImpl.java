package com.widzin.services.implementations;

import com.google.common.collect.Lists;
import com.widzin.models.*;
import com.widzin.repositories.ClubSeasonRepository;
import com.widzin.repositories.MatchRepository;
import com.widzin.repositories.SeasonRepository;
import com.widzin.services.MatchService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatchServiceImpl implements MatchService {

    private final static Integer POINTS_FOR_WIN = 3;
    private final static Integer POINTS_FOR_DRAW = 1;
    private final static Integer POINTS_FOR_LOST = 0;

    private Logger log = Logger.getLogger(MatchServiceImpl.class);

    private MatchRepository matchRepository;
    private ClubSeasonRepository clubSeasonRepository;
    private SeasonRepository seasonRepository;

    @Autowired
    public void setMatchRepository(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @Autowired
    public void setClubSeasonRepository(ClubSeasonRepository clubSeasonRepository) {
        this.clubSeasonRepository = clubSeasonRepository;
    }

    @Autowired
    public void setSeasonRepository(SeasonRepository seasonRepository) {
        this.seasonRepository = seasonRepository;
    }

    @Override
    public Iterable<Match> listAllMatches() {
        return matchRepository.findAll();
    }

    @Override
    public Match getMatchById(Integer id) {
        return matchRepository.findOne(id);
    }

    @Override
    public Match getLastMatch() {
        List<Match> allMatches = Lists.newArrayList(listAllMatches());
        return allMatches.get(allMatches.size() - 1);
    }

    @Override
    public List<Match> listAllMatchesWithClub(Integer clubId) {
        Iterable<Match> allMatches = listAllMatches();
        List<Match> allMatchesWithClub = new ArrayList<>();

        for (Match match: allMatches) {
            if (match.getHome().getClubSeason().getClub().getId() == clubId
                    || match.getAway().getClubSeason().getClub().getId() == clubId)
                allMatchesWithClub.add(match);
        }

        return allMatchesWithClub;
    }

    @Override
    public List<Match> listAllMatchesWithClubSeason(Integer clubSeasonId) {
        List<Match> matches = new ArrayList<>();

        for (Match match: Lists.newArrayList(listAllMatches())) {
            if (match.getHome().getClubSeason().getId() == clubSeasonId
                    || match.getAway().getClubSeason().getId() == clubSeasonId)
                matches.add(match);
        }
        return matches;
    }

    @Override
    public List<Match> listAllPlayedMatchesWithClub(Integer clubId) {
        List<Match> allPlayedMatches = new ArrayList<>();

        for (Match match: listAllMatchesWithClub(clubId)) {
            if (match.isPlayed())
                allPlayedMatches.add(match);
        }

        return allPlayedMatches;
    }

    @Override
    public List<Match> listAllNotPlayedMatchesWithClub(Integer clubId) {
        List<Match> allPlayedMatches = new ArrayList<>();

        for (Match match: listAllMatchesWithClub(clubId)) {
            if (!match.isPlayed())
                allPlayedMatches.add(match);
        }

        return allPlayedMatches;
    }

    @Override
    public Integer listAllMatchesFromPeriodAndRoundSize(String period, Integer round) {
        Integer counter = 0;
        for (Match match: Lists.newArrayList(listAllMatches())) {
            if (match.getPeriod().equals(period) && match.getRound() == round)
                counter++;
        }
        return counter;
    }

    @Override
    public List<Match> listAllNextMatches(List<Season> seasons) {
        Calculations calculations = Calculations.getInstance();
        List<Match> nextMatches = new ArrayList<>();

        for (Match match: Lists.newArrayList(listAllMatches())) {
            if (!match.isPlayed()) {
                if (match.getRates()[0] == null || calculations.isAddedNewMatch()) {
                    calculations.prepareMatch(match.getHome().getClubSeason(),
                            match.getAway().getClubSeason(), seasons);
                    match.setRates((calculations.calculateRates()));
                    saveMatch(match);
                    log.info("Policzono: " + match.getRates()[0]
                    + " - " + match.getRates()[1] + " - " + match.getRates()[2]);
                }
                nextMatches.add(match);
                calculations.resetClubs();
            }

        }
        return nextMatches;
    }

    @Override
    public void updateClubsAfterMatch(int matchId, int homeScore, int awayScore) {
        Match match = matchRepository.findOne(matchId);
        match.getHome().setGoals(homeScore);
        match.getAway().setGoals(awayScore);
        match.setPlayed(true);

        saveMatch(match);

        ClubSeason homeClub = match.getHome().getClubSeason();
        ClubSeason awayClub = match.getAway().getClubSeason();
        updateClubAfterMatch(homeClub, homeScore, awayScore);
        updateClubAfterMatch(awayClub, awayScore, homeScore);

        Calculations calculations = Calculations.getInstance();
        calculations.addNumberOfAllMatches();
        calculations.addAllGoalsScoredAtHome(homeScore);
        calculations.addAllGoalsLostAtHome(awayScore);
    }

    @Override
    public void updateClubAfterMatch(ClubSeason clubSeason, int hisScore, int enemyScore) {
        if (hisScore > enemyScore) {
            clubSeason.addWin();
            clubSeason.addPoints(POINTS_FOR_WIN);
        }
        else if (hisScore < enemyScore) {
            clubSeason.addLose();
            clubSeason.addPoints(POINTS_FOR_LOST);
        }
        else {
            clubSeason.addDraw();
            clubSeason.addPoints(POINTS_FOR_DRAW);
        }

        clubSeason.addMatch();
        clubSeason.addScoredGoals(hisScore);
        clubSeason.addLostGoals(enemyScore);
        clubSeason.setBilans();
        clubSeasonRepository.save(clubSeason);
    }

    @Override
    public List<Match> listAllMatchesBetween(int homeId, int awayId) {
        List<Match> matches = new ArrayList<>();
        for (Match match: listAllPlayedMatchesWithClub(homeId)) {
            if (match.getHome().getClubSeason().getClub().getId() == awayId
                    || match.getAway().getClubSeason().getClub().getId() == awayId)
                matches.add(match);
        }
        return matches;
    }

    @Override
    public Match saveMatch(Match match) {
        return matchRepository.save(match);
    }

    @Override
    public void deleteMatch(Integer id) {
        matchRepository.delete(id);
    }
}
