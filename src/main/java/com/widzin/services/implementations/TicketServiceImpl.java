package com.widzin.services.implementations;

import com.widzin.models.BetGame;
import com.widzin.models.Game;
import com.widzin.models.Result;
import com.widzin.models.Ticket;
import com.widzin.repositories.TicketRepository;
import com.widzin.services.TicketService;
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

	@Override
	public void deleteTicket (Ticket ticket) {
		ticketRepository.delete(ticket);
	}

	@Override
	public Iterable<Ticket> getAllTickets () {
		return ticketRepository.findAll();
	}
}
