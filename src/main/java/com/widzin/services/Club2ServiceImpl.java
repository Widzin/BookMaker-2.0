package com.widzin.services;

import com.widzin.model.Club2;
import com.widzin.repositories.Club2Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Club2ServiceImpl implements  Club2Service{

    private Club2Repository club2Repository;

    @Autowired
    public void setClubRepository (Club2Repository club2Repository) {
        this.club2Repository = club2Repository;
    }

    @Override
    public Iterable<Club2> listAllClubs2() {
        return club2Repository.findAll();
    }

    @Override
    public Club2 getClub2ById(Integer id) {
        return club2Repository.findOne(id);
    }

    @Override
    public Club2 saveClub2(Club2 club2) {
        return club2Repository.save(club2);
    }

    @Override
    public void deleteClub2(Integer id) {
        club2Repository.delete(id);
    }
}
