package com.example.zach.smashmyandroid.models;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;


/**
 * Created by Zach on 3/19/2018.
 */

    @Entity(indices = {@Index("id")}, tableName="Players")
    public class Player {

    @NonNull
    @ColumnInfo(name="id")
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name="firstName")
    private String firstName;

    @ColumnInfo(name="lastName")
    private String lastName;

    @ColumnInfo(name="smashName")
    private String smashName;

    @ColumnInfo(name="assFactor")
    private int assFactor;


    public Player(String firstName, String lastName, String smashName, int assFactor) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.smashName = smashName;
        this.assFactor = assFactor;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getAssFactor() {
        return assFactor;
    }

    public void setAssFactor(int assFactor) {
        this.assFactor = assFactor;
    }

    public String toString() {
        String s = firstName + " \"" + smashName + "\" " + lastName;
        return s;
    }
}


