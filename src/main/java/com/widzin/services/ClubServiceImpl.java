package com.widzin.services;

import com.widzin.repositories.ClubRepository;
import com.widzin.domain.Club;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
