package com.widzin.models;

import com.widzin.bootstrap.loaders.parsers.PlayerParser;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Version
    private Integer version;

    private String name;
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private Date birthDate;

    @OneToMany(mappedBy = "player", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PlayerSeason> playerSeasonList;

    public Player() {
    }

    public Player(String name, Date birthDate) {
        this.name = name;
        this.birthDate = birthDate;
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

    public String getName() {
        return name;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public List<PlayerSeason> getPlayerSeasonList() {
        return playerSeasonList;
    }

    public void setPlayerSeasonList(List<PlayerSeason> playerSeasonList) {
        this.playerSeasonList = playerSeasonList;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", birthDate=" + new PlayerParser().getFormattedDate(birthDate) +
                '}';
    }
}
