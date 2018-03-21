package com.example.zach.smashmyandroid.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Update;

import com.example.zach.smashmyandroid.models.Match;

/**
 * Created by Zach on 3/21/2018.
 */

@Dao
public interface MatchDao {

    @Insert
    void insert(Match... match);

    @Update
    void update(Match... match);

    @Delete
    void delete(Match... match);

}
