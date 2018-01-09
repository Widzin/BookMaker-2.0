package com.widzin.model;

import javax.persistence.*;

@Entity
@Table
public class MatchEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Version
    private Integer version;

    private Integer minute;
    private String additionalInformation;

    @OneToOne
    @JoinColumn(name = "player_season_id")
    private PlayerSeason player;

    public MatchEvent(Integer minute, PlayerSeason player, String additionalInformation) {
        this.minute = minute;
        this.player = player;
        this.additionalInformation = additionalInformation;
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

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public PlayerSeason getPlayer() {
        return player;
    }

    public void setPlayer(PlayerSeason player) {
        this.player = player;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    @Override
    public String toString() {
        return "MatchEvent{" +
                "minute=" + minute +
                ", player='" + player + '\'' +
                ", additionalInformation='" + additionalInformation + '\'' +
                '}';
    }
}
