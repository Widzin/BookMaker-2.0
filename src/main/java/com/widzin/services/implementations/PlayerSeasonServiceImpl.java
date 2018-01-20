package com.widzin.services.implementations;

import com.widzin.models.Match;
import com.widzin.models.MatchEvent;
import com.widzin.models.PlayerSeason;
import com.widzin.models.TeamMatchDetails;
import com.widzin.repositories.PlayerSeasonRepository;
import com.widzin.services.PlayerSeasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlayerSeasonServiceImpl implements PlayerSeasonService {

    private PlayerSeasonRepository playerSeasonRepository;

    @Autowired
    public void setPlayerSeasonRepository(PlayerSeasonRepository playerSeasonRepository) {
        this.playerSeasonRepository = playerSeasonRepository;
    }

    @Override
    public Iterable<PlayerSeason> listAllPlayerSeasons() {
        return playerSeasonRepository.findAll();
    }

    @Override
    public PlayerSeason getPlayerSeasonById(Integer id) {
        return playerSeasonRepository.findOne(id);
    }

    @Override
    public PlayerSeason savePlayerSeason(PlayerSeason playerSeason) {
        return playerSeasonRepository.save(playerSeason);
    }

    @Override
    public void setPositionOfPlayerSeasonId(Integer id, final TeamMatchDetails teamMatchDetails) {
        if (getPlayerSeasonById(id).getPosition().equals("RB")
                || getPlayerSeasonById(id).getPosition().equals("CB")
                || getPlayerSeasonById(id).getPosition().equals("LB"))
            teamMatchDetails.getLineupDefense().add(getPlayerSeasonById(id));
        else if (getPlayerSeasonById(id).getPosition().equals("CDM")
                || getPlayerSeasonById(id).getPosition().equals("RM")
                || getPlayerSeasonById(id).getPosition().equals("CM")
                || getPlayerSeasonById(id).getPosition().equals("LM")
                || getPlayerSeasonById(id).getPosition().equals("CAM"))
            teamMatchDetails.getLineupMidfield().add(getPlayerSeasonById(id));
        else
            teamMatchDetails.getLineupForward().add(getPlayerSeasonById(id));
    }

    @Override
    public void updatePlayersAfterMatch(Match match, int homeScore, int awayScore) {
        List<PlayerSeason> playerSeasons = new ArrayList<>();
        playerSeasons.add(match.getHome().getLineupGoalkeeper());
        playerSeasons.add(match.getAway().getLineupGoalkeeper());
        playerSeasons.addAll(match.getHome().getLineupDefense());
        playerSeasons.addAll(match.getAway().getLineupDefense());
        playerSeasons.addAll(match.getHome().getLineupMidfield());
        playerSeasons.addAll(match.getAway().getLineupMidfield());
        playerSeasons.addAll(match.getHome().getLineupForward());
        playerSeasons.addAll(match.getAway().getLineupForward());

        for (PlayerSeason pl: playerSeasons) {
            pl.addMatches();
            savePlayerSeason(pl);
        }

        if (homeScore == 0) {
            match.getAway().getLineupGoalkeeper().addCleanSheets();
            savePlayerSeason(match.getAway().getLineupGoalkeeper());
        }
        if (awayScore == 0) {
            match.getHome().getLineupGoalkeeper().addCleanSheets();
            savePlayerSeason(match.getHome().getLineupGoalkeeper());
        }

        for (MatchEvent me: match.getHome().getGoalDetails()) {
            me.getPlayerSeason().addGoals();
            savePlayerSeason(me.getPlayerSeason());
        }

        for (MatchEvent me: match.getAway().getGoalDetails()) {
            me.getPlayerSeason().addGoals();
            savePlayerSeason(me.getPlayerSeason());
        }
    }

    @Override
    public void deletePlayerSeason(Integer id) {
        playerSeasonRepository.delete(id);
    }
}
