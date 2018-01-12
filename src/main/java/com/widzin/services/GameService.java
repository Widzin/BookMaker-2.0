package com.widzin.services;

import com.widzin.models.Club;
import com.widzin.models.Game;

public interface GameService {
	Iterable<Game> findAllGames();
	Game findById(Integer id);
	Game saveMatch(Game game);
	Iterable<Game> listMatchesBetween (Club home, Club away);
	Iterable<Game> getNextMatches();
}
