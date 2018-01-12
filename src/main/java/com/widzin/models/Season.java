package com.widzin.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Season {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Version
    private Integer version;

    private String period;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<ClubSeason> clubs;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<Match> matches;

    public Season() {
    }

    public Season(String period) {
        this.period = period;
        clubs = new ArrayList<>();
        matches = new ArrayList<>();
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

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public List<ClubSeason> getClubs() {
        return clubs;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void addClub(ClubSeason club) {
        clubs.add(club);
    }

    public void addMatch(Match match) {
        matches.add(match);
    }

    @Override
    public String toString() {
        return "";
    }
}
