package com.widzin.services;

import com.widzin.model.Match;

public interface MatchService {
    Iterable<Match> listAllMatches();
    Match getMatchById(Integer id);
    Match saveMatch(Match match);
    void deleteMatch(Integer id);
}
