package com.widzin.services;

import com.widzin.models.Match;

import java.util.List;

public interface MatchService {
    Iterable<Match> listAllMatches();
    Match getMatchById(Integer id);
    Match getLastMatch();
    List<Match> listAllMatchesWithClub(Integer clubId);
    List<Match> listAllMatchesWithClubSeason(Integer clubSeasonId);
    List<Match> listAllPlayedMatchesWithClub(Integer clubId);
    List<Match> listAllNotPlayedMatchesWithClub(Integer clubId);
    Integer listAllMatchesFromPeriodAndRoundSize(String period, Integer round);
    List<Match> listAllNextMatches();
    Match saveMatch(Match match);
    void deleteMatch(Integer id);
}
