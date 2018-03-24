package com.example.zach.smashmyandroid.database;

import com.example.zach.smashmyandroid.models.Player;
import io.reactivex.Flowable;
import java.util.List;

/**
 * Created by Zach on 3/21/2018.
 */

public class PlayerRepository implements IPlayerDataSource {

    private IPlayerDataSource mLocalDataSource;

    private static PlayerRepository INSTANCE;

    public PlayerRepository(IPlayerDataSource mLocalDataSource) {
        this.mLocalDataSource = mLocalDataSource;
    }

    public static PlayerRepository getInstance(IPlayerDataSource mLocalDataSource ) {
        if(INSTANCE == null) {
            INSTANCE = new PlayerRepository(mLocalDataSource);
        }
        return INSTANCE;
    }

    @Override
    public Flowable<List<Player>> loadAllUsers() {
        return mLocalDataSource.loadAllUsers();
    }

    @Override
    public Flowable<Player> loadUserById(int id) {
        return mLocalDataSource.loadUserById(id);
    }

    @Override
    public Flowable<List<Player>> findByFirstAndLastName(String firstName, String lastName) {
        return mLocalDataSource.findByFirstAndLastName(firstName, lastName);
    }

    @Override
    public Flowable<Player> loadUserBySmashName(String smashName) {
        return mLocalDataSource.loadUserBySmashName(smashName);
    }

    @Override
    public Flowable<List<Player>> findRankGreaterThan(int rank) {
        return mLocalDataSource.findRankGreaterThan(rank);
    }

    @Override
    public Flowable<List<Player>> findRankLessThan(int rank) {
        return mLocalDataSource.findRankLessThan(rank);
    }

    @Override
    public void insertUser(Player player) {
        mLocalDataSource.insertUser(player);
    }

    @Override
    public void deleteUser(Player player) {
        mLocalDataSource.deleteUser(player);
    }

    @Override
    public void clearTable() { mLocalDataSource.clearTable(); }

    @Override
    public void deleteUser(int id) { mLocalDataSource.deleteUser(id); }

    @Override
    public void deleteUsersByName(String badName) {
        mLocalDataSource.deleteUsersByName(badName);
    }

    @Override
    public void insertOrReplaceUsers(Player... players) {
        mLocalDataSource.insertOrReplaceUsers(players);
    }

    @Override
    public int updatePlayer(Player player) { return mLocalDataSource.updatePlayer(player); }
}
