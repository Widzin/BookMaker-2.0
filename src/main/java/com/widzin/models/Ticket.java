package com.widzin.models;

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
	private Boolean finished;
	private Boolean win;

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

	public Boolean isFinished () {
		return finished;
	}

	public void setFinished (Boolean finished) {
		this.finished = finished;
	}

	public Boolean isWin () {
		return win;
	}

	public void setWin (Boolean win) {
		this.win = win;
	}

	public User getTicketOwner () {
		return ticketOwner;
	}

	public void setTicketOwner (User ticketOwner) {
		this.ticketOwner = ticketOwner;
	}
}
