package com.example.zach.smashmyandroid.database;

import com.example.zach.smashmyandroid.models.Match;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by zcuts on 3/28/2018.
 */

public interface IMatchDataSource {

    Flowable<List<Match>> getMatchesByUser(int id);

    Flowable<List<Match>> getAllMatches();

    Flowable<List<Match>> getMatch(int id);

    Flowable<List<Match>> getMatchesByTournament(int id);

    Flowable<List<Match>> getPlayerMatchesByTournament(int tournamentId, int playerId);

    void insert(Match... match);

    void update(Match... match);

    void delete(Match... match);


}
