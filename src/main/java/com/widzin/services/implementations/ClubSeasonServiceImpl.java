package com.widzin.services.implementations;

import com.google.common.collect.Lists;
import com.widzin.models.ClubSeason;
import com.widzin.models.Match;
import com.widzin.models.PlayerSeason;
import com.widzin.repositories.ClubRepository;
import com.widzin.repositories.ClubSeasonRepository;
import com.widzin.services.ClubSeasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ClubSeasonServiceImpl implements ClubSeasonService {

    private ClubSeasonRepository clubSeasonRepository;
    private ClubRepository clubRepository;

    @Autowired
    public void setClubSeasonRepository(ClubSeasonRepository clubSeasonRepository) {
        this.clubSeasonRepository = clubSeasonRepository;
    }

    @Autowired
    public void setClubRepository(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    @Override
    public Iterable<ClubSeason> listAllClubsSeason() {
        return clubSeasonRepository.findAll();
    }

    @Override
    public ClubSeason getClubSeasonById(Integer id) {
        return clubSeasonRepository.findOne(id);
    }

    @Override
    public List<ClubSeason> getClubSeasonsByClubId(Integer id) {
        Iterable<ClubSeason> allClubSeasons = listAllClubsSeason();
        List<ClubSeason> clubSeasons = new ArrayList<>();

        for (ClubSeason clubSeason: allClubSeasons) {
            if (clubSeason.getClub().getId() == id)
                clubSeasons.add(clubSeason);
        }

        return clubSeasons;
    }

    @Override
    public ClubSeason getLastClubSeason(Integer id) {
        List<ClubSeason> clubSeasons = getClubSeasonsByClubId(id);
        if (clubSeasons.size() == 0)
            return new ClubSeason(clubRepository.findOne(id));

        Integer maxId = 0;
        for (ClubSeason clubSeason: clubSeasons) {
            if (maxId < clubSeason.getId())
                maxId = clubSeason.getId();
        }
        return getClubSeasonById(maxId);
    }

    @Override
    public ClubSeason saveClubSeason(ClubSeason clubSeason) {
        return clubSeasonRepository.save(clubSeason);
    }

    @Override
    public void deleteClubSeason(Integer id) {
        clubSeasonRepository.delete(id);
    }

    @Override
    public List<ClubSeason> sortListForTable (List<ClubSeason> list) {
        Comparator<ClubSeason> c = (p, o) -> (-1)*p.getPoints().compareTo(o.getPoints());
        c = c.thenComparing((p, o) -> (-1)*p.getBilans().compareTo(o.getBilans()));
        c = c.thenComparing((p, o) -> (-1)*p.getScoredGoals().compareTo(o.getScoredGoals()));
        c = c.thenComparing((p, o) -> (-1)*p.getWins().compareTo(o.getWins()));

        list.sort(c);
        return list;
    }

    @Override
    public List<PlayerSeason> getPlayersFromLine(Integer clubSeasonId, String line) {
        List<PlayerSeason> playerSeasons = new ArrayList<>();
        for (PlayerSeason pl: getClubSeasonById(clubSeasonId).getPlayers()) {
            if (pl.getPosition().equals(line))
                playerSeasons.add(pl);
        }
        return playerSeasons;
    }

    @Override
    public PlayerSeason getPlayerSeasonFromClubSeason(Integer clubSeasonId, Integer playerSeasonId) {
        for (PlayerSeason pl: getClubSeasonById(clubSeasonId).getPlayers()) {
            Integer id = pl.getId();
            if (id.equals(playerSeasonId))
                return pl;
        }
        return null;
    }

    @Override
    public List<PlayerSeason> getNotPickedPlayersForMatch(Match match, Integer clubSeasonId) {
        List<PlayerSeason> pickedPlayers = new ArrayList<>();
        List<PlayerSeason> notPickedPlayers = new ArrayList<>();
        if (match.getHome().getClubSeason().getId().equals(clubSeasonId)) {
            pickedPlayers.add(match.getHome().getLineupGoalkeeper());
            pickedPlayers.addAll(match.getHome().getLineupDefense());
            pickedPlayers.addAll(match.getHome().getLineupMidfield());
            pickedPlayers.addAll(match.getHome().getLineupForward());
        } else {
            pickedPlayers.add(match.getAway().getLineupGoalkeeper());
            pickedPlayers.addAll(match.getAway().getLineupDefense());
            pickedPlayers.addAll(match.getAway().getLineupMidfield());
            pickedPlayers.addAll(match.getAway().getLineupForward());
        }

        for (PlayerSeason pl: getClubSeasonById(clubSeasonId).getPlayers()) {
            if (!pickedPlayers.contains(pl))
                notPickedPlayers.add(pl);
        }

        return notPickedPlayers;
    }
}
