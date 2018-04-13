package com.example.zach.smashmyandroid.local.Repository;

import com.example.zach.smashmyandroid.local.Interface.ITournamentDataSource;
import com.example.zach.smashmyandroid.local.models.Tournament;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by zcuts on 4/2/2018.
 */

public class TournamentRepository implements ITournamentDataSource {

    private ITournamentDataSource mLocalDataSource;

    private static TournamentRepository INSTANCE;

    public TournamentRepository(ITournamentDataSource mLocalDataSource) {
        this.mLocalDataSource = mLocalDataSource;
    }

    public static TournamentRepository getInstance(ITournamentDataSource mLocalDataSource) {
        if(INSTANCE == null) {
            INSTANCE = new TournamentRepository(mLocalDataSource);
        }
        return INSTANCE;
    }

    @Override
    public Flowable<List<Tournament>> getAllTournaments() {
        return mLocalDataSource.getAllTournaments();
    }

    @Override
    public Flowable<List<Tournament>> getTournament(int id) {
        return mLocalDataSource.getTournament(id);
    }

    @Override
    public void clearTable() { mLocalDataSource.clearTable(); }

    @Override
    public void insert(Tournament... tournament) {
        mLocalDataSource.insert(tournament);
    }

    @Override
    public void update(Tournament... tournament) {
        mLocalDataSource.update(tournament);
    }

    @Override
    public void delete(Tournament... tournament) {
        mLocalDataSource.delete(tournament);
    }
}
