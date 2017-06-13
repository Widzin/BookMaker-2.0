package com.widzin.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
public class Game {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Integer id;

	@Version
	private Integer version;

	//@ManyToMany(mappedBy = "games")
	//private Set<Club> clubs;
	@ManyToOne(fetch = FetchType.LAZY)
	private Club home;

	@ManyToOne(fetch = FetchType.LAZY)
	private Club away;

	private Integer homeScore;
	private Integer awayScore;
	private Date date;

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
}
