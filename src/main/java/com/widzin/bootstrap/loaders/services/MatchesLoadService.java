package com.widzin.bootstrap.loaders.services;

import com.widzin.bootstrap.loaders.parsers.MatchParser;
import com.widzin.bootstrap.loaders.xmlModels.XMLMatch;
import com.widzin.bootstrap.loaders.xmlModels.XMLMatchesInSeason;
import com.widzin.models.*;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MatchesLoadService implements LoadService {

    private final static Integer POINTS_FOR_WIN = 3;
    private final static Integer POINTS_FOR_DRAW = 1;
    private final static Integer POINTS_FOR_LOST = 0;

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
        match.setPlayed(true);
        match.setRound(xmlMatch.getRound());
        match.setPeriod(service.getPeriodByMatchDate(xmlMatch.getDate()));

        //------------- Setting home team details ------------
        match.getHome().setClubSeason(
                service.getClubSeasonByPeriodAndName(match.getPeriod(), xmlMatch.getHomeTeam()));
        match.getHome().setGoals(xmlMatch.getHomeGoals());

        //------------- Setting away team details ------------
        match.getAway().setClubSeason(
                service.getClubSeasonByPeriodAndName(match.getPeriod(), xmlMatch.getAwayTeam()));
        match.getAway().setGoals(xmlMatch.getAwayGoals());

        //------------- Setting home squad -------------------
        match.getHome().setLineupGoalkeeper(getPlayersInArray(service, xmlMatch.getHomeLineupGoalkeeper(),
                match.getHome().getClubSeason().getClub2().getName(), match.getPeriod()).get(0));
        match.getHome().setLineupDefense(getPlayersInArray(service, xmlMatch.getHomeLineupDefense(),
                match.getHome().getClubSeason().getClub2().getName(), match.getPeriod()));
        match.getHome().setLineupMidfield(getPlayersInArray(service, xmlMatch.getHomeLineupMidfield(),
                match.getHome().getClubSeason().getClub2().getName(), match.getPeriod()));
        match.getHome().setLineupForward(getPlayersInArray(service, xmlMatch.getHomeLineupForward(),
                match.getHome().getClubSeason().getClub2().getName(), match.getPeriod()));
        match.getHome().setFormation(xmlMatch.getHomeTeamFormation());
        match.getHome().setLineupSubstitutes(getPlayersInArray(service, xmlMatch.getHomeLineupSubstitutes(),
                match.getHome().getClubSeason().getClub2().getName(), match.getPeriod()));

        //------------- Setting away squad -------------------
        match.getAway().setLineupGoalkeeper(getPlayersInArray(service, xmlMatch.getAwayLineupGoalkeeper(),
                match.getAway().getClubSeason().getClub2().getName(), match.getPeriod()).get(0));
        match.getAway().setLineupDefense(getPlayersInArray(service, xmlMatch.getAwayLineupDefense(),
                match.getAway().getClubSeason().getClub2().getName(), match.getPeriod()));
        match.getAway().setLineupMidfield(getPlayersInArray(service, xmlMatch.getAwayLineupMidfield(),
                match.getAway().getClubSeason().getClub2().getName(), match.getPeriod()));
        match.getAway().setLineupForward(getPlayersInArray(service, xmlMatch.getAwayLineupForward(),
                match.getAway().getClubSeason().getClub2().getName(), match.getPeriod()));
        match.getAway().setFormation(xmlMatch.getAwayTeamFormation());
        match.getAway().setLineupSubstitutes(getPlayersInArray(service, xmlMatch.getAwayLineupSubstitutes(),
                match.getAway().getClubSeason().getClub2().getName(), match.getPeriod()));

        //-------------- Setting match events ----------------

        List<PlayerSeason> homePlayers = getPlayersFromOneTeam(match.getHome());
        List<PlayerSeason> awayPlayers = getPlayersFromOneTeam(match.getAway());

        match.getHome().setGoalDetails(getEventsInArray(xmlMatch.getHomeGoalDetails(), Event.GOAL, service, homePlayers, awayPlayers));
        match.getHome().setSubDetails(getEventsInArray(xmlMatch.getHomeSubDetails(), Event.SUBSTITUTION, service, homePlayers, awayPlayers));
        match.getHome().setYellowCardDetails(getEventsInArray(xmlMatch.getHomeTeamYellowCardDetails(), Event.YELLOW_CARD, service, homePlayers, awayPlayers));
        match.getHome().setRedCardDetails(getEventsInArray(xmlMatch.getHomeTeamRedCardDetails(), Event.RED_CARD, service, homePlayers, awayPlayers));

        match.getAway().setGoalDetails(getEventsInArray(xmlMatch.getAwayGoalDetails(), Event.GOAL, service, awayPlayers, homePlayers));
        match.getAway().setSubDetails(getEventsInArray(xmlMatch.getAwaySubDetails(), Event.SUBSTITUTION, service, awayPlayers, homePlayers));
        match.getAway().setYellowCardDetails(getEventsInArray(xmlMatch.getAwayTeamYellowCardDetails(), Event.YELLOW_CARD, service, awayPlayers, homePlayers));
        match.getAway().setRedCardDetails(getEventsInArray(xmlMatch.getAwayTeamRedCardDetails(), Event.RED_CARD, service, awayPlayers, homePlayers));

        //-------------- Adding stats to goalkeepers ---------
        if (match.getHome().getGoals() == 0)
            match.getAway().getLineupGoalkeeper().addCleanSheets();
        if (match.getAway().getGoals() == 0)
            match.getHome().getLineupGoalkeeper().addCleanSheets();

        //-------------- Adding stats to clubs ---------------

        if (match.getHome().getGoals() > match.getAway().getGoals()) {
            match.getHome().getClubSeason().addWin();
            match.getAway().getClubSeason().addLose();

            match.getHome().getClubSeason().addPoints(POINTS_FOR_WIN);
            match.getAway().getClubSeason().addPoints(POINTS_FOR_LOST);
        } else if (match.getHome().getGoals() < match.getAway().getGoals()) {
            match.getHome().getClubSeason().addLose();
            match.getAway().getClubSeason().addWin();

            match.getHome().getClubSeason().addPoints(POINTS_FOR_LOST);
            match.getAway().getClubSeason().addPoints(POINTS_FOR_WIN);
        } else {
            match.getHome().getClubSeason().addDraw();
            match.getAway().getClubSeason().addDraw();

            match.getHome().getClubSeason().addPoints(POINTS_FOR_DRAW);
            match.getAway().getClubSeason().addPoints(POINTS_FOR_DRAW);
        }

        match.getHome().getClubSeason().addMatch();
        match.getAway().getClubSeason().addMatch();

        match.getHome().getClubSeason().addScoredGoals(match.getHome().getGoals());
        match.getAway().getClubSeason().addScoredGoals(match.getAway().getGoals());

        match.getHome().getClubSeason().addLostGoals(match.getAway().getGoals());
        match.getAway().getClubSeason().addLostGoals(match.getHome().getGoals());

        match.getHome().getClubSeason().setBilans();
        match.getAway().getClubSeason().setBilans();

        return match;
    }

    public List<PlayerSeason> getPlayersInArray(MainLoadService service, String array, String clubName, String period) {
        List<String> stringPlayers = getStringPlayersInArray(array);
        List<PlayerSeason> filteredPlayers = service.getPlayersFromClubAndSeason(clubName, period);
        List<PlayerSeason> players = new ArrayList<>();
        for (String s: stringPlayers) {
            players.add(getPlayerFromPlayersByName(s, service, filteredPlayers));
        }
        return players;
    }

    public List<String> getStringPlayersInArray(String array) {
        if (array.contains(";"))
            return Arrays.asList(array.split(";"));
        else
            return Arrays.asList(array);
    }

    public List<PlayerSeason> getPlayersFromOneTeam(TeamMatchDetails team) {
        List<PlayerSeason> players = new ArrayList<>();

        players.add(team.getLineupGoalkeeper());
        players.addAll(team.getLineupDefense());
        players.addAll(team.getLineupMidfield());
        players.addAll(team.getLineupForward());
        for (PlayerSeason playerSeason: players) {
            playerSeason.addMatches();
        }
        players.addAll(team.getLineupSubstitutes());

        return players;
    }

    public List<MatchEvent> getEventsInArray(String array, Event event, MainLoadService service, List<PlayerSeason> ourPlayers, List<PlayerSeason> theirPlayers) {
        if (array.contains(";")) {
            String[] eventsString = array.split(";");

            List<MatchEvent> matchEvents = new ArrayList<>();

            for (String s: eventsString) {
                String[] parts = s.split("':");
                MatchEvent me = null;
                switch (event) {
                    case GOAL:
                        if (parts[1].trim().startsWith(Event.OWN.getEvent()))
                            me = fromStringToMatchEvent(parts[0], parts[1], Event.OWN.getEvent(), service, theirPlayers);
                        else if (parts[1].trim().startsWith(Event.PENALTY.getEvent()))
                            me = fromStringToMatchEvent(parts[0], parts[1], Event.PENALTY.getEvent(), service, ourPlayers);
                        else {
                            PlayerSeason playerSeason = getPlayerFromPlayersByName(parts[1].trim(),
                                    service, ourPlayers);
                            playerSeason.addGoals();
                            me = new MatchEvent(Integer.parseInt(parts[0].trim()),
                                    playerSeason, Event.GOAL.getEvent());
                        }
                        break;
                    case SUBSTITUTION:
                        if (parts[1].trim().startsWith(Event.IN.getEvent()))
                            me = fromStringToMatchEvent(parts[0], parts[1], Event.IN.getEvent(), service, ourPlayers);
                        else
                            me = fromStringToMatchEvent(parts[0], parts[1], Event.OUT.getEvent(), service, ourPlayers);
                        break;
                    case YELLOW_CARD:
                        PlayerSeason playerSeason1 = getPlayerFromPlayersByName(parts[1].trim(),
                                service, ourPlayers);
                        playerSeason1.addYellowCards();
                        me = new MatchEvent(Integer.parseInt(parts[0].trim()),
                                playerSeason1, Event.YELLOW_CARD.getEvent());
                        break;
                    case RED_CARD:
                        PlayerSeason playerSeason2 = getPlayerFromPlayersByName(parts[1].trim(),
                                service, ourPlayers);
                        playerSeason2.addRedCards();
                        me = new MatchEvent(Integer.parseInt(parts[0].trim()),
                                playerSeason2, Event.RED_CARD.getEvent());
                        break;
                }
                if (me != null)
                    matchEvents.add(me);
            }
            return matchEvents;
        } else
            return new ArrayList<>();
    }

    private MatchEvent fromStringToMatchEvent(String part_0, String part_1, String addInfo, MainLoadService service, List<PlayerSeason> players) {
        String regex = "\\s*\\b" + addInfo + "\\b\\s*";

        PlayerSeason playerSeason = getPlayerFromPlayersByName(part_1.replaceAll(regex, "").trim(),
                service, players);

        switch (addInfo){
            case "penalty":
                playerSeason.addGoals();
                break;
            case "own":
                playerSeason.addOwnGoals();
                break;
            case "in":
                playerSeason.addMatches();
                break;
        }

        return new MatchEvent(Integer.parseInt(part_0.trim()),
                playerSeason, addInfo);
    }

    private PlayerSeason getPlayerFromPlayersByName(String name, MainLoadService service, List<PlayerSeason> filteredPlayers) {
        List<PlayerSeason> players = new ArrayList<>();

        String beginningName = parser.getPartOfPlayerName(name);
        List<PlayerSeason> playerSeasonList = service.getPlayersFromPlayersByName(filteredPlayers, beginningName);
        if (playerSeasonList.size() > 1) {
            String lastPartOfName = parser.getRestOfPlayerName(name);
            List<PlayerSeason> onePlayerInList = service.getPlayersFromPlayersByName(playerSeasonList, lastPartOfName);
            if (onePlayerInList.size() == 0 || onePlayerInList.size() > 1) {
                System.out.println("");
            }
            players.add(onePlayerInList.get(0));
        } else if (playerSeasonList.size() == 1) {
            players.add(playerSeasonList.get(0));
        }
        return players.get(0);
    }
}
