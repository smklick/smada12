package com.example.zach.smashmyandroid.database.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.zach.smashmyandroid.database.local.models.Match;

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

    @Query("select * from Matches where tournamentId =:id")
    Flowable<List<Match>> getMatchesByTournament(int id);

    @Query("select * from Matches where tournamentId =:tournamentId and (winnerId =:playerId or loserId =:playerId)")
    Flowable<List<Match>> getPlayerMatchesByTournament(int tournamentId, int playerId);

    @Insert
    void insert(Match... match);

    @Update
    void update(Match... match);

    @Delete
    void delete(Match... match);

}
