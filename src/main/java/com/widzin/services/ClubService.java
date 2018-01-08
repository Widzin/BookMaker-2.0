package com.widzin.services;

import com.widzin.model.Club;
import com.widzin.model.Game;

import java.util.List;

public interface ClubService {
	Iterable<Club> listAllClubs();
	Club getClubById(Integer id);
	Club saveClub(Club club);
	void deleteClub(Integer id);
	Iterable<Game> getLastFiveMatches(Club club);
	Iterable<Game> getAllGames(Club club);
	List<Game> getAllPlayedGames(Club club);
	Iterable<Club> getForTableThisSeason();
	List<Club> getListOfCurrentClubs();
	Iterable<Club> getForTableAllSeasons();
	List<Club> sortListForTable(List<Club> list);
	Iterable<Game> getNextMatchesForClub (Club club);
}
