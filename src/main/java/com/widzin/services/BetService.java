package com.widzin.services;

import com.widzin.domain.BetGame;

public interface BetService {
	BetGame findById(Integer id);
	void saveBet(BetGame betGame);
}
