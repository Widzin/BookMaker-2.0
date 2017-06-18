package com.widzin.services;

import com.widzin.domain.BetGame;
import com.widzin.domain.Game;
import com.widzin.repositories.BetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BetServiceImpl implements BetService{

	private BetRepository betRepository;

	@Autowired
	public void setBetRepository (BetRepository betRepository) {
		this.betRepository = betRepository;
	}

	@Override
	public BetGame findById (Integer id) {
		return betRepository.findOne(id);
	}

	@Override
	public void saveBet (BetGame betGame) {
		betRepository.save(betGame);
	}
}
