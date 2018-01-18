package com.widzin.models;

import javax.persistence.*;

@Entity
public class BetGame {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Version
	private Integer version;

	@ManyToOne(fetch = FetchType.EAGER)
	private Match match;
	private Result result;
	private Double rate;
	private Boolean matched;

	@ManyToOne(fetch = FetchType.EAGER)
	private Ticket ticket;

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

	public Match getMatch () {
		return match;
	}

	public void setMatch (Match match) {
		this.match = match;
	}

	public Result getResult () {
		return result;
	}

	public Double getRate () {
		return rate;
	}

	public void setBet(Result result){
		this.result = result;
		switch (this.result){
			case home:
				rate = match.getRates()[0];
				break;
			case draw:
				rate = match.getRates()[1];
				break;
			case guest:
				rate = match.getRates()[2];
				break;
		}
	}

	public Boolean isMatched () {
		return matched;
	}

	public void setMatched (Boolean matched) {
		this.matched = matched;
	}

	public Ticket getTicket () {
		return ticket;
	}

	public void setTicket (Ticket ticket) {
		this.ticket = ticket;
	}

	@Override
	public String toString(){
		return match.getHome().getClubSeason().getClub().getName()
                + " - " + match.getAway().getClubSeason().getClub().getName()
                + "\tRate: " + rate + ", Result: " + result;
	}
}
