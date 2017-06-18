package com.widzin.services;

import com.widzin.domain.Result;
import com.widzin.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

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
}
