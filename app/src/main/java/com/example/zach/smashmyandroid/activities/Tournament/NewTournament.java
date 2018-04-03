package com.example.zach.smashmyandroid.activities.Tournament;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.zach.smashmyandroid.R;
import com.example.zach.smashmyandroid.local.models.Tournament;

public class NewTournament extends AppCompatActivity {

    TextView tournamentName;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tournament);

        tournamentName = findViewById(R.id.tournamentName);
        submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tournamentName.getText() != null) {
                    Tournament t = new Tournament(tournamentName.getText().toString());

                    Intent i = new Intent(NewTournament.this, TournamentManager.class).putExtra("tournament", t);

                    setResult(RESULT_OK, i);
                    finish();
                }
            }
        });

    }
}
