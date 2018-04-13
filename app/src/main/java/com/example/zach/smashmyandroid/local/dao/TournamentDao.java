package com.example.zach.smashmyandroid.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.zach.smashmyandroid.local.models.Tournament;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by zcuts on 4/2/2018.
 */

@Dao
public interface TournamentDao {

    @Query("select * from Tournaments")
    Flowable<List<Tournament>> getAllTournaments();

    @Query("select * from Tournaments where id =:id")
    Flowable<List<Tournament>> getTournament(int id);

    @Query("delete from Tournaments")
    public void clearTable();

    @Insert
    void insert(Tournament... tournament);

    @Update
    void update(Tournament... tournament);

    @Delete
    void delete(Tournament... tournament);

}
