package com.widzin.services;

import com.widzin.domain.Club;
import com.widzin.domain.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GameService {
	Game findById(Integer id);
	Game saveMatch(Game game);
	Iterable<Game> listMatchesBetween(Club home, Club away);
	Iterable<Game> getNextMatches();
}
