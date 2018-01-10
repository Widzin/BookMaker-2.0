package com.widzin.services;

import com.widzin.models.PlayerSeason;

public interface PlayerSeasonService {
    Iterable<PlayerSeason> listAllPlayerSeasons();
    PlayerSeason getPlayerSeasonById(Integer id);
    PlayerSeason savePlayerSeason(PlayerSeason playerSeason);
    void deletePlayerSeason(Integer id);
}
