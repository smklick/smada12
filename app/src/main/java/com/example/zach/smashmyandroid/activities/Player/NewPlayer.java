package com.example.zach.smashmyandroid.activities.Player;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zach.smashmyandroid.R;
import com.example.zach.smashmyandroid.models.Player;

public class NewPlayer extends AppCompatActivity {

    private EditText firstName;
    private EditText lastName;
    private EditText smashName;
    private EditText rank;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_player);

        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        smashName = (EditText) findViewById(R.id.smashName);
        rank = (EditText) findViewById(R.id.rank);
        submit = (Button) findViewById(R.id.submit);

        final Player player;

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // If all fields are populated, create a new Player object using the entered data
                // and return it to the original activity
                if(firstName.getText() != null
                        && lastName.getText() != null
                        && smashName.getText() != null
                        && rank.getText() != null) {

                    final Player p = new Player(firstName.getText().toString(), lastName.getText().toString(), smashName.getText().toString(), Integer.parseInt(rank.getText().toString()));

                    Intent i = new Intent(NewPlayer.this, PlayerManager.class).putExtra("player", p);

                    setResult(RESULT_OK, i);

                } else {
                    Toast.makeText(NewPlayer.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_CANCELED);
                }
                finish();
            }
        });
    }
}
