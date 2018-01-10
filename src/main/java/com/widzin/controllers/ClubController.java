package com.widzin.controllers;

import com.widzin.models.Club;
import com.widzin.services.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ClubController {

	private ClubService clubService;

	@Autowired
	public void setClubService (ClubService clubService) {
		this.clubService = clubService;
	}

	/*@RequestMapping(value = "/clubs", method = RequestMethod.GET)
	public String list(Model models){
		models.addAttribute("clubs", clubService.listAllClubs());
		return "clubs";
	}*/

	/*@RequestMapping("/club/show/{id}")
	public String showClub(@PathVariable Integer id, Model models) {
		models.addAttribute("club", clubService.getClubById(id));
		models.addAttribute("lastGames", clubService.getLastFiveMatches(clubService.getClubById(id)));
		models.addAttribute("nextGames", clubService.getNextMatchesForClub(clubService.getClubById(id)));
		return "clubshow";
	}*/

	/*@RequestMapping("/club/edit/{id}")
	public String edit(@PathVariable Integer id, Model model) {
		model.addAttribute("club", clubService.getClubById(id));
		return "clubform";
	}*/

	/*@RequestMapping("/club/new")
	public String newClub(Model model) {
		model.addAttribute("club", new Club());
		return "clubform";
	}*/

	/*@RequestMapping(value = "/club", method = RequestMethod.POST)
	public String saveClub(Club club) {
		clubService.saveClub(club);
		return "redirect:/club/show/" + club.getId();
	}*/

    /*@RequestMapping("/club/delete/{id}")
    public String delete(@PathVariable Integer id) {
        clubService.deleteClub(id);
        return "redirect:/clubs";
    }*/

	/*@RequestMapping("/table")
	public String showTable(Model model){
		model.addAttribute("clubs", clubService.getForTableThisSeason());
		return "table";
	}*/

	/*@RequestMapping(value = "/club/{id}/history", method = RequestMethod.POST)
	public String showHistory(@PathVariable Integer id, Model model){
		model.addAttribute("club", clubService.getClubById(id));
		model.addAttribute("allPastGames", clubService.getAllGames(clubService.getClubById(id)));
		return "history";
	}*/
}
