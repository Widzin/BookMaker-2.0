package com.widzin.services;

import com.widzin.domain.BetGame;
import com.widzin.domain.Game;
import com.widzin.domain.Result;
import com.widzin.domain.Ticket;
import com.widzin.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {
	private TicketRepository ticketRepository;

	@Autowired
	public void setTicketRepository (TicketRepository ticketRepository) {
		this.ticketRepository = ticketRepository;
	}

	@Override
	public Iterable<Result> getAllOptions () {
		Result[] results = Result.values();
		return Arrays.asList(results);
	}

	@Override
	public List<Game> getAllGamesFromTicket (Ticket ticket) {
		List<Game> games = new ArrayList<>();
		for (BetGame bg: ticket.getBets()){
			games.add(bg.getOneGame());
		}
		return games;
	}

	@Override
	public void saveTicket (Ticket ticket) {
		ticketRepository.save(ticket);
	}

	@Override
	public Ticket findById (Integer id) {
		return ticketRepository.findOne(id);
	}

	@Override
	public List<Ticket> getAllTicketsFromUser (Integer id) {
		List<Ticket> tickets = new ArrayList<>();
		for(Ticket t: ticketRepository.findAll()){
			if (t.getTicketOwner().getId() == id)
				tickets.add(t);
		}
		return tickets;
	}

	@Override
	public List<Ticket> getAllTicketsWithMatch (Game game) {
		List<Ticket> tickets = new ArrayList<>();
		for(Ticket t: ticketRepository.findAll()){
			for (BetGame bg: t.getBets()){
				if (bg.getOneGame().getId() == game.getId())
					tickets.add(t);
			}
		}
		return tickets;
	}
}
