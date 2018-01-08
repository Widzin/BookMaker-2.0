package com.widzin.model;

import com.widzin.bootstrap.loaders.parsers.PlayerParser;
import com.widzin.bootstrap.loaders.services.MainLoadService;
import com.widzin.bootstrap.loaders.xmlModels.XMLPlayer;

import java.util.Date;

public class PlayerSeason {

    private Player player;

    private String position;
    private Date fromWhenInClub;

    private Integer shirtNumber;
    private Double value;

    public PlayerSeason(Player player, Integer shirtNumber) {
        this.player = player;
        this.shirtNumber = shirtNumber;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Date getFromWhenInClub() {
        return fromWhenInClub;
    }

    public void setFromWhenInClub(Date fromWhenInClub) {
        this.fromWhenInClub = fromWhenInClub;
    }

    public Integer getShirtNumber() {
        return shirtNumber;
    }

    public void setShirtNumber(Integer shirtNumber) {
        this.shirtNumber = shirtNumber;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "PlayerSeason{" +
                "player=" + player +
                ", position='" + position + '\'' +
                ", fromWhenInClub=" + new PlayerParser().getFormattedDate(fromWhenInClub) +
                ", shirtNumber=" + shirtNumber +
                ", value=" + new PlayerParser().getFormattedValue(value) +
                '}';
    }
}