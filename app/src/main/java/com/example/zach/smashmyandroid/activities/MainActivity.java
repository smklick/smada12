package com.example.zach.smashmyandroid.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.zach.smashmyandroid.R;
import com.example.zach.smashmyandroid.activities.Player.PlayerManager;
import com.example.zach.smashmyandroid.activities.Tournament.TournamentManager;

public class MainActivity extends AppCompatActivity {

    Button playerManager;
    Button tournamentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerManager = findViewById(R.id.playerManager);
        tournamentManager = findViewById(R.id.tournamentManager);



        playerManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, PlayerManager.class);
                startActivity(i);
            }
        });

        tournamentManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, TournamentManager.class);
                startActivity(i);
            }
        });
    }
}
