package com.widzin.model;

import java.util.ArrayList;
import java.util.List;

public class Season {

    private String period;

    private List<ClubSeason> clubs;

    private List<Match> matches;



    public Season(String period) {
        this.period = period;
        clubs = new ArrayList<>();
        matches = new ArrayList<>();
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
        return "Season{" +
                "period='" + period + '\'' +
                ", matches=" + matches +
                ", clubs=" + clubs +
                '}';
    }
}
