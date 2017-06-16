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
	private double rate;

	public Game getOneGame () {
		return oneGame;
	}

	public void setOneGame (Game oneGame) {
		this.oneGame = oneGame;
	}

	public Result getResult () {
		return result;
	}

	public void setResult (Result result) {
		this.result = result;
	}

	public double getRate () {
		return rate;
	}

	public void setRate (double rate) {
		this.rate = rate;
	}
}
