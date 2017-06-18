package com.widzin.services;

import com.widzin.domain.BetGame;
import com.widzin.domain.Game;
import com.widzin.domain.Ticket;

import java.util.List;

public interface BetService {
	void saveBet(BetGame betGame);
	List<BetGame> getBetsFromGameAndTicket(Game game, Ticket ticket);
	void deleteBet(BetGame betGame);
}
