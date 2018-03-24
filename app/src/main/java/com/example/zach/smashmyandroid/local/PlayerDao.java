package com.example.zach.smashmyandroid.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.zach.smashmyandroid.models.Player;

import java.util.List;

import io.reactivex.Flowable;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;
import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by Zach on 3/19/2018.
 */

@Dao
public interface PlayerDao {

    @Query("select * from Players")
    Flowable<List<Player>> loadAllUsers();

    @Query("select * from Players where id = :id")
    Flowable<Player> loadUserById(int id);

    @Query("select * from Players where firstName = :firstName " +
            "and lastName = :lastName")
    Flowable<List<Player>> findByFirstAndLastName(String firstName, String lastName);

    @Query("select * from Players where smashName = :smashName")
    Flowable<Player> loadUserBySmashName(String smashName);

    @Query("select * from Players where assFactor >= :rank")
    Flowable<List<Player>> findRankGreaterThan(int rank);

    @Query("select * from Players where assFactor <= :rank")
    Flowable<List<Player>> findRankLessThan(int rank);

    @Insert(onConflict = IGNORE)
    void insertUser(Player player);

    @Delete
    void deleteUser(Player player);

    @Query("delete from Players " +
            "where firstName like :badName OR lastName like :badName")
    void deleteUsersByName(String badName);

    @Query("delete from Players where id = :id")
    void deleteUser(int id);

    @Query("delete from Players")
    public void clearTable();

    @Insert(onConflict = IGNORE)
    void insertOrReplaceUsers(Player... players);

    @Update(onConflict = REPLACE)
    int updatePlayer(Player player);
}

