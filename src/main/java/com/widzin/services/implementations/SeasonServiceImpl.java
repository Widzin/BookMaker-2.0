package com.widzin.services.implementations;

import com.widzin.model.Season;
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
    public void deleteSeason(Integer id) {
        seasonRepository.delete(id);
    }
}
