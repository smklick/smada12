package com.example.zach.smashmyandroid.database;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.zach.smashmyandroid.models.Player;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Zach on 3/21/2018.
 */

public interface IPlayerDataSource {


    Flowable<List<Player>> loadAllUsers();

    Flowable<Player> loadUserById(int id);

    Flowable<List<Player>> findByFirstAndLastName(String firstName, String lastName);

    Flowable<Player> loadUserBySmashName(String smashName);

    Flowable<List<Player>> findRankGreaterThan(int rank);

    Flowable<List<Player>> findRankLessThan(int rank);

    void insertUser(Player player);

    void deleteUser(Player player);

    void deleteUsersByName(String badName);

    void insertOrReplaceUsers(Player... players);
}
