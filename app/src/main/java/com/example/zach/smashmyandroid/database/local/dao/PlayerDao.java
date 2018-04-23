package com.example.zach.smashmyandroid.database.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.zach.smashmyandroid.database.local.models.Player;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;
import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by Zach on 3/19/2018.
 * Data Access Object for Player.
 * Defines actions that can be performed with Player table in PlayerDatabase
 *
 */

@Dao
public interface PlayerDao {

    @Query("select * from Players")
    Flowable<List<Player>> loadAllUsers();

    @Query("select * from Players where id = :id")
    Player loadUserById(int id);

    @Query("select * from Players where firstName = :firstName " +
            "and lastName = :lastName")
    Flowable<List<Player>> findByFirstAndLastName(String firstName, String lastName);

    @Query("select * from Players where smashName = :smashName")
    Flowable<Player> loadUserBySmashName(String smashName);

    @Query("select * from Players where rank >= :rank")
    Flowable<List<Player>> findRankGreaterThan(int rank);

    @Query("select * from Players where rank <= :rank")
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

