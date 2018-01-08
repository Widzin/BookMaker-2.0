package com.widzin.model;

import com.widzin.bootstrap.loaders.parsers.PlayerParser;

import java.util.Date;

public class Player {
    private String name;
    private Date birthDate;

    public Player(String name, Date birthDate) {
        this.name = name;
        this.birthDate = birthDate;
    }

    public String getName() {
        return name;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", birthDate=" + new PlayerParser().getFormattedDate(birthDate) +
                '}';
    }
}
