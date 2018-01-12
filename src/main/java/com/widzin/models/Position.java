package com.widzin.models;

public enum Position {
    BR("Bramkarz"),
    CF("Cofnięty napastnik"),
    ŚPD("Defensywny pomocnik"),
    LN("Lewy napastnik"),
    LO("Lewy obrońca"),
    LP("Lewy pomocnik"),
    ŚPO("Ofensywny pomocnik"),
    PN("Prawy napastnik"),
    PO("Prawy obrońca"),
    PP("Prawy pomocnik"),
    ŚN("Środkowy napastnik"),
    ŚO("Środkowy obrońca"),
    ŚP("Środkowy pomocnik");

    private String pos;

    Position(String pos) {
        this.pos = pos;
    }

    public String getPosition() {
        return pos;
    }
}
