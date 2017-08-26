package com.rza.firebaseloginpractice.model;

import android.net.Uri;

/**
 * Created by Rza on 23-Aug-17.
 */

public class User {

    private String id;
    private String name;
    private Uri image;
    private String email;
    private String position;


    public User(){

    }

    public User(String id, String name, Uri image, String email, String position) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.email = email;
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
