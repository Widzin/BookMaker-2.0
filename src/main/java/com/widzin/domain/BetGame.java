package com.widzin.domain;

import javax.persistence.*;

@Entity
public class BetGame {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Version
	private Integer version;

	@ManyToOne(fetch = FetchType.EAGER)
	private Game oneGame;
	private Result result;
	private Double rate;

	@ManyToOne(fetch = FetchType.EAGER)
	private Ticket ticket;

	public Game getOneGame () {
		return oneGame;
	}

	public void setOneGame (Game oneGame) {
		this.oneGame = oneGame;
	}

	public Result getResult () {
		return result;
	}

	public double getRate () {
		return rate;
	}

	public void setBet(Result result){
		this.result = result;
		switch (this.result){
			case home:
				rate = oneGame.getRates()[0];
				break;
			case draw:
				rate = oneGame.getRates()[1];
				break;
			case guest:
				rate = oneGame.getRates()[2];
				break;
		}
	}

	public Ticket getTicket () {
		return ticket;
	}

	public void setTicket (Ticket ticket) {
		this.ticket = ticket;
	}
}
