package com.widzin.services.implementations;

import com.widzin.model.Game2;
import com.widzin.repositories.Game2Repository;
import com.widzin.services.Game2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Game2ServiceImpl implements Game2Service {

    private Game2Repository game2Repository;

    @Autowired
    public void setGame2Repository(Game2Repository game2Repository) {
        this.game2Repository = game2Repository;
    }

    @Override
    public Iterable<Game2> listAllGames2() {
        return game2Repository.findAll();
    }

    @Override
    public Game2 getGame2ById(Integer id) {
        return game2Repository.findOne(id);
    }

    @Override
    public Game2 saveGame2(Game2 game2) {
        return game2Repository.save(game2);
    }

    @Override
    public void deleteGame2(Integer id) {
        game2Repository.delete(id);
    }
}
