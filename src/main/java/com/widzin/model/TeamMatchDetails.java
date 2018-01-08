package com.widzin.model;

import java.util.List;

public class TeamMatchDetails {

    private String name;
    private Integer goals;
    private Integer shots;
    private Integer shotsOnTarget;
    private List<MatchEvent> goalDetails;
    private PlayerSeason lineupGoalkeeper;
    private List<PlayerSeason> lineupDefense;
    private List<PlayerSeason> lineupMidfield;
    private List<PlayerSeason> lineupForward;
    private String formation;
    private List<PlayerSeason> lineupSubstitutes;
    private List<MatchEvent> redCardDetails;
    private List<MatchEvent> subDetails;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
                "name='" + name + '\'' +
                ", goals=" + goals +
                ", shots=" + shots +
                ", shotsOnTarget=" + shotsOnTarget +
                ", goalDetails=" + goalDetails +
                ", lineupGoalkeeper=" + lineupGoalkeeper +
                ", lineupDefense=" + lineupDefense +
                ", lineupMidfield=" + lineupMidfield +
                ", lineupForward=" + lineupForward +
                ", formation='" + formation + '\'' +
                ", lineupSubstitutes=" + lineupSubstitutes +
                ", redCardDetails=" + redCardDetails +
                ", subDetails=" + subDetails +
                '}';
    }
}
