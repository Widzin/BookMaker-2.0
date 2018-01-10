package com.widzin.services;

import com.widzin.models.BetGame;
import com.widzin.models.Game;
import com.widzin.models.Ticket;

import java.util.List;

public interface BetService {
	void saveBet(BetGame betGame);
	List<BetGame> getBetsFromGameAndTicket(Game game, Ticket ticket);
	void deleteBet(BetGame betGame);
}
