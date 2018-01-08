package com.widzin.bootstrap.loaders.xmlModels;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "XMLSOCCER.COM")
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLMatchesInSeason {

    @XmlElement(name="Match")
    private List<XMLMatch> matches;

    public List<XMLMatch> getMatches() {
        return matches;
    }
}
