package com.example.zach.smashmyandroid.local;

import com.example.zach.smashmyandroid.database.IPlayerDataSource;
import com.example.zach.smashmyandroid.models.Player;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Zach on 3/21/2018.
 */

public class PlayerDataSource implements IPlayerDataSource {

    private PlayerDao playerDao;
    private static PlayerDataSource INSTANCE;

    public PlayerDataSource(PlayerDao playerDao) {
        this.playerDao = playerDao;
    }

    public static PlayerDataSource getInstance(PlayerDao playerDao) {
        if(INSTANCE == null) {
            INSTANCE = new PlayerDataSource(playerDao);
        }
        return INSTANCE;
    }

    @Override
    public Flowable<List<Player>> loadAllUsers() {
        return playerDao.loadAllUsers();
    }

    @Override
    public Flowable<Player> loadUserById(int id) {
        return playerDao.loadUserById(id);
    }

    @Override
    public Flowable<List<Player>> findByFirstAndLastName(String firstName, String lastName) {
        return playerDao.findByFirstAndLastName(firstName, lastName);
    }

    @Override
    public Flowable<Player> loadUserBySmashName(String smashName) {
        return playerDao.loadUserBySmashName(smashName);
    }

    @Override
    public Flowable<List<Player>> findRankGreaterThan(int rank) {
        return playerDao.findRankGreaterThan(rank);
    }

    @Override
    public Flowable<List<Player>> findRankLessThan(int rank) {
        return playerDao.findRankLessThan(rank);
    }

    @Override
    public void insertUser(Player player) {
        playerDao.insertUser(player);
    }

    @Override
    public void deleteUser(Player player) {
        playerDao.deleteUser(player);
    }

    @Override
    public void deleteUsersByName(String badName) {
        playerDao.deleteUsersByName(badName);
    }

    @Override
    public void insertOrReplaceUsers(Player... players) {
        playerDao.insertOrReplaceUsers(players);
    }
}
