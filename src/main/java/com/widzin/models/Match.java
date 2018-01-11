package com.widzin.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "bundesliga_match")
public class Match {
    private static final int NUMBER_OF_RATES = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Version
    private Integer version;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private Date date;

    private Integer round;
    private String period;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private TeamMatchDetails home;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private TeamMatchDetails away;

    private boolean played;
    private Double[] rates;

    @OneToMany(mappedBy = "match", cascade = {CascadeType.ALL})
    private List<BetGame> betGameList;

    public Match() {
        played = false;
        rates = new Double[NUMBER_OF_RATES];
        betGameList = new ArrayList<>();
        home = new TeamMatchDetails();
        away = new TeamMatchDetails();
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getRound() {
        return round;
    }

    public void setRound(Integer round) {
        this.round = round;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public TeamMatchDetails getHome() {
        return home;
    }

    public void setHome(TeamMatchDetails home) {
        this.home = home;
    }

    public TeamMatchDetails getAway() {
        return away;
    }

    public void setAway(TeamMatchDetails away) {
        this.away = away;
    }

    public boolean isPlayed() {
        return played;
    }

    public void setPlayed(boolean played) {
        this.played = played;
    }

    public Double[] getRates() {
        return rates;
    }

    public void setRates(Double[] rates) {
        this.rates = rates;
    }

    public List<BetGame> getBetGameList() {
        return betGameList;
    }

    public void addBetGameToList(BetGame betGame) {
        betGameList.add(betGame);
    }

    @Override
    public String toString() {
        return "Match{" +
                "id=" + id +
                ", version=" + version +
                ", date=" + date +
                ", round=" + round +
                ", period='" + period + '\'' +
                ", home=" + home +
                ", away=" + away +
                ", played=" + played +
                ", rates=" + Arrays.toString(rates) +
                ", betGameList=" + betGameList +
                '}';
    }
}
