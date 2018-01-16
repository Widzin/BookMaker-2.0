package com.widzin.services;

import com.widzin.models.Club;

public interface ClubService {
    Iterable<Club> listAllClubs();
    Club getClubById(Integer id);
    Club saveClub(Club club);
    void deleteClub(Integer id);
}
