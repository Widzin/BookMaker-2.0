package com.widzin.services.implementations;

import com.widzin.bootstrap.SpringJpaBootstrap;
import com.widzin.model.Calculations;
import com.widzin.model.Club;
import com.widzin.model.Game;
import com.widzin.repositories.GameRepository;
import com.widzin.services.GameService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameServiceImpl implements GameService {
	private GameRepository gameRepository;

	@Autowired
	public void setGameRepository (GameRepository gameRepository) {
		this.gameRepository = gameRepository;
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
		List<Game> list = new ArrayList<>();
		for (Game g: gameRepository.findAll()) {
			if (g.isPlayed()) {
				if (g.getHome().getName().equals(home.getName())
						&& g.getAway().getName().equals(away.getName()))
					list.add(g);
				else if (g.getHome().getName().equals(away.getName())
						&& g.getAway().getName().equals(home.getName()))
					list.add(g);
			}
		}
		Iterable<Game> games = list;
		return games;
	}

	private Logger log = Logger.getLogger(SpringJpaBootstrap.class);

	@Override
	public Iterable<Game> getNextMatches () {
		Calculations calculations = Calculations.getInstance();
		List<Game> list = new ArrayList<>();
		for (Game g: gameRepository.findAll()) {
			if (!g.isPlayed()){
				if (g.getRates()[0] == null || calculations.isAddedNewMatch()) {
					calculations.prepareMatch(g.getHome(), g.getAway());
					g.setRates(calculations.calculateRates());
					gameRepository.save(g);
					log.info("Policzyłem: " + g.getRates()[0] + " - " + g.getRates()[1] + " - " + g.getRates()[2]);
				}
				list.add(g);
				calculations.resetClubs();
			}
		}
		Iterable<Game> nextGames = list;
		return nextGames;
	}

	@Override
	public Iterable<Game> findAllGames(){
		return gameRepository.findAll();
	}
}
