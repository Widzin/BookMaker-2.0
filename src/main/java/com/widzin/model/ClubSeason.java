package com.widzin.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//@Entity
public class ClubSeason {

    /*@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Version
    private Integer version;*/

    private Club2 club2;

    private Integer points;
    private Integer matches;
    private Integer wins;
    private Integer draws;
    private Integer loses;
    private Integer scoredGoals;
    private Integer lostGoals;
    private Integer bilans;

    private List<PlayerSeason> players;

    public ClubSeason(String name) {
        club2 = new Club2(name);
        players = new ArrayList<>();
        points = 0;
        matches = 0;
        wins = 0;
        draws = 0;
        loses = 0;
        scoredGoals = 0;
        lostGoals = 0;
        bilans = 0;
    }

    public ClubSeason(Club2 club2) {
        this.club2 = club2;
        players = new ArrayList<>();
        points = 0;
        matches = 0;
        wins = 0;
        draws = 0;
        loses = 0;
        scoredGoals = 0;
        lostGoals = 0;
        bilans = 0;
    }

    public Club2 getClub2() {
        return club2;
    }

    public void setClub(Club2 club2) {
        this.club2 = club2;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getMatches() {
        return matches;
    }

    public void setMatches(Integer matches) {
        this.matches = matches;
    }

    public Integer getWins() {
        return wins;
    }

    public void setWins(Integer wins) {
        this.wins = wins;
    }

    public Integer getDraws() {
        return draws;
    }

    public void setDraws(Integer draws) {
        this.draws = draws;
    }

    public Integer getLoses() {
        return loses;
    }

    public void setLoses(Integer loses) {
        this.loses = loses;
    }

    public Integer getScoredGoals() {
        return scoredGoals;
    }

    public void setScoredGoals(Integer scoredGoals) {
        this.scoredGoals = scoredGoals;
    }

    public Integer getLostGoals() {
        return lostGoals;
    }

    public void setLostGoals(Integer lostGoals) {
        this.lostGoals = lostGoals;
    }

    public Integer getBilans() {
        return bilans;
    }

    public void setBilans(Integer bilans) {
        this.bilans = bilans;
    }

    public List<PlayerSeason> getPlayers() {
        return players;
    }

    public void addPlayer(PlayerSeason player) {
        players.add(player);
    }

    @Override
    public String toString() {
        return "ClubSeason{" +
                "club=" + club2 +
                ", players=" + players +
                '}';
    }
}
