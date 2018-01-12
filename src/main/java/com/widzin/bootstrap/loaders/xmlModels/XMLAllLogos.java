package com.widzin.bootstrap.loaders.xmlModels;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "allLogos")
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLAllLogos {

    @XmlElement(name="pair")
    private List<XMLClubLogo> logos;

    public List<XMLClubLogo> getLogos() {
        return logos;
    }
}
