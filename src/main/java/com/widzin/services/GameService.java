package com.widzin.services;

import com.widzin.model.Club;
import com.widzin.model.Game;

public interface GameService {
	Iterable<Game> findAllGames();
	Game findById(Integer id);
	Game saveMatch(Game game);
	Iterable<Game> listMatchesBetween (Club home, Club away);
	Iterable<Game> getNextMatches();
}
