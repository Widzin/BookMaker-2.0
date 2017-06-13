package com.widzin.controllers;

import com.widzin.domain.Club;
import com.widzin.domain.Game;
import com.widzin.services.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
public class ClubController {

	private ClubService clubService;

	@Autowired
	public void setClubService (ClubService clubService) {
		this.clubService = clubService;
	}

	@RequestMapping(value = "/clubs", method = RequestMethod.GET)
	public String list(Model model){
		model.addAttribute("clubs", doNotSort(clubService));
		return "clubs";
	}

	@RequestMapping("/club/show/{id}")
	public String showClub(@PathVariable Integer id, Model model) {
		model.addAttribute("club", clubService.getClubById(id));
		model.addAttribute("games", clubService.getLastFiveMatches(clubService.getClubById(id)));
		return "clubshow";
	}

	@RequestMapping("/club/edit/{id}")
	public String edit(@PathVariable Integer id, Model model) {
		model.addAttribute("club", clubService.getClubById(id));
		return "clubform";
	}

	@RequestMapping("/club/new")
	public String newClub(Model model) {
		model.addAttribute("club", new Club());
		return "clubform";
	}

	@RequestMapping(value = "/club", method = RequestMethod.POST)
	public String saveClub(Club club) {
		clubService.saveClub(club);
		return "redirect:/club/show/" + club.getId();
	}

	@RequestMapping("/club/delete/{id}")
	public String delete(@PathVariable Integer id) {
		clubService.deleteClub(id);
		return "redirect:/clubs";
	}

	private Iterable<Club> doNotSort(ClubService clubService) {
		Iterable<Club> newIterable = getListOfCurrentClubs(clubService);
		return newIterable;
	}

	private Iterable<Club> sortForTable(ClubService clubService) {
		List<Club> list = getListOfCurrentClubs(clubService);

		Comparator<Club> c = (p, o) -> (-1)*p.getPoints().compareTo(o.getPoints());
		c = c.thenComparing((p, o) -> (-1)*p.getBilans().compareTo(o.getBilans()));
		c = c.thenComparing((p, o) -> (-1)*p.getScoredGoals().compareTo(o.getScoredGoals()));
		c = c.thenComparing((p, o) -> (-1)*p.getWins().compareTo(o.getWins()));

		list.sort(c);
		Iterable<Club> newIterable = list;
		return newIterable;
	}

	private List<Club> getListOfCurrentClubs(ClubService clubService){
		Iterable<Club> iterable = clubService.listAllClubs();
		List<Club> list = new ArrayList<>();

		for (Club club: iterable) {
			if (club.isBundesliga())
				list.add(club);
		}
		return list;
	}
}
