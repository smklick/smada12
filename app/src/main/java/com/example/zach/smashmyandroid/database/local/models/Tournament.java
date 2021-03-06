package com.example.zach.smashmyandroid.database.local.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by zcuts on 3/28/2018.
 */

@Entity(indices = {@Index("id")}, tableName="Tournaments")
public class Tournament implements Parcelable {

    @Ignore
    private static final int TOURNAMENT_ONGOING = 0;

    @Ignore
    private static final int TOURNAMENT_FINISHED = 1;

    @NonNull
    @ColumnInfo(name="id")
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name="name")
    private String name;

    public Tournament(String name) {
        this.name = name;
    }

    public Tournament(Parcel p) {
        this.id = p.readInt();
        this.name = p.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Tournament createFromParcel(Parcel p) {
            return new Tournament(p);
        }
        public Tournament[] newArray(int size) {
            return new Tournament[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}
