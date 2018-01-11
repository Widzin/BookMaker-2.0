package com.widzin.models;

import com.widzin.bootstrap.loaders.parsers.PlayerParser;
import com.widzin.bootstrap.loaders.services.MainLoadService;
import com.widzin.bootstrap.loaders.xmlModels.XMLPlayer;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
public class PlayerSeason {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Version
    private Integer version;

    @ManyToOne
    private Player player;

    private String position;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private Date fromWhenInClub;

    private Integer shirtNumber;
    private Double value;

    private Integer matches;
    private Integer goals;
    private Integer ownGoals;
    private Integer cleanSheets;
    private Integer yellowCards;
    private Integer redCards;

    public PlayerSeason() {
        matches = 0;
        goals = 0;
        ownGoals = 0;
        cleanSheets = 0;
        yellowCards = 0;
        redCards = 0;
    }

    public PlayerSeason(Player player, Integer shirtNumber) {
        this.player = player;
        this.shirtNumber = shirtNumber;
        matches = 0;
        goals = 0;
        ownGoals = 0;
        cleanSheets = 0;
        yellowCards = 0;
        redCards = 0;
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

    public Integer getMatches() {
        return matches;
    }

    public void addMatches() {
        matches ++;
    }

    public Integer getGoals() {
        return goals;
    }

    public void addGoals() {
        goals ++;
    }

    public Integer getOwnGoals() {
        return ownGoals;
    }

    public void addOwnGoals() {
        ownGoals ++;
    }

    public Integer getCleanSheets() {
        return cleanSheets;
    }

    public void addCleanSheets() {
        cleanSheets ++;
    }

    public Integer getYellowCards() {
        return yellowCards;
    }

    public void addYellowCards() {
        yellowCards ++;
    }

    public Integer getRedCards() {
        return redCards;
    }

    public void addRedCards() {
        redCards ++;
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
