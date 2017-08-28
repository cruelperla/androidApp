package com.rza.firebaseloginpractice.model;

import android.support.annotation.NonNull;

import java.util.Comparator;

/**
 * Created by Rza on 24-Aug-17.
 */

public class Employee {
    private String name;
    private String email;
    private String position;
    private String date;
    private String imgUri;
    private String id;
    private String officeId;
    private Office office;

    public Employee() {

    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Employee{");
        sb.append("name='").append(name).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", position='").append(position).append('\'');
        sb.append(", date='").append(date).append('\'');
        sb.append(", imgUri='").append(imgUri).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
