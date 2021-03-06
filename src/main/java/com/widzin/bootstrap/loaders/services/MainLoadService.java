package com.widzin.bootstrap.loaders.services;

import com.widzin.models.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MainLoadService {

    private List<Season> seasons;
    private List<Club> clubs;
    private List<Player> players;

    public MainLoadService() {
        seasons = new ArrayList<>();
        clubs = new ArrayList<>();
        players = new ArrayList<>();
    }

    public List<Season> getSeasons() {
        return seasons;
    }

    public void addSeason(Season season) {
        seasons.add(season);
    }

    public List<Club> getClubs() {
        return clubs;
    }

    public void addClub(Club club) {
        clubs.add(club);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public Player getPlayerFromService(String name, Date birthDate) {
        for (Player player: players) {
            if (player.getName().equalsIgnoreCase(name)
                    && player.getBirthDate().equals(birthDate))
                return player;
        }
        return null;
    }

    public Club getClubFromService(String clubName) {
        for (Club club: clubs) {
            if (club.getName().contains(clubName))
                return club;
        }
        return null;
    }

    public List<PlayerSeason> getPlayersFromClubAndSeason (String clubName, String period) {
        for (Season season: seasons) {
            if (season.getPeriod().equalsIgnoreCase(period)) {
                for (ClubSeason clubSeason: season.getClubs()) {
                    if (clubSeason.getClub().getName().contains(clubName))
                        return clubSeason.getPlayers();
                }
            }
        }
        return new ArrayList<>();
    }

    public List<PlayerSeason> getPlayersFromPlayersByName(List<PlayerSeason> searchedPlayers, String name) {
        List<PlayerSeason> playerSeasons = new ArrayList<>();

        for (PlayerSeason pls: searchedPlayers) {
            if (pls.getPlayer().getName().contains(name))
                playerSeasons.add(pls);
        }

        return playerSeasons;
    }

    public String getPeriodByMatchDate (Date matchDate) {
        Calendar cal = new GregorianCalendar(2016, 07, 01);
        Date date = cal.getTime();
        if (matchDate.before(date)) {
            return getStringSeasonByBeginning("2015");
        } else {
            cal.add(Calendar.YEAR, 1);
            date = cal.getTime();
            if (matchDate.before(date)) {
                return getStringSeasonByBeginning("2016");
            } else {
                return getStringSeasonByBeginning("2017");
            }
        }
    }

    private String getStringSeasonByBeginning (String periodStart) {
        for (Season s: seasons) {
            if (s.getPeriod().startsWith(periodStart))
                return s.getPeriod();
        }
        return null;
    }

    public Season getSeasonByPeriod (String period) {
        for (Season s: seasons) {
            if (s.getPeriod().equals(period))
                return s;
        }
        return null;
    }

    public ClubSeason getClubSeasonByPeriodAndName (String period, String clubName) {
        for (ClubSeason clubSeason: getSeasonByPeriod(period).getClubs()) {
            if (clubSeason.getClub().getName().contains(clubName))
                return clubSeason;
        }

        return null;
    }
}
