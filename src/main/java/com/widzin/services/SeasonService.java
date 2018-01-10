package com.widzin.services;

import com.widzin.model.Season;

public interface SeasonService {
    Iterable<Season> listAllSeasons();
    Season getSeasonById(Integer id);
    Season saveSeason(Season season);
    void deleteSeason(Integer id);
}
