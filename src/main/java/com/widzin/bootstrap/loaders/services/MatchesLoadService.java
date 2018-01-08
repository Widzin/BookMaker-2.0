package com.widzin.bootstrap.loaders.services;

import com.widzin.bootstrap.loaders.parsers.MatchParser;
import com.widzin.bootstrap.loaders.xmlModels.XMLMatch;
import com.widzin.bootstrap.loaders.xmlModels.XMLMatchesInSeason;
import com.widzin.model.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MatchesLoadService implements LoadService {

    private MatchParser parser;

    @Override
    public void startParsing(String directory, final MainLoadService service) {
        parser = new MatchParser();
        File f = new File(directory);
        parse(f, service);
    }

    @Override
    public void parse(File file, final MainLoadService loadService) {
        try {
            String period = parser.getSeason(file);

            JAXBContext jc = JAXBContext.newInstance(XMLMatchesInSeason.class);

            Unmarshaller unmarshaller = jc.createUnmarshaller();
            XMLMatchesInSeason xmlMatchesInSeason = (XMLMatchesInSeason) unmarshaller.unmarshal(file);

            for (XMLMatch xmlMatch: xmlMatchesInSeason.getMatches()) {
                Match match = parseXmlToObject(xmlMatch, loadService);
                //System.out.println(match.toString());
                loadService.getSeasonByPeriod(period).addMatch(match);
            }
        } catch (JAXBException ex) {
            ex.printStackTrace();
            System.out.println("Cannot create instance of XMLMatchesInSeason Class");
        }
    }

    public Match parseXmlToObject(XMLMatch xmlMatch, MainLoadService service) {
        Match match = new Match();

        //------------- Setting match details ----------------
        match.setDate(xmlMatch.getDate());
        match.setRound(xmlMatch.getRound());
        match.setPeriod(service.getPeriodByMatchDate(xmlMatch.getDate()));

        //------------- Setting home team details ------------
        match.getHome().setName(xmlMatch.getHomeTeam());
        match.getHome().setGoals(xmlMatch.getHomeGoals());
        match.getHome().setShots(xmlMatch.getHomeShots());
        match.getHome().setShotsOnTarget(xmlMatch.getHomeShotsOnTarget());

        match.getHome().setLineupGoalkeeper(getPlayersInArray(service, xmlMatch.getHomeLineupGoalkeeper(),
                match.getHome().getName(), match.getPeriod()).get(0));
        match.getHome().setLineupDefense(getPlayersInArray(service, xmlMatch.getHomeLineupDefense(),
                match.getHome().getName(), match.getPeriod()));
        match.getHome().setLineupMidfield(getPlayersInArray(service, xmlMatch.getHomeLineupMidfield(),
                match.getHome().getName(), match.getPeriod()));
        match.getHome().setLineupForward(getPlayersInArray(service, xmlMatch.getHomeLineupForward(),
                match.getHome().getName(), match.getPeriod()));
        match.getHome().setFormation(xmlMatch.getHomeTeamFormation());
        match.getHome().setLineupSubstitutes(getPlayersInArray(service, xmlMatch.getHomeLineupSubstitutes(),
                match.getHome().getName(), match.getPeriod()));

        match.getHome().setGoalDetails(getEventsInArray(xmlMatch.getHomeGoalDetails(), Event.GOAL));
        match.getHome().setSubDetails(getEventsInArray(xmlMatch.getHomeSubDetails(), Event.SUBSTITUTION));
        match.getHome().setRedCardDetails(getEventsInArray(xmlMatch.getHomeTeamRedCardDetails(), Event.RED_CARD));

        //------------- Setting away team details ------------
        match.getAway().setName(xmlMatch.getAwayTeam());
        match.getAway().setGoals(xmlMatch.getAwayGoals());
        match.getAway().setShots(xmlMatch.getAwayShots());
        match.getAway().setShotsOnTarget(xmlMatch.getAwayShotsOnTarget());

        match.getAway().setLineupGoalkeeper(getPlayersInArray(service, xmlMatch.getAwayLineupGoalkeeper(),
                match.getAway().getName(), match.getPeriod()).get(0));
        match.getAway().setLineupDefense(getPlayersInArray(service, xmlMatch.getAwayLineupDefense(),
                match.getAway().getName(), match.getPeriod()));
        match.getAway().setLineupMidfield(getPlayersInArray(service, xmlMatch.getAwayLineupMidfield(),
                match.getAway().getName(), match.getPeriod()));
        match.getAway().setLineupForward(getPlayersInArray(service, xmlMatch.getAwayLineupForward(),
                match.getAway().getName(), match.getPeriod()));
        match.getAway().setFormation(xmlMatch.getAwayTeamFormation());
        match.getAway().setLineupSubstitutes(getPlayersInArray(service, xmlMatch.getAwayLineupSubstitutes(),
                match.getAway().getName(), match.getPeriod()));

        match.getAway().setGoalDetails(getEventsInArray(xmlMatch.getAwayGoalDetails(), Event.GOAL));
        match.getAway().setSubDetails(getEventsInArray(xmlMatch.getAwaySubDetails(), Event.SUBSTITUTION));
        match.getAway().setRedCardDetails(getEventsInArray(xmlMatch.getAwayTeamRedCardDetails(), Event.RED_CARD));

        return match;
    }

    public List<PlayerSeason> getPlayersInArray(MainLoadService service, String array, String clubName, String period) {
        List<String> stringPlayers = getStringPlayersInArray(array);
        List<PlayerSeason> filteredPlayers = service.getPlayersFromClubAndSeason(clubName, period);
        List<PlayerSeason> players = new ArrayList<>();
        for (String s: stringPlayers) {
            String beginningName = parser.getPartOfPlayerName(s);
            List<PlayerSeason> playerSeasonList = service.getPlayerByNameFromPlayers(filteredPlayers, beginningName);
            if (playerSeasonList.size() > 1) {
                String lastPartOfName = parser.getRestOfPlayerName(s);
                List<PlayerSeason> onePlayerInList = service.getPlayerByNameFromPlayers(playerSeasonList, lastPartOfName);
                if (onePlayerInList.size() == 0) {
                    System.out.println("");
                }
                players.add(onePlayerInList.get(0));
            } else if (playerSeasonList.size() == 1) {
                players.add(playerSeasonList.get(0));
            }
        }
        if (players.size() == 0) {
            System.out.println("Dupa");
        }
        return players;
    }

    public List<String> getStringPlayersInArray(String array) {
        if (array.contains(";"))
            return Arrays.asList(array.split(";"));
        else
            return Arrays.asList(array);
    }

    public List<MatchEvent> getEventsInArray(String array, Event event) {
        if (array.contains(";")) {
            String[] eventsString = array.split(";");

            List<MatchEvent> matchEvents = new ArrayList<>();

            for (String s: eventsString) {
                String[] parts = s.split("':");
                MatchEvent me = null;
                switch (event) {
                    case GOAL:
                        if (parts[1].trim().startsWith(Event.OWN.getEvent()))
                            me = fromStringToMatchEvent(parts[0], parts[1], Event.OWN.getEvent());
                        else if (parts[1].trim().startsWith(Event.PENALTY.getEvent()))
                            me = fromStringToMatchEvent(parts[0], parts[1], Event.PENALTY.getEvent());
                        else
                            me = new MatchEvent(Integer.parseInt(parts[0].trim()),
                                    parts[1].trim(), Event.GOAL.getEvent());

                        break;
                    case SUBSTITUTION:
                        if (parts[1].trim().startsWith(Event.IN.getEvent()))
                            me = fromStringToMatchEvent(parts[0], parts[1], Event.IN.getEvent());
                        else
                            me = fromStringToMatchEvent(parts[0], parts[1], Event.OUT.getEvent());
                        break;
                    case RED_CARD:
                        me = new MatchEvent(Integer.parseInt(parts[0].trim()),
                                parts[1].trim(), Event.RED_CARD.getEvent());
                        break;
                }
                if (me != null)
                    matchEvents.add(me);
            }
            return matchEvents;
        } else
            return new ArrayList<>();
    }

    private MatchEvent fromStringToMatchEvent(String part_0, String part_1, String addInfo) {
        String regex = "\\s*\\b" + addInfo + "\\b\\s*";
        return new MatchEvent(Integer.parseInt(part_0.trim()),
                part_1.replaceAll(regex, "").trim(),
                addInfo);
    }
}
