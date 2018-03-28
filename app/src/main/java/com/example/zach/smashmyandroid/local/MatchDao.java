package com.example.zach.smashmyandroid.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.zach.smashmyandroid.models.Match;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Zach on 3/21/2018.
 */

@Dao
public interface MatchDao {

    @Query("select * from Matches")
    Flowable<List<Match>> getAllMatches();

    @Query("select * from Matches where winnerId =:id or loserId =:id")
    Flowable<List<Match>> getMatchesByUser(int id);

    @Query("select * from Matches where id =:id")
    Flowable<List<Match>> getMatch(int id);

    @Insert
    void insert(Match... match);

    @Update
    void update(Match... match);

    @Delete
    void delete(Match... match);

}
