package com.widzin.model;

import javax.persistence.*;

@Entity
public class Club2 {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Version
    private Integer version;

    private String name;
    private String imgUrl;
    private boolean bundesliga;

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

    public boolean isBundesliga() {
        return bundesliga;
    }

    public void setBundesliga(boolean bundesliga) {
        this.bundesliga = bundesliga;
    }

    @Override
    public String toString() {
        return "Club2{" +
                "name='" + name + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", bundesliga=" + bundesliga +
                '}';
    }
}
