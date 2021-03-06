package com.widzin.bootstrap.loaders.xmlModels;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import java.util.Date;

@XmlRootElement(name = "Match")
public class XMLMatch {
    // -------------------- Match details -------------------------------
    @XmlElement(name = "Id")
    private Integer id;

    @XmlElement(name = "Date")
    @XmlSchemaType(name = "dateTime")
    private Date date;

    @XmlElement(name = "Round")
    private Integer round;

    // -------------------- Home team stats -------------------------------
    @XmlElement(name = "HomeTeam")
    private String homeTeam;

    @XmlElement(name = "HomeGoals")
    private Integer homeGoals;

    @XmlElement(name = "HomeGoalDetails")
    private String homeGoalDetails;

    @XmlElement(name = "HomeLineupGoalkeeper")
    private String homeLineupGoalkeeper;

    @XmlElement(name = "HomeLineupDefense")
    private String homeLineupDefense;

    @XmlElement(name = "HomeLineupMidfield")
    private String homeLineupMidfield;

    @XmlElement(name = "HomeLineupForward")
    private String homeLineupForward;

    @XmlElement(name = "HomeTeamFormation")
    private String homeTeamFormation;

    @XmlElement(name = "HomeLineupSubstitutes")
    private String homeLineupSubstitutes;

    @XmlElement(name = "HomeTeamYellowCardDetails")
    private String homeTeamYellowCardDetails;

    @XmlElement(name = "HomeTeamRedCardDetails")
    private String homeTeamRedCardDetails;

    @XmlElement(name = "HomeSubDetails")
    private String homeSubDetails;

    // -------------------- Away team stats -------------------------------
    @XmlElement(name = "AwayTeam")
    private String awayTeam;

    @XmlElement(name = "AwayGoals")
    private Integer awayGoals;

    @XmlElement(name = "AwayGoalDetails")
    private String awayGoalDetails;

    @XmlElement(name = "AwayLineupGoalkeeper")
    private String awayLineupGoalkeeper;

    @XmlElement(name = "AwayLineupDefense")
    private String awayLineupDefense;

    @XmlElement(name = "AwayLineupMidfield")
    private String awayLineupMidfield;

    @XmlElement(name = "AwayLineupForward")
    private String awayLineupForward;

    @XmlElement(name = "AwayTeamFormation")
    private String awayTeamFormation;

    @XmlElement(name = "AwayLineupSubstitutes")
    private String awayLineupSubstitutes;

    @XmlElement(name = "AwayTeamYellowCardDetails")
    private String awayTeamYellowCardDetails;

    @XmlElement(name = "AwayTeamRedCardDetails")
    private String awayTeamRedCardDetails;

    @XmlElement(name = "AwaySubDetails")
    private String awaySubDetails;

    public XMLMatch() {
    }

    public Integer getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public Integer getRound() {
        return round;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public Integer getHomeGoals() {
        return homeGoals;
    }

    public String getHomeGoalDetails() {
        return homeGoalDetails;
    }

    public String getHomeLineupGoalkeeper() {
        return homeLineupGoalkeeper;
    }

    public String getHomeLineupDefense() {
        return homeLineupDefense;
    }

    public String getHomeLineupMidfield() {
        return homeLineupMidfield;
    }

    public String getHomeLineupForward() {
        return homeLineupForward;
    }

    public String getHomeTeamFormation() {
        return homeTeamFormation;
    }

    public String getHomeLineupSubstitutes() {
        return homeLineupSubstitutes;
    }

    public String getHomeTeamYellowCardDetails() {
        return homeTeamYellowCardDetails;
    }

    public String getHomeTeamRedCardDetails() {
        return homeTeamRedCardDetails;
    }

    public String getHomeSubDetails() {
        return homeSubDetails;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public Integer getAwayGoals() {
        return awayGoals;
    }

    public String getAwayGoalDetails() {
        return awayGoalDetails;
    }

    public String getAwayLineupGoalkeeper() {
        return awayLineupGoalkeeper;
    }

    public String getAwayLineupDefense() {
        return awayLineupDefense;
    }

    public String getAwayLineupMidfield() {
        return awayLineupMidfield;
    }

    public String getAwayLineupForward() {
        return awayLineupForward;
    }

    public String getAwayTeamFormation() {
        return awayTeamFormation;
    }

    public String getAwayLineupSubstitutes() {
        return awayLineupSubstitutes;
    }

    public String getAwayTeamYellowCardDetails() {
        return awayTeamYellowCardDetails;
    }

    public String getAwayTeamRedCardDetails() {
        return awayTeamRedCardDetails;
    }

    public String getAwaySubDetails() {
        return awaySubDetails;
    }

    @Override
    public String toString() {
        return "XMLMatch{" +
                "id=" + id +
                ", date=" + date +
                ", round=" + round +
                ", homeTeam='" + homeTeam + '\'' +
                ", homeGoals=" + homeGoals +
                ", homeGoalDetails='" + homeGoalDetails + '\'' +
                ", homeLineupGoalkeeper='" + homeLineupGoalkeeper + '\'' +
                ", homeLineupDefense='" + homeLineupDefense + '\'' +
                ", homeLineupMidfield='" + homeLineupMidfield + '\'' +
                ", homeLineupForward='" + homeLineupForward + '\'' +
                ", homeTeamFormation='" + homeTeamFormation + '\'' +
                ", homeLineupSubstitutes='" + homeLineupSubstitutes + '\'' +
                ", homeTeamYellowCardDetails='" + homeTeamYellowCardDetails + '\'' +
                ", homeTeamRedCardDetails='" + homeTeamRedCardDetails + '\'' +
                ", homeSubDetails='" + homeSubDetails + '\'' +
                ", awayTeam='" + awayTeam + '\'' +
                ", awayGoals=" + awayGoals +
                ", awayGoalDetails='" + awayGoalDetails + '\'' +
                ", awayLineupGoalkeeper='" + awayLineupGoalkeeper + '\'' +
                ", awayLineupDefense='" + awayLineupDefense + '\'' +
                ", awayLineupMidfield='" + awayLineupMidfield + '\'' +
                ", awayLineupForward='" + awayLineupForward + '\'' +
                ", awayTeamFormation='" + awayTeamFormation + '\'' +
                ", awayLineupSubstitutes='" + awayLineupSubstitutes + '\'' +
                ", awayTeamYellowCardDetails='" + awayTeamYellowCardDetails + '\'' +
                ", awayTeamRedCardDetails='" + awayTeamRedCardDetails + '\'' +
                ", awaySubDetails='" + awaySubDetails + '\'' +
                '}';
    }
}