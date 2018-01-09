package com.widzin.model;

import javax.persistence.*;

@Entity
public class Club2 {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "club2_id")
    private Integer id;

    @Version
    private Integer version;

    private String name;
    private String imgUrl;
    private boolean bundesliga;

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
