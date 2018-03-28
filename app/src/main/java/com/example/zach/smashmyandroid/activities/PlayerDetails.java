package com.example.zach.smashmyandroid.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zach.smashmyandroid.R;
import com.example.zach.smashmyandroid.models.Player;

public class PlayerDetails extends AppCompatActivity {

    private EditText firstName;
    private EditText lastName;
    private EditText smashName;
    private EditText rank;
    private Button submit;
    private TextView playerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_detials);

        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        smashName = (EditText) findViewById(R.id.smashName);
        rank = (EditText) findViewById(R.id.rank);
        submit = (Button) findViewById(R.id.submit);
        playerId = (TextView) findViewById(R.id.playerID);

        firstName.setEnabled(false);
        lastName.setEnabled(false);
        smashName.setEnabled(false);
        rank.setEnabled(false);
        submit.setVisibility(View.GONE);

        final Player player = getIntent().getExtras().getParcelable("player");

        firstName.setText(player.getFirstName());
        lastName.setText(player.getLastName());
        smashName.setText(player.getSmashName());
        rank.setText(Integer.toString(player.getRank()));
        playerId.setText(Integer.toString(player.getId()));

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.setFirstName(firstName.getText().toString());
                player.setLastName(lastName.getText().toString());
                player.setSmashName(smashName.getText().toString());
                player.setRank(Integer.parseInt(rank.getText().toString()));

                Intent i = new Intent(PlayerDetails.this, PlayerManager.class).putExtra("player", player);
                setResult(RESULT_OK, i);
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.player_details_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_edit:
                makeUserEditable();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void makeUserEditable() {
        firstName.setEnabled(true);
        lastName.setEnabled(true);
        smashName.setEnabled(true);
        rank.setEnabled(true);
        submit.setVisibility(View.VISIBLE);
    }
}
