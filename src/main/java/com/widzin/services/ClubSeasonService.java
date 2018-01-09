package com.widzin.services;

import com.widzin.model.ClubSeason;

public interface ClubSeasonService {
    Iterable<ClubSeason> listAllClubsSeason();
    ClubSeason getClubSeasonById(Integer id);
    ClubSeason saveClubSeason(ClubSeason clubSeason);
    void deleteClubSeason(Integer id);
}
