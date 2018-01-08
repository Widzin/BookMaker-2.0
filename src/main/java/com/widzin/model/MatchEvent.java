package com.widzin.model;

public class MatchEvent {

    private Integer minute;
    private String player;
    private String additionalInformation;

    public MatchEvent(Integer minute, String player, String additionalInformation) {
        this.minute = minute;
        this.player = player;
        this.additionalInformation = additionalInformation;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
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
