package com.example.zach.smashmyandroid;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Entity;

import android.arch.*;
/**
 * Created by Zach on 3/19/2018.
 */

    @Entity
    public class User {
        private @PrimaryKey String id;
        private String firstName;
        private String lastName;
        private String smashName;
        private double assFactor;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSmashName() {
        return smashName;
    }

    public void setSmashName(String smashName) {
        this.smashName = smashName;
    }

    public double getAssFactor() {
        return assFactor;
    }

    public void setAssFactor(double assFactor) {
        this.assFactor = assFactor;
    }
}


