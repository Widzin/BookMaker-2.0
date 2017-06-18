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
	private boolean finished;

	@ManyToOne(fetch = FetchType.EAGER)
	private User ticketOwner;

	public Ticket(){
		rate = 1.0;
		bets = new ArrayList<>();
		finished = false;
	}

	public Integer getId () {
		return id;
	}

	public void setId (Integer id) {
		this.id = id;
	}

	public Integer getVersion () {
		return version;
	}

	public void setVersion (Integer version) {
		this.version = version;
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

	public void calculateTicket () {
		for (BetGame bet: bets) {
			rate *= bet.getRate();
		}
		moneyToWin = rate * moneyInserted;
	}

	public boolean isFinished () {
		return finished;
	}

	public void setFinished (boolean finished) {
		this.finished = finished;
	}

	public User getTicketOwner () {
		return ticketOwner;
	}

	public void setTicketOwner (User ticketOwner) {
		this.ticketOwner = ticketOwner;
	}
}
