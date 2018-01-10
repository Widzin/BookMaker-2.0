package com.widzin.services.implementations;

import com.widzin.model.Match;
import com.widzin.repositories.MatchRepository;
import com.widzin.services.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//@Service
public class MatchServiceImpl /*implements MatchService*/ {

    /*private MatchRepository matchRepository;

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
    public Match saveMatch(Match match) {
        return matchRepository.save(match);
    }

    @Override
    public void deleteMatch(Integer id) {
        matchRepository.delete(id);
    }*/
}
