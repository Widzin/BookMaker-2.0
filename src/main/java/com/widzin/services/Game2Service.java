package com.widzin.services;

import com.widzin.model.Game2;

public interface Game2Service {
    Iterable<Game2> listAllGames2();
    Game2 getGame2ById(Integer id);
    Game2 saveGame2(Game2 game2);
    void deleteGame2(Integer id);
}
