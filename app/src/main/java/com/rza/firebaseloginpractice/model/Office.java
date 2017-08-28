package com.rza.firebaseloginpractice.model;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Rza on 26-Aug-17.
 */

public class Office implements Serializable {

    private String id;
    private String name;
    private String imgUrl;
    private String lat;
    private String lng;


    public Office() {

    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
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
