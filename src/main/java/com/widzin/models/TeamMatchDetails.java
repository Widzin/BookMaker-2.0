package com.widzin.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class TeamMatchDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Version
    private Integer version;

    @ManyToOne(fetch = FetchType.EAGER)
    private ClubSeason clubSeason;

    private Integer goals;
    private Integer shots;
    private Integer shotsOnTarget;

    @ManyToOne(fetch = FetchType.EAGER)
    private PlayerSeason lineupGoalkeeper;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "player_season_id")
    private List<PlayerSeason> lineupDefense;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "player_season_id")
    private List<PlayerSeason> lineupMidfield;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "player_season_id")
    private List<PlayerSeason> lineupForward;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "player_season_id")
    private List<PlayerSeason> lineupSubstitutes;

    private String formation;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "match_event_id")
    private List<MatchEvent> goalDetails;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "match_event_id")
    private List<MatchEvent> yellowCardDetails;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "match_event_id")
    private List<MatchEvent> redCardDetails;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "match_event_id")
    private List<MatchEvent> subDetails;

    public TeamMatchDetails() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public ClubSeason getClubSeason() {
        return clubSeason;
    }

    public void setClubSeason(ClubSeason clubSeason) {
        this.clubSeason = clubSeason;
    }

    public Integer getGoals() {
        return goals;
    }

    public void setGoals(Integer goals) {
        this.goals = goals;
    }

    public Integer getShots() {
        return shots;
    }

    public void setShots(Integer shots) {
        this.shots = shots;
    }

    public Integer getShotsOnTarget() {
        return shotsOnTarget;
    }

    public void setShotsOnTarget(Integer shotsOnTarget) {
        this.shotsOnTarget = shotsOnTarget;
    }

    public List<MatchEvent> getGoalDetails() {
        return goalDetails;
    }

    public void setGoalDetails(List<MatchEvent> goalDetails) {
        this.goalDetails = goalDetails;
    }

    public PlayerSeason getLineupGoalkeeper() {
        return lineupGoalkeeper;
    }

    public void setLineupGoalkeeper(PlayerSeason lineupGoalkeeper) {
        this.lineupGoalkeeper = lineupGoalkeeper;
    }

    public List<PlayerSeason> getLineupDefense() {
        return lineupDefense;
    }

    public void setLineupDefense(List<PlayerSeason> lineupDefense) {
        this.lineupDefense = lineupDefense;
    }

    public List<PlayerSeason> getLineupMidfield() {
        return lineupMidfield;
    }

    public void setLineupMidfield(List<PlayerSeason> lineupMidfield) {
        this.lineupMidfield = lineupMidfield;
    }

    public List<PlayerSeason> getLineupForward() {
        return lineupForward;
    }

    public void setLineupForward(List<PlayerSeason> lineupForward) {
        this.lineupForward = lineupForward;
    }

    public String getFormation() {
        return formation;
    }

    public void setFormation(String formation) {
        this.formation = formation;
    }

    public List<PlayerSeason> getLineupSubstitutes() {
        return lineupSubstitutes;
    }

    public void setLineupSubstitutes(List<PlayerSeason> lineupSubstitutes) {
        this.lineupSubstitutes = lineupSubstitutes;
    }

    public List<MatchEvent> getYellowCardDetails() {
        return yellowCardDetails;
    }

    public void setYellowCardDetails(List<MatchEvent> yellowCardDetails) {
        this.yellowCardDetails = yellowCardDetails;
    }

    public List<MatchEvent> getRedCardDetails() {
        return redCardDetails;
    }

    public void setRedCardDetails(List<MatchEvent> redCardDetails) {
        this.redCardDetails = redCardDetails;
    }

    public List<MatchEvent> getSubDetails() {
        return subDetails;
    }

    public void setSubDetails(List<MatchEvent> subDetails) {
        this.subDetails = subDetails;
    }

    @Override
    public String toString() {
        return "TeamMatchDetails{" +
                "id=" + id +
                ", version=" + version +
                ", clubSeason=" + clubSeason +
                ", goals=" + goals +
                ", shots=" + shots +
                ", shotsOnTarget=" + shotsOnTarget +
                ", lineupGoalkeeper=" + lineupGoalkeeper +
                ", lineupDefense=" + lineupDefense +
                ", lineupMidfield=" + lineupMidfield +
                ", lineupForward=" + lineupForward +
                ", lineupSubstitutes=" + lineupSubstitutes +
                ", formation='" + formation + '\'' +
                ", goalDetails=" + goalDetails +
                ", yellowCardDetails=" + yellowCardDetails +
                ", redCardDetails=" + redCardDetails +
                ", subDetails=" + subDetails +
                '}';
    }
}
