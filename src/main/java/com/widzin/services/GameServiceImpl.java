package com.widzin.services;

import com.widzin.domain.Club;
import com.widzin.domain.Game;
import com.widzin.repositories.ClubRepository;
import com.widzin.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

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

	@Override
	public Iterable<Game> listMatchesBetween (Club home, Club away) {
		Iterable<Game> allGames = gameRepository.findAll();
		List<Game> list = new ArrayList<>();
		for (Game g: allGames) {
			if (g.getHome().getName().equals(home.getName())
				&& g.getAway().getName().equals(away.getName()))
				list.add(g);
			else if (g.getHome().getName().equals(away.getName())
					&& g.getAway().getName().equals(home.getName()))
				list.add(g);
		}
		Iterable<Game> games = list;
		return games;
	}
}
