package com.widzin.services;

import com.widzin.model.MatchEvent;

public interface MatchEventService {
    Iterable<MatchEvent> listAllMatchEvents();
    MatchEvent getMatchEventById(Integer id);
    MatchEvent saveMatchEvent(MatchEvent matchEvent);
    void deleteMatchEvent(Integer id);
}
