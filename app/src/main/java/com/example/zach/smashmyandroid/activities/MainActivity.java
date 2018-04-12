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

import com.example.zach.smashmyandroid.R;

import com.example.zach.smashmyandroid.activities.Player.FragmentPlayerList;
import com.example.zach.smashmyandroid.activities.Player.PlayerManager;
import com.example.zach.smashmyandroid.activities.Player.TestFragment;
import com.example.zach.smashmyandroid.activities.Tournament.FragmentTournamentList;
import com.example.zach.smashmyandroid.database.SmaDatabase;
import com.example.zach.smashmyandroid.local.DataSource.PlayerDataSource;
import com.example.zach.smashmyandroid.local.Repository.PlayerRepository;
import com.example.zach.smashmyandroid.local.models.Player;
import com.example.zach.smashmyandroid.local.models.Tournament;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {

    FragmentPlayerList playerFragment;
    FragmentTournamentList tournamentFragment;
    TestFragment tf;

    PlayerRepository playerRepository;
    SmaDatabase smaDb;
    private CompositeDisposable compositeDisposable;
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

        SmaDatabase playerDatabase = SmaDatabase.getInstance(this);
        playerRepository = PlayerRepository.getInstance(PlayerDataSource.getInstance(playerDatabase.playerDao()));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        playerFragment = FragmentPlayerList.newInstance(players);
        tournamentFragment = FragmentTournamentList.newInstance(tournaments);
        tf = new TestFragment();

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
                                //deleteAllUsers();
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
}
