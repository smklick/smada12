package com.example.zach.smashmyandroid.local.models;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Entity;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;


/**
 * Created by Zach on 3/19/2018.
 */

    @Entity(indices = {@Index("id")}, tableName="Players")
    public class Player implements Parcelable {

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

    @ColumnInfo(name="rank")
    private int rank;


    public Player(String firstName, String lastName, String smashName, int rank) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.smashName = smashName;
        this.rank = rank;
    }

    public Player(Parcel p) {
        this.id = p.readInt();
        this.firstName = p.readString();
        this.lastName = p.readString();
        this.smashName = p.readString();
        this.rank = p.readInt();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        public Player createFromParcel(Parcel p){
            return new Player(p);
        }
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.smashName);
        dest.writeInt(this.rank);
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

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String toString() {
        String s = smashName;
        return s;
    }
}


