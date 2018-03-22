package com.example.zach.smashmyandroid.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.zach.smashmyandroid.local.PlayerDao;
import com.example.zach.smashmyandroid.models.Player;

import static com.example.zach.smashmyandroid.database.PlayerDatabase.DATABASE_VERSION;

/**
 * Created by Zach on 3/21/2018.
 */
@Database(entities = Player.class, version = DATABASE_VERSION)
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
