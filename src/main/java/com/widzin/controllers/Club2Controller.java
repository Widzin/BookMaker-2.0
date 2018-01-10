package com.widzin.controllers;

import com.widzin.models.Club2;
import com.widzin.services.Club2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class Club2Controller {

    private Club2Service club2Service;

    @Autowired
    public void setClub2Service(Club2Service club2Service) {
        this.club2Service = club2Service;
    }

    @RequestMapping(value = "/clubs", method = RequestMethod.GET)
    public String list(Model model){
        model.addAttribute("clubs", club2Service.listAllClubs2());
        return "clubs";
    }

    @RequestMapping("/club/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        model.addAttribute("club", club2Service.getClub2ById(id));
        return "clubform";
    }

    @RequestMapping(value = "/club", method = RequestMethod.POST)
    public String saveClub(Club2 club2) {
        club2Service.saveClub2(club2);
        return "redirect:/club/show/" + club2.getId();
    }
}
