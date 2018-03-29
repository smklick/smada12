package com.example.zach.smashmyandroid.activities.Player;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.zach.smashmyandroid.R;
import com.example.zach.smashmyandroid.database.MatchRepository;
import com.example.zach.smashmyandroid.database.SmaDatabase;
import com.example.zach.smashmyandroid.local.MatchDataSource;
import com.example.zach.smashmyandroid.models.Match;
import com.example.zach.smashmyandroid.models.Player;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class PlayerProfile extends AppCompatActivity {

    SmaDatabase mDb;

    private ListView lvMatches;

    List<Match> matchList = new ArrayList<>();
    ArrayAdapter adapter;

    private Player p;

    private CompositeDisposable mCompositeDisposable;
    private MatchRepository matchRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile);

        mCompositeDisposable = new CompositeDisposable();

        lvMatches = (ListView) findViewById(R.id.matches);

        // Create new array adapter
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, matchList);

        // Register list view for use with context menus
        registerForContextMenu(lvMatches);

        // Assign adapter to the listView
        lvMatches.setAdapter(adapter);

        mDb = SmaDatabase.getInstance(this);
        matchRepository = MatchRepository.getInstance(MatchDataSource.getInstance(mDb.matchDao()));
    }


    private void loadData(int id) {
        Disposable disposable = matchRepository.getMatchesByUser(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Match>>() {
                    @Override
                    public void accept(List<Match> matches) throws Exception {
                        onGetPlayerMatchesSuccess(matches);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(PlayerProfile.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    private void onGetPlayerMatchesSuccess(List<Match> matches) {
        matchList.clear();;
        matchList.addAll(matches);
        adapter.notifyDataSetChanged();
    }
}

