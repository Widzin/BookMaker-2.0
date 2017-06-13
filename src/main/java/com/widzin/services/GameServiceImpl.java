package com.widzin.services;

import com.widzin.domain.Game;
import com.widzin.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameServiceImpl implements GameService {
	private GameRepository gameRepository;

	@Autowired
	public void setGameRepository (GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}

	@Override
	public Iterable<Game> listAllMatches () {
		return gameRepository.findAll();
	}

	@Override
	public Game findById (Integer id) {
		return gameRepository.findOne(id);
	}

	@Override
	public Game saveMatch (Game game) {
		return gameRepository.save(game);
	}
}
