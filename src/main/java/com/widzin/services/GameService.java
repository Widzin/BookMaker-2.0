package com.widzin.services;

import com.widzin.domain.Club;
import com.widzin.domain.Game;

public interface GameService {
	Iterable<Game> listAllMatches();
	Game findById(Integer id);
	Game saveMatch(Game game);
	Iterable<Game> listMatchesBetween(Club home, Club away);
}
