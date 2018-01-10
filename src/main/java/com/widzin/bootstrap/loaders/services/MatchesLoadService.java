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
                Game2 game2 = parseXmlToObject(xmlMatch, loadService);
                loadService.getSeasonByPeriod(period).addMatch(game2);
            }
        } catch (JAXBException ex) {
            ex.printStackTrace();
            System.out.println("Cannot create instance of XMLMatchesInSeason Class");
        }
    }

    public Game2 parseXmlToObject(XMLMatch xmlMatch, MainLoadService service) {
        Game2 game2 = new Game2();

        //------------- Setting game2 details ----------------
        game2.setDate(xmlMatch.getDate());
        game2.setRound(xmlMatch.getRound());
        game2.setPeriod(service.getPeriodByMatchDate(xmlMatch.getDate()));

        //------------- Setting home team details ------------
        game2.getHome().setClubSeason(
                service.getClubSeasonByPeriodAndName(game2.getPeriod(), xmlMatch.getHomeTeam()));
        game2.getHome().setGoals(xmlMatch.getHomeGoals());
        game2.getHome().setShots(xmlMatch.getHomeShots());
        game2.getHome().setShotsOnTarget(xmlMatch.getHomeShotsOnTarget());

        //------------- Setting away team details ------------
        game2.getAway().setClubSeason(
                service.getClubSeasonByPeriodAndName(game2.getPeriod(), xmlMatch.getAwayTeam()));
        game2.getAway().setGoals(xmlMatch.getAwayGoals());
        game2.getAway().setShots(xmlMatch.getAwayShots());
        game2.getAway().setShotsOnTarget(xmlMatch.getAwayShotsOnTarget());

        //------------- Setting home squad -------------------
        game2.getHome().setLineupGoalkeeper(getPlayersInArray(service, xmlMatch.getHomeLineupGoalkeeper(),
                game2.getHome().getClubSeason().getClub2().getName(), game2.getPeriod()).get(0));
        game2.getHome().setLineupDefense(getPlayersInArray(service, xmlMatch.getHomeLineupDefense(),
                game2.getHome().getClubSeason().getClub2().getName(), game2.getPeriod()));
        game2.getHome().setLineupMidfield(getPlayersInArray(service, xmlMatch.getHomeLineupMidfield(),
                game2.getHome().getClubSeason().getClub2().getName(), game2.getPeriod()));
        game2.getHome().setLineupForward(getPlayersInArray(service, xmlMatch.getHomeLineupForward(),
                game2.getHome().getClubSeason().getClub2().getName(), game2.getPeriod()));
        game2.getHome().setFormation(xmlMatch.getHomeTeamFormation());
        game2.getHome().setLineupSubstitutes(getPlayersInArray(service, xmlMatch.getHomeLineupSubstitutes(),
                game2.getHome().getClubSeason().getClub2().getName(), game2.getPeriod()));

        //------------- Setting away squad -------------------
        game2.getAway().setLineupGoalkeeper(getPlayersInArray(service, xmlMatch.getAwayLineupGoalkeeper(),
                game2.getAway().getClubSeason().getClub2().getName(), game2.getPeriod()).get(0));
        game2.getAway().setLineupDefense(getPlayersInArray(service, xmlMatch.getAwayLineupDefense(),
                game2.getAway().getClubSeason().getClub2().getName(), game2.getPeriod()));
        game2.getAway().setLineupMidfield(getPlayersInArray(service, xmlMatch.getAwayLineupMidfield(),
                game2.getAway().getClubSeason().getClub2().getName(), game2.getPeriod()));
        game2.getAway().setLineupForward(getPlayersInArray(service, xmlMatch.getAwayLineupForward(),
                game2.getAway().getClubSeason().getClub2().getName(), game2.getPeriod()));
        game2.getAway().setFormation(xmlMatch.getAwayTeamFormation());
        game2.getAway().setLineupSubstitutes(getPlayersInArray(service, xmlMatch.getAwayLineupSubstitutes(),
                game2.getAway().getClubSeason().getClub2().getName(), game2.getPeriod()));

        //-------------- Setting game2 events ----------------

        List<PlayerSeason> homePlayers = getPlayersFromOneTeam(game2.getHome());
        List<PlayerSeason> awayPlayers = getPlayersFromOneTeam(game2.getAway());

        game2.getHome().setGoalDetails(getEventsInArray(xmlMatch.getHomeGoalDetails(), Event.GOAL, service, homePlayers, awayPlayers));
        game2.getHome().setSubDetails(getEventsInArray(xmlMatch.getHomeSubDetails(), Event.SUBSTITUTION, service, homePlayers, awayPlayers));
        game2.getHome().setRedCardDetails(getEventsInArray(xmlMatch.getHomeTeamRedCardDetails(), Event.RED_CARD, service, homePlayers, awayPlayers));

        game2.getAway().setGoalDetails(getEventsInArray(xmlMatch.getAwayGoalDetails(), Event.GOAL, service, awayPlayers, homePlayers));
        game2.getAway().setSubDetails(getEventsInArray(xmlMatch.getAwaySubDetails(), Event.SUBSTITUTION, service, awayPlayers, homePlayers));
        game2.getAway().setRedCardDetails(getEventsInArray(xmlMatch.getAwayTeamRedCardDetails(), Event.RED_CARD, service, awayPlayers, homePlayers));

        //-------------- Adding stats to clubs ---------------

        if (game2.getHome().getGoals() > game2.getAway().getGoals()) {
            game2.getHome().getClubSeason().addWin();
            game2.getAway().getClubSeason().addLose();

            game2.getHome().getClubSeason().addPoints(POINTS_FOR_WIN);
            game2.getAway().getClubSeason().addPoints(POINTS_FOR_LOST);
        } else if (game2.getHome().getGoals() < game2.getAway().getGoals()) {
            game2.getHome().getClubSeason().addLose();
            game2.getAway().getClubSeason().addWin();

            game2.getHome().getClubSeason().addPoints(POINTS_FOR_LOST);
            game2.getAway().getClubSeason().addPoints(POINTS_FOR_WIN);
        } else {
            game2.getHome().getClubSeason().addDraw();
            game2.getAway().getClubSeason().addDraw();

            game2.getHome().getClubSeason().addPoints(POINTS_FOR_DRAW);
            game2.getAway().getClubSeason().addPoints(POINTS_FOR_DRAW);
        }

        game2.getHome().getClubSeason().addMatch();
        game2.getAway().getClubSeason().addMatch();

        game2.getHome().getClubSeason().addScoredGoals(game2.getHome().getGoals());
        game2.getAway().getClubSeason().addScoredGoals(game2.getAway().getGoals());

        game2.getHome().getClubSeason().addLostGoals(game2.getAway().getGoals());
        game2.getAway().getClubSeason().addLostGoals(game2.getHome().getGoals());

        game2.getHome().getClubSeason().setBilans();
        game2.getAway().getClubSeason().setBilans();

        return game2;
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
                    case RED_CARD:
                        PlayerSeason playerSeason = getPlayerFromPlayersByName(parts[1].trim(),
                                service, ourPlayers);
                        me = new MatchEvent(Integer.parseInt(parts[0].trim()),
                                playerSeason, Event.RED_CARD.getEvent());
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
        if (players.size() == 0 || players.size() > 1) {
            System.out.println("DUPA");
        }
        return players.get(0);
    }
}
