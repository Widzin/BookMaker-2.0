package com.widzin.bootstrap.loaders.xmlModels;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "WEBHARVY_DATA")
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLClubSeason {
    @XmlElement(name="item")
    private List<XMLPlayer> players;

    public List<XMLPlayer> getPlayers() {
        return players;
    }
}
