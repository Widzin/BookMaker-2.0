package com.widzin.services.implementations;

import com.google.common.collect.Lists;
import com.widzin.models.Calculations;
import com.widzin.models.Match;
import com.widzin.repositories.MatchRepository;
import com.widzin.services.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatchServiceImpl implements MatchService {

    private MatchRepository matchRepository;

    @Autowired
    public void setMatchRepository(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
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
            if (match.getHome().getClubSeason().getClub2().getId() == clubId
                    || match.getAway().getClubSeason().getClub2().getId() == clubId)
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
    public List<Match> listAllNextMatches() {
        Calculations calculations = Calculations.getInstance();
        List<Match> nextMatches = new ArrayList<>();

        for (Match match: Lists.newArrayList(listAllMatches())) {
            if (!match.isPlayed()) {
                //policz rate'y
                //zapisz
                nextMatches.add(match);
            }

        }
        return nextMatches;
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
