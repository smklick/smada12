package com.example.zach.smashmyandroid.local.Repository;

import com.example.zach.smashmyandroid.local.Interface.IMatchDataSource;
import com.example.zach.smashmyandroid.local.models.Match;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by zcuts on 3/28/2018.
 */

public class MatchRepository implements IMatchDataSource {

    private IMatchDataSource mLocalDataSource;

    private static MatchRepository INSTANCE;

    public MatchRepository(IMatchDataSource mLocalDataSource) {
        this.mLocalDataSource = mLocalDataSource;
    }

    public static MatchRepository getInstance(IMatchDataSource mLocalDataSource) {
        if(INSTANCE == null) {
            INSTANCE = new MatchRepository(mLocalDataSource);
        }
        return INSTANCE;
    }
    @Override
    public Flowable<List<Match>> getMatchesByUser(int id) {
        return mLocalDataSource.getMatchesByUser(id);
    }

    @Override
    public Flowable<List<Match>> getAllMatches() {
        return getAllMatches();
    }

    @Override
    public Flowable<List<Match>> getMatch(int id) {
        return getMatchesByUser(id);
    }

    @Override
    public Flowable<List<Match>> getMatchesByTournament(int id) {
        return getMatchesByUser(id);
    }

    @Override
    public Flowable<List<Match>> getPlayerMatchesByTournament(int tournamentId, int playerId) {
        return getPlayerMatchesByTournament(tournamentId, playerId);
    }

    @Override
    public void insert(Match... match) {
        mLocalDataSource.insert(match);
    }

    @Override
    public void update(Match... match) {
        mLocalDataSource.update(match);
    }

    @Override
    public void delete(Match... match) {
        mLocalDataSource.delete(match);
    }
}
