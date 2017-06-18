package com.widzin.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Ticket {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Version
	private Integer version;

	@OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
	private List<BetGame> bets;
	private Double rate;
	private Double moneyInserted;
	private Double moneyToWin;

	@ManyToOne(fetch = FetchType.EAGER)
	private User ticketOwner;

	public Ticket(){
		rate = 1.0;
		bets = new ArrayList<>();
	}

	public List<BetGame> getBets () {
		return bets;
	}

	public void addBets (BetGame bet) {
		bets.add(bet);
	}

	public Double getRate () {
		return rate;
	}

	public Double getMoneyInserted () {
		return moneyInserted;
	}

	public void setMoneyInserted (Double moneyInserted) {
		this.moneyInserted = moneyInserted;
	}

	public Double getMoneyToWin () {
		return moneyToWin;
	}

	public void finishTicket () {
		for (BetGame bet: bets) {
			rate *= bet.getRate();
		}
		moneyToWin = rate * moneyInserted;
	}

	public User getTicketOwner () {
		return ticketOwner;
	}

	public void setTicketOwner (User ticketOwner) {
		this.ticketOwner = ticketOwner;
	}
}
