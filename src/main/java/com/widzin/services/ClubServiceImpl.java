package com.widzin.services;

import com.widzin.domain.Game;
import com.widzin.repositories.ClubRepository;
import com.widzin.domain.Club;
import com.widzin.repositories.GameRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ClubServiceImpl implements ClubService{
	private ClubRepository clubRepository;

	@Autowired
	public void setClubRepository (ClubRepository clubRepository) {
		this.clubRepository = clubRepository;
	}

	@Override
	public Iterable<Club> listAllClubs () {
		return clubRepository.findAll();
	}

	@Override
	public Club getClubById (Integer id) {
		return clubRepository.findOne(id);
	}

	@Override
	public Club saveClub (Club club) {
		return clubRepository.save(club);
	}

	@Override
	public void deleteClub (Integer id) {
		clubRepository.delete(id);
	}

	@Override
	public Iterable<Game> getLastFiveMatches (Club club) {
		List<Game> lastFiveList = getAllPlayedGames(club).subList(0, 5);
		Iterable<Game> lastFive = lastFiveList;
		return lastFive;
	}

	@Override
	public List<Game> getAllGames (Club club) {
		List<Game> allGames = clubRepository.findOne(club.getId()).getGamesAtHome();
		allGames.addAll(clubRepository.findOne(club.getId()).getGamesAway());
		Comparator<Game> byDate = (p, o) -> (-1)*p.getDate().compareTo(o.getDate());
		allGames.sort(byDate);
		return allGames;
	}

	@Override
	public List<Game> getAllPlayedGames (Club club) {
		List<Game> allGames = clubRepository.findOne(club.getId()).getGamesAtHome();
		allGames.addAll(clubRepository.findOne(club.getId()).getGamesAway());
		List<Game> list = new ArrayList<>();
		for (Game g: allGames){
			if (g.isPlayed())
				list.add(g);
		}
		Comparator<Game> byDate = (p, o) -> (-1)*p.getDate().compareTo(o.getDate());
		list.sort(byDate);
		return list;
	}

	@Override
	public Iterable<Club> getForTableThisSeason () {
		Iterable<Club> newIterable = sortListForTable(getListOfCurrentClubs());
		return newIterable;
	}

	@Override
	public List<Club> getListOfCurrentClubs () {
		Iterable<Club> iterable = clubRepository.findAll();
		List<Club> list = new ArrayList<>();

		for (Club club: iterable) {
			if (club.isBundesliga())
				list.add(club);
		}
		return list;
	}

	@Override
	public Iterable<Club> getForTableAllSeasons () {
		Iterable<Club> iterable = clubRepository.findAll();
		List<Club> list = new ArrayList<>();
		for (Club club: iterable) {
			list.add(club);
		}
		list = sortListForTable(list);
		Iterable<Club> newIterable = list;
		return newIterable;
	}

	@Override
	public List<Club> sortListForTable (List<Club> list) {
		Comparator<Club> c = (p, o) -> (-1)*p.getPoints().compareTo(o.getPoints());
		c = c.thenComparing((p, o) -> (-1)*p.getBilans().compareTo(o.getBilans()));
		c = c.thenComparing((p, o) -> (-1)*p.getScoredGoals().compareTo(o.getScoredGoals()));
		c = c.thenComparing((p, o) -> (-1)*p.getWins().compareTo(o.getWins()));

		list.sort(c);
		return list;
	}

	private Logger log = Logger.getLogger(ClubServiceImpl.class);

	@Override
	public Iterable<Game> getNextMatches (Club club) {
		List<Game> allGames = clubRepository.findOne(club.getId()).getGamesAtHome();
		allGames.addAll(clubRepository.findOne(club.getId()).getGamesAway());
		List<Game> nextGames = new ArrayList<>();
		for (Game g: allGames){
			if (!g.isPlayed()) {
				nextGames.add(g);
			}
		}
		Comparator<Game> byDate = (p, o) -> p.getDate().compareTo(o.getDate());
		nextGames.sort(byDate);
		Iterable<Game> newIterable;
		if (nextGames.size() > 3)
			newIterable = nextGames.subList(0, 3);
		else
			newIterable = nextGames;
		log.info("Rozmiar tej tablicy: " + nextGames.size());
		return newIterable;
	}
}
