package com.example.zach.smashmyandroid.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.example.zach.smashmyandroid.models.Player;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by Zach on 3/21/2018.
 */
@Entity(tableName = "Matches",

        foreignKeys = {

        @ForeignKey(entity = Player.class,
                parentColumns = "id",
                childColumns = "winnerId",
                onDelete = CASCADE),

        @ForeignKey(entity = Player.class,
                parentColumns = "id",
                childColumns = "loserId",
                onDelete = CASCADE)
},
        indices = {@Index("id"), @Index("winnerId"), @Index("loserId")})

public class Match {

    @PrimaryKey private final int id;
    private int winnerId;
    private int loserId;

    public Match(final int id, int winnerId, int loserId) {
        this.id = id;
        this.winnerId = winnerId;
        this.loserId = loserId;
    }

    public int getId() {
        return id;
    }

    public int getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(int winnerId) {
        this.winnerId = winnerId;
    }

    public int getLoserId() {
        return loserId;
    }

    public void setLoserId(int loserId) {
        this.loserId = loserId;
    }
}
