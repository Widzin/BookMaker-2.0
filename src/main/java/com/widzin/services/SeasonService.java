package com.widzin.services;

import com.widzin.models.Season;

public interface SeasonService {
    Iterable<Season> listAllSeasons();
    Season getSeasonById(Integer id);
    Season saveSeason(Season season);
    Season getLastSeason();
    void deleteSeason(Integer id);
}
