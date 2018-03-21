
package com.example.zach.smashmyandroid;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.zach.smashmyandroid.database.PlayerDatabase;
import com.example.zach.smashmyandroid.database.PlayerRepository;
import com.example.zach.smashmyandroid.local.PlayerDataSource;
import com.example.zach.smashmyandroid.models.Player;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private ListView lvPlayers;
    private FloatingActionButton addPlayer;

    List<Player> playersList = new ArrayList<>();
    ArrayAdapter adapter;

    private CompositeDisposable compositeDisposable;
    private PlayerRepository playerRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        compositeDisposable = new CompositeDisposable();

        lvPlayers = (ListView) findViewById(R.id.listPlayers);
        addPlayer = (FloatingActionButton) findViewById(R.id.fab);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, playersList);
        registerForContextMenu(lvPlayers);
        lvPlayers.setAdapter(adapter);

        PlayerDatabase playerDatabase = PlayerDatabase.getInstance(this);
        playerRepository = PlayerRepository.getInstance(PlayerDataSource.getInstance(playerDatabase.playerDao()));

        loadData();

        addPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
                    @Override
                    public void subscribe(ObservableEmitter<Object> e) throws Exception {
                        Player player = new Player("Zach", "Cutshall", "DadMuscles", 700);
                        playerRepository.insertUser(player);
                        e.onComplete();
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Consumer() {
                                       @Override
                                       public void accept(Object o) throws Exception {
                                           Toast.makeText(MainActivity.this, "User Added", Toast.LENGTH_SHORT).show();
                                       }
                                   }, new Consumer<Throwable>() {
                                       @Override
                                       public void accept(Throwable throwable) throws Exception {
                                           Toast.makeText(MainActivity.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                       }
                                   },
                                new Action() {
                                    @Override
                                    public void run() throws Exception {
                                        loadData();
                                    }
                                }
                        );
            }
        });
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
                        Toast.makeText(MainActivity.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        compositeDisposable.add(disposable);
    }
    private void onGetAllPlayersSuccess(List<Player> players) {
        playersList.clear();
        playersList.addAll(players);
        adapter.notifyDataSetChanged();
    }
}
