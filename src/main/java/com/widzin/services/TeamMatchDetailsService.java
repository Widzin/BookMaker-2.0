package com.widzin.services;

import com.widzin.models.TeamMatchDetails;

public interface TeamMatchDetailsService {
    Iterable<TeamMatchDetails> listAllTeamMatchDetails();
    TeamMatchDetails getTeamMatchDetailsById(Integer id);
    TeamMatchDetails saveTeamMatchDetails(TeamMatchDetails teamMatchDetails);
    void deleteTeamMatchDetails(Integer id);
}
