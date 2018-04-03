package com.example.zach.smashmyandroid.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.zach.smashmyandroid.local.dao.MatchDao;
import com.example.zach.smashmyandroid.local.dao.PlayerDao;
import com.example.zach.smashmyandroid.local.dao.TournamentDao;
import com.example.zach.smashmyandroid.local.models.Match;
import com.example.zach.smashmyandroid.local.models.Player;
import com.example.zach.smashmyandroid.local.models.Tournament;

import static com.example.zach.smashmyandroid.database.SmaDatabase.DATABASE_VERSION;

/**
 * Created by Zach on 3/21/2018.
 * Add new tables to the @Database(entitities) array.
 * Database Version MUST be incremented or the app will crash!!!
 */
@Database(entities = {Player.class, Match.class, Tournament.class}, version = DATABASE_VERSION)
public abstract class SmaDatabase extends RoomDatabase {
    public static final int DATABASE_VERSION=4;
    public static final String DATABASE_NAME="smada12";
    public abstract PlayerDao playerDao();
    public abstract MatchDao matchDao();
    public abstract TournamentDao tournamentDao();
    private static SmaDatabase INSTANCE;
    public static SmaDatabase getInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, SmaDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
