package com.widzin.services;

import com.widzin.models.PlayerSeason;
import com.widzin.models.TeamMatchDetails;

public interface PlayerSeasonService {
    Iterable<PlayerSeason> listAllPlayerSeasons();
    PlayerSeason getPlayerSeasonById(Integer id);
    PlayerSeason savePlayerSeason(PlayerSeason playerSeason);
    void setPositionOfPlayerSeasonId(Integer id, TeamMatchDetails teamMatchDetails);
    void deletePlayerSeason(Integer id);
}
