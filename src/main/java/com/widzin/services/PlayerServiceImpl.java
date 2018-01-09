package com.widzin.services;

import com.widzin.model.Player;
import com.widzin.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerServiceImpl implements PlayerService {

    private PlayerRepository playerRepository;

    @Autowired
    public void setPlayerRepository(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public Iterable<Player> listAllPlayers() {
        return playerRepository.findAll();
    }

    @Override
    public Player getPlayerById(Integer id) {
        return playerRepository.findOne(id);
    }

    @Override
    public Player savePlayer(Player player) {
        return playerRepository.save(player);
    }

    @Override
    public void deletePlayer(Integer id) {
        playerRepository.delete(id);
    }
}
