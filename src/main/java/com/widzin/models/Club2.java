package com.widzin.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class Club2 {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Version
    private Integer version;

    private String name;
    private String imgUrl;

    @OneToMany(mappedBy = "club2", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ClubSeason> clubSeasonList;

    public Club2() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Club2(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public List<ClubSeason> getClubSeasonList() {
        return clubSeasonList;
    }

    public void setClubSeasonList(List<ClubSeason> clubSeasonList) {
        this.clubSeasonList = clubSeasonList;
    }

    @Override
    public String toString() {
        return "Club2{" +
                "name='" + name + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}