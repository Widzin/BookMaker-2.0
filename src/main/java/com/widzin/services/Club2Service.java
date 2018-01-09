package com.widzin.services;

import com.widzin.model.Club2;

public interface Club2Service {
    Iterable<Club2> listAllClubs();
    Club2 getClubById(Integer id);
    Club2 saveClub(Club2 club2);
    void deleteClub(Integer id);
}
