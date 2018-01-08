package com.widzin.model;

import com.widzin.bootstrap.loaders.parsers.MatchParser;
import com.widzin.bootstrap.loaders.parsers.ParsingMethods;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

//@Entity
public class Match {

    /*@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Version
    private Integer version;

    @DateTimeFormat(pattern = "yyyy/MM/dd")*/
    private Date date;

    private Integer round;
    private String period;

    //@OneToOne(mappedBy = "match")
    private TeamMatchDetails home;

    //@OneToOne(mappedBy = "match")
    private TeamMatchDetails away;

    public Match() {
        home = new TeamMatchDetails();
        away = new TeamMatchDetails();
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

    @Override
    public String toString() {
        return "Match{" +
                "date=" + date +
                ", round=" + round +
                ", period='" + period + '\'' +
                ", home=" + home +
                ", away=" + away +
                '}';
    }
}
