package com.widzin.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/*@Entity
@Table*/
public class Match {

    /*@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Version
    private Integer version;

    @DateTimeFormat(pattern = "yyyy/MM/dd")*/
    private Date date;

    private Integer round;
    private String period;

    /*@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "team_match_details_id", insertable = false, updatable = false)*/
    private TeamMatchDetails home;

    /*@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "team_match_details_id", insertable = false, updatable = false)*/
    private TeamMatchDetails away;

    public Match() {
        home = new TeamMatchDetails();
        away = new TeamMatchDetails();
    }

    /*public Integer getId() {
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
    }*/

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
