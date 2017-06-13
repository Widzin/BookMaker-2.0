package com.widzin.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
public class Game {
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
	private Date date;

	public Game () {
	}

	public Game (Club home, Club away, Date date) {
		this.home = home;
		this.away = away;
		this.date = date;
	}

	public Game (Club home, Club away, Integer homeScore, Integer awayScore, Date date) {
		this.home = home;
		this.away = away;
		this.homeScore = homeScore;
		this.awayScore = awayScore;
		this.date = date;
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

	public void setScore(int homeScore, int awayScore){
		this.homeScore = homeScore;
		this.awayScore = awayScore;
	}

	public void updateClubStats(){
		if (homeScore > awayScore){
			home.addWins();
			away.addLoses();
		} else if (homeScore < awayScore){
			home.addLoses();
			away.addWins();
		} else {
			home.addDraws();
			away.addDraws();
		}
		home.addScoredGoals(homeScore);
		home.addLostGoals(awayScore);
		away.addScoredGoals(awayScore);
		away.addLostGoals(homeScore);
	}
}
