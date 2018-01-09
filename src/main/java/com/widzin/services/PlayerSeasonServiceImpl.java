package com.widzin.services;

import com.widzin.model.PlayerSeason;
import com.widzin.repositories.PlayerSeasonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerSeasonServiceImpl implements PlayerSeasonService {

    private PlayerSeasonRepository playerSeasonRepository;

    @Autowired
    public void setPlayerSeasonRepository(PlayerSeasonRepository playerSeasonRepository) {
        this.playerSeasonRepository = playerSeasonRepository;
    }

    @Override
    public Iterable<PlayerSeason> listAllPlayerSeasons() {
        return playerSeasonRepository.findAll();
    }

    @Override
    public PlayerSeason getPlayerSeasonById(Integer id) {
        return playerSeasonRepository.findOne(id);
    }

    @Override
    public PlayerSeason savePlayerSeason(PlayerSeason playerSeason) {
        return playerSeasonRepository.save(playerSeason);
    }

    @Override
    public void deletePlayerSeason(Integer id) {
        playerSeasonRepository.delete(id);
    }
}
