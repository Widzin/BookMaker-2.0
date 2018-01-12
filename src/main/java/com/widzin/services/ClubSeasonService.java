package com.widzin.services;

import com.widzin.models.ClubSeason;

import java.util.List;

public interface ClubSeasonService {
    Iterable<ClubSeason> listAllClubsSeason();
    ClubSeason getClubSeasonById(Integer id);
    List<ClubSeason> getClubSeasonsByClub2Id(Integer id);
    ClubSeason getLastClubSeason(Integer id);
    ClubSeason saveClubSeason(ClubSeason clubSeason);
    List<ClubSeason> sortListForTable(List<ClubSeason> list);
    void deleteClubSeason(Integer id);
}
