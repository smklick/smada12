package com.example.zach.smashmyandroid.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.zach.smashmyandroid.R;

import com.example.zach.smashmyandroid.activities.Player.FragmentPlayerList;
import com.example.zach.smashmyandroid.activities.Tournament.FragmentTournamentList;
import com.example.zach.smashmyandroid.database.SmaDatabase;
import com.example.zach.smashmyandroid.local.DataSource.PlayerDataSource;
import com.example.zach.smashmyandroid.local.DataSource.TournamentDataSource;
import com.example.zach.smashmyandroid.local.Repository.PlayerRepository;
import com.example.zach.smashmyandroid.local.Repository.TournamentRepository;
import com.example.zach.smashmyandroid.local.models.Player;
import com.example.zach.smashmyandroid.local.models.Tournament;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    //Fragments
    FragmentPlayerList playerFragment;
    FragmentTournamentList tf;

    //DB
    PlayerRepository playerRepository;
    TournamentRepository tournamentRepository;
    SmaDatabase smaDb;

    private CompositeDisposable compositeDisposable;

    // Arrays
    ArrayList<Player> players = new ArrayList<>();
    ArrayList<Tournament> tournaments = new ArrayList<>();
    ArrayAdapter adapter;

    public boolean OnCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        compositeDisposable = new CompositeDisposable();

        smaDb = SmaDatabase.getInstance(this);
        playerRepository = PlayerRepository.getInstance(PlayerDataSource.getInstance(smaDb.playerDao()));
        tournamentRepository = TournamentRepository.getInstance(TournamentDataSource.getInstance(smaDb.tournamentDao()));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        playerFragment = FragmentPlayerList.newInstance(players);
        tf = FragmentTournamentList.newInstance(tournaments);

        getSupportFragmentManager().beginTransaction().add(R.id.contentFrame, playerFragment);

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);

        tabs.addTab(tabs.newTab().setText("Players"));
        tabs.addTab(tabs.newTab().setText("Tournaments"));

        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {



            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Fragment selected = playerFragment;
                if(position == 0) {
                    selected = playerFragment;
                }else if (position == 1) {
                    selected = tf;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.contentFrame, selected).commit();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Fragment selected = playerFragment;
                if(position == 0) {
                    selected = playerFragment;
                }else if (position == 1) {
                    selected = tf;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.contentFrame, selected).commit();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_clear:

                // Add a confirmation message before deletion
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete All Players")
                        .setMessage("Are you sure?")
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteAllData();
                            }
                        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
                break;

            case R.id.menu_dummy:
                Intent i = new Intent(MainActivity.this, Dummy.class);
                startActivity(i);

        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllData() {

        // Clear Player table
        Disposable disposable = io.reactivex.Observable.create(e -> {
            playerRepository.clearTable();
            e.onComplete();
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(o -> Toast.makeText(MainActivity.this, "User Added", Toast.LENGTH_SHORT).show(),
                        throwable -> Toast.makeText(MainActivity.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show(),
                        () -> {
                            //loadData();
                        }
                );
        compositeDisposable.add(disposable);

        // Clear Tournament Table
        disposable = io.reactivex.Observable.create(e -> {
            tournamentRepository.clearTable();
            e.onComplete();
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(o -> Toast.makeText(MainActivity.this, "User Added", Toast.LENGTH_SHORT).show(),
                        throwable -> Toast.makeText(MainActivity.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show(),
                        () -> {
                            //loadData();
                        }
                );
        compositeDisposable.add(disposable);
    }
}
