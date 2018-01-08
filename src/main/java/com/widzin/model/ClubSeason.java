package com.widzin.model;

import java.util.ArrayList;
import java.util.List;

public class ClubSeason {

    private Club2 club;

    private List<PlayerSeason> players;

    public ClubSeason(String name) {
        club = new Club2(name);
        players = new ArrayList<>();
    }

    public ClubSeason(Club2 club) {
        this.club = club;
        players = new ArrayList<>();
    }

    public Club2 getClub() {
        return club;
    }

    public void setClub(Club2 club) {
        this.club = club;
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
