package com.widzin.services;

import com.widzin.models.ClubSeason;
import com.widzin.models.Match;
import com.widzin.models.PlayerSeason;

import java.util.List;

public interface ClubSeasonService {
    Iterable<ClubSeason> listAllClubsSeason();
    ClubSeason getClubSeasonById(Integer id);
    List<ClubSeason> getClubSeasonsByClubId(Integer id);
    ClubSeason getLastClubSeason(Integer id);
    ClubSeason saveClubSeason(ClubSeason clubSeason);
    List<ClubSeason> sortListForTable(List<ClubSeason> list);
    List<PlayerSeason> getPlayersFromLine(Integer clubSeasonId, String line);
    PlayerSeason getPlayerSeasonFromClubSeason(Integer clubSeasonId, Integer playerSeasonId);
    List<PlayerSeason> getNotPickedPlayersForMatch(Match match, Integer clubSeasonId);
    void deleteClubSeason(Integer id);
}
