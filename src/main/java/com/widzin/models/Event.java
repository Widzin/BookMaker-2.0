package com.widzin.models;

public enum Event {
    GOAL("goal"),
    YELLOW_CARD("yellow card"),
    RED_CARD("red card"),
    SUBSTITUTION("substitution"),
    OWN("own"),
    PENALTY("penalty"),
    IN("in"),
    OUT("out");

    private String event;

    Event(String event) {
        this.event = event;
    }

    public String getEvent() {
        return event;
    }
}
