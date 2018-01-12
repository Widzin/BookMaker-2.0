package com.widzin.services.implementations;

import com.widzin.models.TeamMatchDetails;
import com.widzin.repositories.TeamMatchDetailsRepository;
import com.widzin.services.TeamMatchDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamMatchDetailsServiceImpl implements TeamMatchDetailsService{

    private TeamMatchDetailsRepository teamMatchDetailsRepository;

    @Autowired
    public void setTeamMatchDetailsRepository(TeamMatchDetailsRepository teamMatchDetailsRepository) {
        this.teamMatchDetailsRepository = teamMatchDetailsRepository;
    }

    @Override
    public Iterable<TeamMatchDetails> listAllTeamMatchDetails() {
        return teamMatchDetailsRepository.findAll();
    }

    @Override
    public TeamMatchDetails getTeamMatchDetailsById(Integer id) {
        return teamMatchDetailsRepository.findOne(id);
    }

    @Override
    public TeamMatchDetails saveTeamMatchDetails(TeamMatchDetails teamMatchDetails) {
        return teamMatchDetailsRepository.save(teamMatchDetails);
    }

    @Override
    public void deleteTeamMatchDetails(Integer id) {
        teamMatchDetailsRepository.delete(id);
    }
}
