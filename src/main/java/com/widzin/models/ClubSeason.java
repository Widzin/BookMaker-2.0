package com.widzin.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class ClubSeason {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Version
    private Integer version;

    @ManyToOne(fetch = FetchType.EAGER)
    private Club club;

    private Integer points;
    private Integer matches;
    private Integer wins;
    private Integer draws;
    private Integer loses;
    private Integer scoredGoals;
    private Integer lostGoals;
    private Integer bilans;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<PlayerSeason> players;

    public ClubSeason() {
    }

    public ClubSeason(String name) {
        club = new Club(name);
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

    public ClubSeason(Club club) {
        this.club = club;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public Integer getPoints() {
        return points;
    }

    public void addPoints(Integer points) {
        this.points += points;
    }

    public Integer getMatches() {
        return matches;
    }

    public void addMatch() {
        this.matches ++;
    }

    public Integer getWins() {
        return wins;
    }

    public void addWin() {
        this.wins ++;
    }

    public Integer getDraws() {
        return draws;
    }

    public void addDraw() {
        this.draws ++;
    }

    public Integer getLoses() {
        return loses;
    }

    public void addLose() {
        this.loses ++;
    }

    public Integer getScoredGoals() {
        return scoredGoals;
    }

    public void addScoredGoals(Integer scoredGoals) {
        this.scoredGoals += scoredGoals;
    }

    public Integer getLostGoals() {
        return lostGoals;
    }

    public void addLostGoals(Integer lostGoals) {
        this.lostGoals += lostGoals;
    }

    public Integer getBilans() {
        return bilans;
    }

    public void setBilans() {
        this.bilans = scoredGoals - lostGoals;
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
                "club=" + club +
                ", players=" + players +
                '}';
    }
}
