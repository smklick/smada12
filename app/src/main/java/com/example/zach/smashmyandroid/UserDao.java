package com.example.zach.smashmyandroid;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

/**
 * Created by Zach on 3/19/2018.
 */

@Dao
public interface UserDao {

    @Query("select * from Members")
    List<User> loadAllUsers();

    @Query("select * from Members where id = :id")
    User loadUserById(int id);

    @Query("select * from Members where firstName = :firstName " +
            "and lastName = :lastName")
    List<User> findByFirstAndLastName(String firstName, String lastName);

    @Query("select * from Members where smashName = :smashName")
    User loadUserBySmashName();

    @Query("select * from Members where assFactor >= :rank")
    List<User> findRankGreaterThan(double rank);

    @Query("select * from Members where assFactor <= :rank")
    List<User> findRankLessThan(double rank);

    @Insert(onConflict = IGNORE)
    void insertUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("delete from User " +
            "where firstName like :badName OR lastName like :badName")
    int deleteUsersByName(String badName);

    @Insert(onConflict = IGNORE)
    void insertOrReplaceUsers(User... users);
}

