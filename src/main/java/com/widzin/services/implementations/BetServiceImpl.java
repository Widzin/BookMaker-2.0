package com.widzin.services.implementations;

import com.widzin.models.BetGame;
import com.widzin.models.Game;
import com.widzin.models.Ticket;
import com.widzin.repositories.BetRepository;
import com.widzin.services.BetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BetServiceImpl implements BetService {

	private BetRepository betRepository;

	@Autowired
	public void setBetRepository (BetRepository betRepository) {
		this.betRepository = betRepository;
	}

	@Override
	public void saveBet (BetGame betGame) {
		betRepository.save(betGame);
	}

	@Override
	public List<BetGame> getBetsFromGameAndTicket (Game game, Ticket ticket) {
		List<BetGame> bets = new ArrayList<>();
		for (BetGame bg: ticket.getBets()){
			if (bg.getOneGame().getId() == game.getId())
				bets.add(bg);
		}
		return bets;
	}

	@Override
	public void deleteBet (BetGame betGame) {
		betRepository.delete(betGame);
	}
}
