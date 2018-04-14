package com.example.zach.smashmyandroid.database.local.DataSource;

import com.example.zach.smashmyandroid.database.local.Interface.IMatchDataSource;
import com.example.zach.smashmyandroid.database.local.dao.MatchDao;
import com.example.zach.smashmyandroid.database.local.models.Match;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by zcuts on 3/28/2018.
 */

public class MatchDataSource implements IMatchDataSource {

    private MatchDao matchDao;
    private static MatchDataSource INSTANCE;

    public MatchDataSource(MatchDao matchDao) { this.matchDao = matchDao; }

    public static MatchDataSource getInstance(MatchDao matchDao) {
        if(INSTANCE == null) {
            INSTANCE = new MatchDataSource(matchDao);
        }
        return INSTANCE;
    }

    @Override
    public Flowable<List<Match>> getMatchesByUser(int id) {
        return matchDao.getMatchesByUser(id);
    }

    @Override
    public Flowable<List<Match>> getAllMatches() {
        return matchDao.getAllMatches();
    }

    @Override
    public Flowable<List<Match>> getMatch(int id) {
        return matchDao.getMatch(id);
    }

    @Override
    public Flowable<List<Match>> getMatchesByTournament(int id) {
        return matchDao.getMatchesByTournament(id);
    }

    @Override
    public Flowable<List<Match>> getPlayerMatchesByTournament(int tournamentId, int playerId) {
        return getPlayerMatchesByTournament(tournamentId, playerId);
    }

    @Override
    public void insert(Match... match) {
        matchDao.insert(match);
    }

    @Override
    public void update(Match... match) {
        matchDao.update(match);
    }

    @Override
    public void delete(Match... match) {
        matchDao.delete(match);
    }
}
