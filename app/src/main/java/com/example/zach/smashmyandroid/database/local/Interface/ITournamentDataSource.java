package com.example.zach.smashmyandroid.database.local.Interface;

import com.example.zach.smashmyandroid.database.local.models.Tournament;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by zcuts on 4/2/2018.
 */

public interface ITournamentDataSource {

    Flowable<List<Tournament>> getAllTournaments();

    Flowable<List<Tournament>> getTournament(int id);

    void clearTable();

    void insert(Tournament... tournament);

    void update(Tournament... tournament);

    void delete(Tournament... tournament);
}
