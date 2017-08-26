package com.rza.firebaseloginpractice.model;

/**
 * Created by Rza on 26-Aug-17.
 */

public class Office {

    private String id;
    private String name;
    private String imgUrl;
    private String address;

    public Office() {

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
