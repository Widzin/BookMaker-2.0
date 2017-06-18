package com.widzin.services;

import com.widzin.domain.Game;
import com.widzin.domain.Result;
import com.widzin.domain.Ticket;

public interface TicketService {
	Iterable<Result> getAllOptions();
	Iterable<Game> getGamesFromTicket(Ticket ticket);
}
