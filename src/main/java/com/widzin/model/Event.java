package com.widzin.model;

public enum Event {
    GOAL("goal"),
    RED_CARD("red card"),
    SUBSTITUTION("substitution"),
    OWN("Own"),
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
