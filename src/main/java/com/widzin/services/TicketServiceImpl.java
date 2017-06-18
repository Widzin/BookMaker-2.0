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
	public Iterable<Game> getGamesFromTicket (Ticket ticket) {
		List<Game> list = new ArrayList<>();
		for (BetGame bg: ticket.getBets()) {
			list.add(bg.getOneGame());
		}
		Iterable<Game> games = list;
		return games;
	}
}
