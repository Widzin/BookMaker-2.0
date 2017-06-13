package com.widzin.services;

import com.widzin.domain.Game;

public interface GameService {
	Iterable<Game> listAllMatches();
	Game findById(Integer id);
	Game saveMatch(Game game);
}
