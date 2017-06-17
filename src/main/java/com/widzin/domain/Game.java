package com.widzin.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
public class Game {
	final int NUMBER_OF_RATES = 3;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Version
	private Integer version;

	@ManyToOne(fetch = FetchType.EAGER)
	private Club home;

	@ManyToOne(fetch = FetchType.EAGER)
	private Club away;

	private Integer homeScore;
	private Integer awayScore;

	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private Date date;
	private boolean played;

	private double[] rates;
	private String stringRates;

	@OneToMany(mappedBy = "oneGame", cascade = {CascadeType.ALL})
	private List<BetGame> betGameList;

	public Game () {
		this.played = false;
		betGameList = new ArrayList<>();
		rates = new double[NUMBER_OF_RATES];
	}

	public Game (Club home, Club away, Integer homeScore, Integer awayScore, Date date) {
		this.home = home;
		this.away = away;
		this.homeScore = homeScore;
		this.awayScore = awayScore;
		this.date = date;
		this.played = true;
		betGameList = new ArrayList<>();
		rates = new double[NUMBER_OF_RATES];
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

	public Integer getHomeScore () {
		return homeScore;
	}

	public void setHomeScore (Integer homeScore) {
		this.homeScore = homeScore;
	}

	public Integer getAwayScore () {
		return awayScore;
	}

	public void setAwayScore (Integer awayScore) {
		this.awayScore = awayScore;
	}

	public Date getDate () {
		return date;
	}

	public void setDate (Date date) {
		this.date = date;
	}

	public Club getHome () {
		return home;
	}

	public void setHome (Club home) {
		this.home = home;
	}

	public Club getAway () {
		return away;
	}

	public void setAway (Club away) {
		this.away = away;
	}

	public boolean isPlayed () {
		return played;
	}

	public void setPlayed (boolean played) {
		this.played = played;
	}

	public double[] getRates () {
		return rates;
	}

	public void setRates (double[] rates) {
		this.rates = rates;
	}

	public String getStringRates () {
		return stringRates;
	}

	public void setStringRates (String stringRates) {
		this.stringRates = stringRates;
	}

	public void setScore(int homeScore, int awayScore){
		this.homeScore = homeScore;
		this.awayScore = awayScore;
	}

	public List<BetGame> getBetGameList () {
		return betGameList;
	}

	public void setBetGameList (List<BetGame> betGameList) {
		this.betGameList = betGameList;
	}

	@Override
	public String toString() {
		return home.getName() + " - " + away.getName() + " " + homeScore + ":" + awayScore;
	}
}
