package com.widzin.services;

import com.widzin.domain.Club;

public interface ClubService {
	Iterable<Club> listAllClubs();
	Club getClubById(Integer id);
	Club saveClub(Club club);
	void deleteClub(Integer id);
}
