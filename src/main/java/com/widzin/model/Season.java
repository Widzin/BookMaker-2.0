package com.widzin.model;

import java.util.ArrayList;
import java.util.List;

public class Season {

    private String period;

    private List<ClubSeason> clubs;

    private List<Game2> game2s;



    public Season(String period) {
        this.period = period;
        clubs = new ArrayList<>();
        game2s = new ArrayList<>();
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

    public List<Game2> getGame2s() {
        return game2s;
    }

    public void addClub(ClubSeason club) {
        clubs.add(club);
    }

    public void addMatch(Game2 game2) {
        game2s.add(game2);
    }

    @Override
    public String toString() {
        return "Season{" +
                "period='" + period + '\'' +
                ", game2s=" + game2s +
                ", clubs=" + clubs +
                '}';
    }
}
