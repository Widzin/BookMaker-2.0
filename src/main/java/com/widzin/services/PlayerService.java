package com.widzin.services;

import com.widzin.model.Player;

public interface PlayerService {
    Iterable<Player> listAllPlayers();
    Player getPlayerById(Integer id);
    Player savePlayer(Player player);
    void deletePlayer(Integer id);
}
