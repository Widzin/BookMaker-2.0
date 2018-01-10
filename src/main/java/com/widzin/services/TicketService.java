package com.widzin.services;

import com.widzin.models.Game;
import com.widzin.models.Result;
import com.widzin.models.Ticket;

import java.util.List;

public interface TicketService {
	Iterable<Result> getAllOptions();
	List<Game> getAllGamesFromTicket(Ticket ticket);
	void saveTicket(Ticket ticket);
	Ticket findById(Integer id);
	List<Ticket> getAllTicketsFromUser(Integer id);
	List<Ticket> getAllTicketsWithMatch(Game game);
	void deleteTicket(Ticket ticket);
	Iterable<Ticket> getAllTickets();
}
