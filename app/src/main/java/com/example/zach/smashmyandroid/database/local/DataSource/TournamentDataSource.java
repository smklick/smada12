package com.example.zach.smashmyandroid.database.local.DataSource;

import com.example.zach.smashmyandroid.database.local.Interface.ITournamentDataSource;
import com.example.zach.smashmyandroid.database.local.dao.TournamentDao;
import com.example.zach.smashmyandroid.database.local.models.Tournament;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by zcuts on 4/2/2018.
 */

public class TournamentDataSource implements ITournamentDataSource {

    private TournamentDao tournamentDao;
    private static TournamentDataSource INSTANCE;

    public TournamentDataSource(TournamentDao tournamentDao) { this.tournamentDao = tournamentDao; }

    public static TournamentDataSource getInstance(TournamentDao tournamentDao) {
        if(INSTANCE == null) {
            INSTANCE = new TournamentDataSource(tournamentDao);
        }
        return INSTANCE;
    }

    @Override
    public Flowable<List<Tournament>> getAllTournaments() {
        return tournamentDao.getAllTournaments();
    }

    @Override
    public Flowable<List<Tournament>> getTournament(int id) {
        return tournamentDao.getTournament(id);
    }

    @Override
    public void clearTable() { tournamentDao.clearTable(); }

    @Override
    public void insert(Tournament... tournament) {
        tournamentDao.insert(tournament);
    }

    @Override
    public void update(Tournament... tournament) {
        tournamentDao.update(tournament);
    }

    @Override
    public void delete(Tournament... tournament) {
        tournamentDao.delete(tournament);
    }
}
