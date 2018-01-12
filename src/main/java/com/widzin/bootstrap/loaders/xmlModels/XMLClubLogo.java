package com.widzin.bootstrap.loaders.xmlModels;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class XMLClubLogo {

    @XmlElement(name = "club")
    private String clubName;

    @XmlElement(name = "url")
    private String clubUrl;

    public String getClubName() {
        return clubName;
    }

    public String getClubUrl() {
        return clubUrl;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public void setClubUrl(String clubUrl) {
        this.clubUrl = clubUrl;
    }
}
