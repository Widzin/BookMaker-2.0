package com.widzin.services;

import com.widzin.models.Club2;

public interface Club2Service {
    Iterable<Club2> listAllClubs2();
    Club2 getClub2ById(Integer id);
    Club2 saveClub2(Club2 club2);
    void deleteClub2(Integer id);
}
