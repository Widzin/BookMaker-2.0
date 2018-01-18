package com.widzin.services.implementations;

import com.widzin.models.BetGame;
import com.widzin.models.Match;
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
	public List<BetGame> getBetsFromGameAndTicket (Match match, Ticket ticket) {
		List<BetGame> bets = new ArrayList<>();
		for (BetGame bg: ticket.getBets()){
			if (bg.getMatch().getId() == match.getId())
				bets.add(bg);
		}
		return bets;
	}

    @Override
    public void saveBetsAfterMatch(List<Ticket> tickets, Match match) {
        List<BetGame> bets = new ArrayList<>();
        for (Ticket t: tickets) {
            bets.addAll(getBetsFromGameAndTicket(match, t));
        }
        for (BetGame bg: bets) {
            switch (bg.getResult()){
                case home:
                    if (match.getHome().getGoals() > match.getAway().getGoals())
                        bg.setMatched(true);
                    else
                        bg.setMatched(false);
                    break;
                case guest:
                    if (match.getHome().getGoals() < match.getAway().getGoals())
                        bg.setMatched(true);
                    else
                        bg.setMatched(false);
                    break;
                case draw:
                    if (match.getHome().getGoals() == match.getAway().getGoals())
                        bg.setMatched(true);
                    else
                        bg.setMatched(false);
                    break;
            }
            saveBet(bg);
        }
    }

    @Override
	public void deleteBet (BetGame betGame) {
		betRepository.delete(betGame);
	}
}
