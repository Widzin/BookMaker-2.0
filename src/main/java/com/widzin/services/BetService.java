package com.widzin.services;

import com.widzin.models.BetGame;
import com.widzin.models.Match;
import com.widzin.models.Ticket;

import java.util.List;

public interface BetService {
	void saveBet(BetGame betGame);
	List<BetGame> getBetsFromGameAndTicket(Match match, Ticket ticket);
	void saveBetsAfterMatch(List<Ticket> tickets, Match match);
	void deleteBet(BetGame betGame);
}
