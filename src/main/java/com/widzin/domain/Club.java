package com.widzin.domain;

import javax.persistence.*;
import java.util.*;

@Entity
public class Club {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Version
	private Integer version;

	private String name;
	private String imgUrl;
	private Integer points;
	private Integer matches;
	private Integer wins;
	private Integer draws;
	private Integer loses;
	private Integer scoredGoals;
	private Integer lostGoals;
	private Integer bilans;
	private double valueOfPlayers;
	private Integer numberOfPlayers;
	private boolean bundesliga;
	//private Integer numberOfGames;

	@OneToMany(mappedBy = "home", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Game> gamesAtHome;
	@OneToMany(mappedBy = "away", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Game> gamesAway;

	public Club() {
		points = 0;
		matches = 0;
		wins = 0;
		draws = 0;
		loses = 0;
		scoredGoals = 0;
		lostGoals = 0;
		bilans = 0;
		//numberOfGames = 0;
		gamesAtHome = new ArrayList<>();
		gamesAway = new ArrayList<>();
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

	public String getName () {
		return name;
	}

	public void setName (String name) {
		this.name = name;
	}

	public String getImgUrl () {
		return imgUrl;
	}

	public void setImgUrl (String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Integer getPoints () {
		return points;
	}

	public void setPoints () {
		points = 3*wins + draws;
	}

	public Integer getMatches () {
		return matches;
	}

	public void setMatches () {
		matches ++;
	}

	public Integer getWins () {
		return wins;
	}

	public void addWins () {
		wins ++;
	}

	public Integer getDraws () {
		return draws;
	}

	public void addDraws () {
		draws ++;
	}

	public Integer getLoses () {
		return loses;
	}

	public void addLoses () {
		loses ++;
	}

	public Integer getScoredGoals () {
		return scoredGoals;
	}

	public void addScoredGoals (Integer goals) {
		scoredGoals += goals;
	}

	public Integer getLostGoals () {
		return lostGoals;
	}

	public void addLostGoals (Integer goals) {
		lostGoals += goals;
	}

	public Integer getBilans () {
		return bilans;
	}

	public void setBilans () {
		bilans = (scoredGoals - lostGoals);
	}

	public Double getValueOfPlayers () { return valueOfPlayers;	}

	public void setValueOfPlayers (Double valueOfPlayers) {
		this.valueOfPlayers = valueOfPlayers;
	}

	public Integer getNumberOfPlayers () {
		return numberOfPlayers;
	}

	public void setNumberOfPlayers (Integer numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
	}

	public boolean isBundesliga () {
		return bundesliga;
	}

	public void setBundesliga (boolean bundesliga) {
		this.bundesliga = bundesliga;
	}

	/*public Integer getNumberOfGames () {
		return numberOfGames;
	}

	public void setNumberOfGames(){
		numberOfGames = gamesAtHome.size() + gamesAway.size();
	}*/

	public List<Game> getGamesAtHome () {
		return gamesAtHome;
	}

	public void addGameAtHome(Game game){
		gamesAtHome.add(game);
	}

	public void setGamesAtHome (List<Game> gamesAsOwner) {
		this.gamesAtHome = gamesAsOwner;
	}

	public List<Game> getGamesAway () {
		return gamesAway;
	}

	public void addGameAway(Game game){
		gamesAway.add(game);
	}

	public void setGamesAway (List<Game> gamesAsGuest) {
		this.gamesAway = gamesAsGuest;
	}

	public void resetStats() {
		wins = 0;
		draws = 0;
		loses = 0;
		scoredGoals = 0;
		lostGoals = 0;
		setMatches();
		setPoints();
		setBilans();
	}

	public void updateStats() {
		setMatches();
		setPoints();
		setBilans();
	}
}
