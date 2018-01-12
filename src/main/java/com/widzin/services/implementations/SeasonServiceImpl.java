package com.widzin.services.implementations;

import com.widzin.models.Season;
import com.widzin.repositories.SeasonRepository;
import com.widzin.services.SeasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeasonServiceImpl implements SeasonService{

    private SeasonRepository seasonRepository;

    @Autowired
    public void setSeasonRepository(SeasonRepository seasonRepository) {
        this.seasonRepository = seasonRepository;
    }

    @Override
    public Iterable<Season> listAllSeasons() {
        return seasonRepository.findAll();
    }

    @Override
    public Season getSeasonById(Integer id) {
        return seasonRepository.findOne(id);
    }

    @Override
    public Season saveSeason(Season season) {
        return seasonRepository.save(season);
    }

    @Override
    public Season getLastSeason() {
        Iterable<Season> seasonList = listAllSeasons();
        Integer maxId = 0;

        for (Season season: seasonList) {
            if (maxId < season.getId())
                maxId = season.getId();
        }

        return getSeasonById(maxId);
    }

    @Override
    public void deleteSeason(Integer id) {
        seasonRepository.delete(id);
    }
}
