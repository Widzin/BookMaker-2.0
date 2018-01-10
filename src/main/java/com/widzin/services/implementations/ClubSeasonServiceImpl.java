package com.widzin.services.implementations;

import com.widzin.models.Club2;
import com.widzin.models.ClubSeason;
import com.widzin.repositories.Club2Repository;
import com.widzin.repositories.ClubSeasonRepository;
import com.widzin.services.ClubSeasonService;
import com.widzin.services.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ClubSeasonServiceImpl implements ClubSeasonService {

    private ClubSeasonRepository clubSeasonRepository;
    private Club2Repository club2Repository;

    @Autowired
    public void setClubSeasonRepository(ClubSeasonRepository clubSeasonRepository) {
        this.clubSeasonRepository = clubSeasonRepository;
    }

    @Autowired
    public void setClub2Repository(Club2Repository club2Repository) {
        this.club2Repository = club2Repository;
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
    public List<ClubSeason> getClubSeasonsByClub2Id(Integer id) {
        Iterable<ClubSeason> allClubSeasons = listAllClubsSeason();
        List<ClubSeason> clubSeasons = new ArrayList<>();

        for (ClubSeason clubSeason: allClubSeasons) {
            if (clubSeason.getClub2().getId() == id)
                clubSeasons.add(clubSeason);
        }

        return clubSeasons;
    }

    @Override
    public ClubSeason getLastClubSeason(Integer id) {
        List<ClubSeason> clubSeasons = getClubSeasonsByClub2Id(id);
        if (clubSeasons.size() == 0)
            return new ClubSeason(club2Repository.findOne(id));

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

    public List<ClubSeason> sortListForTable (List<ClubSeason> list) {
        Comparator<ClubSeason> c = (p, o) -> (-1)*p.getPoints().compareTo(o.getPoints());
        c = c.thenComparing((p, o) -> (-1)*p.getBilans().compareTo(o.getBilans()));
        c = c.thenComparing((p, o) -> (-1)*p.getScoredGoals().compareTo(o.getScoredGoals()));
        c = c.thenComparing((p, o) -> (-1)*p.getWins().compareTo(o.getWins()));

        list.sort(c);
        return list;
    }
}
