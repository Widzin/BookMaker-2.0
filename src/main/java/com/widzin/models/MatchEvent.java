package com.widzin.models;

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

    @ManyToOne(fetch = FetchType.EAGER)
    private PlayerSeason playerSeason;

    public MatchEvent() {
    }

    public MatchEvent(Integer minute, PlayerSeason playerSeason, String additionalInformation) {
        this.minute = minute;
        this.playerSeason = playerSeason;
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

    public PlayerSeason getPlayerSeason() {
        return playerSeason;
    }

    public void setPlayerSeason(PlayerSeason playerSeason) {
        this.playerSeason = playerSeason;
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
                ", player='" + playerSeason + '\'' +
                ", additionalInformation='" + additionalInformation + '\'' +
                '}';
    }
}
