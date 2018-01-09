package com.widzin.services.implementations;

import com.widzin.model.MatchEvent;
import com.widzin.repositories.MatchEventRepository;
import com.widzin.services.MatchEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatchEventServiceImpl implements MatchEventService {

    private MatchEventRepository matchEventRepository;

    @Autowired
    public void setMatchEventRepository(MatchEventRepository matchEventRepository) {
        this.matchEventRepository = matchEventRepository;
    }

    @Override
    public Iterable<MatchEvent> listAllMatchEvents() {
        return matchEventRepository.findAll();
    }

    @Override
    public MatchEvent getMatchEventById(Integer id) {
        return matchEventRepository.findOne(id);
    }

    @Override
    public MatchEvent saveMatchEvent(MatchEvent matchEvent) {
        return matchEventRepository.save(matchEvent);
    }

    @Override
    public void deleteMatchEvent(Integer id) {
        matchEventRepository.delete(id);
    }
}
