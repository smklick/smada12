package com.example.zach.smashmyandroid.activities.Match;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.zach.smashmyandroid.R;
import com.example.zach.smashmyandroid.database.SmaDatabase;
import com.example.zach.smashmyandroid.local.DataSource.PlayerDataSource;
import com.example.zach.smashmyandroid.local.Repository.PlayerRepository;
import com.example.zach.smashmyandroid.local.models.Player;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class NewMatch extends AppCompatActivity {

    List<Player> playerList = new ArrayList<>();
    ArrayAdapter adapter;

    private CompositeDisposable compositeDisposable;
    private PlayerRepository playerRepository;
    private Spinner winnerDropdown;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_match);

        compositeDisposable = new CompositeDisposable();

        winnerDropdown = findViewById(R.id.winner);
        adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, playerList);

        SmaDatabase smaDb = SmaDatabase.getInstance(this);
        playerRepository = PlayerRepository.getInstance(PlayerDataSource.getInstance(smaDb.playerDao()));

        loadData();
    }

    private void loadData() {
        Disposable disposable = playerRepository.loadAllUsers()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Player>>() {
                    @Override
                    public void accept(List<Player> players) throws Exception {
                        onGetAllPlayersSuccess(players);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(NewMatch.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void onGetAllPlayersSuccess(List<Player> players) {
        playerList.clear();
        playerList.addAll(players);
        adapter.notifyDataSetChanged();
    }
}
