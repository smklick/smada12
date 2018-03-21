package com.example.zach.smashmyandroid.database;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.zach.smashmyandroid.local.PlayerDao;

/**
 * Created by Zach on 3/21/2018.
 */

public abstract class PlayerDatabase extends RoomDatabase {
    public static final int DATABASE_VERSION=1;
    public static final String DATABASE_NAME="smada12";
    public abstract PlayerDao playerDao();
    private static PlayerDatabase INSTANCE;
    public static PlayerDatabase getInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, PlayerDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
