package com.widzin.services;

import com.widzin.domain.Game;
import com.widzin.domain.Result;
import com.widzin.domain.Ticket;

import java.util.List;

public interface TicketService {
	Iterable<Result> getAllOptions();
	List<Game> getAllGamesFromTicket(Ticket ticket);
	void saveTicket(Ticket ticket);
}
