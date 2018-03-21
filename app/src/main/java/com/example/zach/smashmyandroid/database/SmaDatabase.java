package com.example.zach.smashmyandroid.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.zach.smashmyandroid.local.MatchDao;
import com.example.zach.smashmyandroid.local.PlayerDao;
import com.example.zach.smashmyandroid.models.Match;
import com.example.zach.smashmyandroid.models.Player;

/**
 * Created by Zach on 3/21/2018.
 */

@Database(entities = {Player.class, Match.class},
            version = 1, exportSchema = false)

public abstract class SmaDatabase extends RoomDatabase {

    private static SmaDatabase INSTANCE;

    public abstract PlayerDao getPlayerDao();
    public abstract MatchDao getMatchDao();

    public static SmaDatabase getInMemoryDatabase(Context context) {
        if(INSTANCE == null) {
            INSTANCE =
                    Room.inMemoryDatabaseBuilder(context.getApplicationContext(),
                            SmaDatabase.class).build();

        }
        return INSTANCE;
    }

    public static SmaDatabase getFileDatabase(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    SmaDatabase.class,
                    "smada12")
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() { INSTANCE = null;}

}
