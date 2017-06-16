package com.widzin.controllers;

import com.widzin.services.BetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class BetController {

	private BetService betService;

	@Autowired
	public void setBetService (BetService betService) {
		this.betService = betService;
	}
}
