package com.widzin.services;

import com.widzin.models.Match;

import java.util.List;

public interface MatchService {
    Iterable<Match> listAllMatches();
    Match getMatchById(Integer id);
    List<Match> listAllMatchesWithClub(Integer clubId);
    Match saveMatch(Match match);
    void deleteMatch(Integer id);
}
