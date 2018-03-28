package com.example.zach.smashmyandroid.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.zach.smashmyandroid.R;

public class MainActivity extends AppCompatActivity {

    Button playerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerManager = findViewById(R.id.playerManager);

        playerManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, PlayerManager.class);
                startActivity(i);
            }
        });
    }
}
