package com.widzin.models;

public enum Position {
    GK("Bramkarz"),
    CF("Cofnięty napastnik"),
    CDM("Defensywny pomocnik"),
    LW("Lewy napastnik"),
    LB("Lewy obrońca"),
    LM("Lewy pomocnik"),
    CAM("Ofensywny pomocnik"),
    RW("Prawy napastnik"),
    RB("Prawy obrońca"),
    RM("Prawy pomocnik"),
    ST("Środkowy napastnik"),
    CB("Środkowy obrońca"),
    CM("Środkowy pomocnik");

    private String pos;

    Position(String pos) {
        this.pos = pos;
    }

    public String getPosition() {
        return pos;
    }
}
