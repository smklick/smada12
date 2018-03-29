package com.example.zach.smashmyandroid.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.zach.smashmyandroid.models.Player;

import io.reactivex.annotations.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by Zach on 3/21/2018.
 */
@Entity(tableName = "Matches",

        foreignKeys = {

        @ForeignKey(entity = Tournament.class,
                    parentColumns = "id",
                    childColumns = "tournamentId",
                    onDelete = CASCADE),

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

public class Match implements Parcelable{

    @NonNull
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private final int id;

    @NonNull
    @ColumnInfo(name = "tournamentId")
    private int tournamentId;

    @ColumnInfo(name = "winnerId")
    private int winnerId;

    @ColumnInfo(name = "loserId")
    private int loserId;

    public Match(final int id, int winnerId, int loserId) {
        this.id = id;
        this.winnerId = winnerId;
        this.loserId = loserId;
    }

    protected Match(Parcel in) {
        id = in.readInt();
        winnerId = in.readInt();
        loserId = in.readInt();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        public Match createFromParcel(Parcel p){
            return new Match(p);
        }
        public Match[] newArray(int size) {
            return new Match[size];
        }
    };

    public int getId() {
        return id;
    }

    public int getTournamentId() { return tournamentId; };

    public void setTournamentId(int tournamentId) { this.tournamentId = tournamentId; };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(winnerId);
        dest.writeInt(loserId);
    }
}
