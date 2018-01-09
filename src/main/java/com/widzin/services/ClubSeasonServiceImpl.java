package com.widzin.services;

import com.widzin.model.ClubSeason;
import com.widzin.repositories.ClubSeasonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClubSeasonServiceImpl implements ClubSeasonService{

    private ClubSeasonRepository clubSeasonRepository;

    @Autowired
    public void setClubSeasonRepository(ClubSeasonRepository clubSeasonRepository) {
        this.clubSeasonRepository = clubSeasonRepository;
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
    public ClubSeason saveClubSeason(ClubSeason clubSeason) {
        return clubSeasonRepository.save(clubSeason);
    }

    @Override
    public void deleteClubSeason(Integer id) {
        clubSeasonRepository.delete(id);
    }
}
