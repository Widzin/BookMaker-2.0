package com.widzin.bootstrap.loaders.xmlModels;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class XMLPlayer {
    @XmlElement(name = "Nazwa", nillable = false)
    private String name;

    @XmlElement(name = "Numer", nillable = true)
    private Integer number;

    @XmlElement(name = "Pozycja", nillable = false)
    private String position;

    @XmlElement(name = "Data_urodzenia_wiek", nillable = false)
    private String birthDateAndAge;

    @XmlElement(name = "Wartosc_rynkowa", nillable = true)
    private String valueStr;

    @XmlElement(name = "Od_kiedy", nillable = true)
    private String fromWhenStr;

    public String getName() {
        return name;
    }

    public Integer getNumber() {
        return number;
    }

    public String getPosition() {
        return position;
    }

    public String getBirthDateAndAge() {
        return birthDateAndAge;
    }

    public String getValueStr() {
        return valueStr;
    }

    public String getFromWhenStr() {
        return fromWhenStr;
    }

    @Override
    public String toString() {
        return "NewPlayer [name=" + name + ", number=" + number + ", position=" + position + ", birthDateAndAge="
                + birthDateAndAge + ", valueStr=" + valueStr + ", fromWhenStr=" + fromWhenStr + "]";
    }
}
