package com.widzin.services;

import com.widzin.domain.Game;
import com.widzin.repositories.ClubRepository;
import com.widzin.domain.Club;
import com.widzin.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		List<Game> allGames = clubRepository.findOne(club.getId()).getGamesAtHome();
		allGames.addAll(clubRepository.findOne(club.getId()).getGamesAway());
		Comparator<Game> byDate = (p, o) -> (-1)*p.getDate().compareTo(o.getDate());
		allGames.sort(byDate);
		List<Game> lastFiveList = allGames.subList(0, 5);
		Iterable<Game> lastFive = lastFiveList;
		return lastFive;
	}
}
