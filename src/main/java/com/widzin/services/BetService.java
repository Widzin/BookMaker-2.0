package com.widzin.services;

import com.widzin.model.BetGame;
import com.widzin.model.Game;
import com.widzin.model.Ticket;

import java.util.List;

public interface BetService {
	void saveBet(BetGame betGame);
	List<BetGame> getBetsFromGameAndTicket(Game game, Ticket ticket);
	void deleteBet(BetGame betGame);
}
