package com.example.zach.smashmyandroid.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zach.smashmyandroid.R;
import com.example.zach.smashmyandroid.models.Player;

public class PlayerDetails extends AppCompatActivity {

    private EditText firstName;
    private EditText lastName;
    private EditText smashName;
    private EditText rank;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_detials);

        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        smashName = (EditText) findViewById(R.id.smashName);
        rank = (EditText) findViewById(R.id.rank);
        submit = (Button) findViewById(R.id.submit);

        firstName.setEnabled(false);
        lastName.setEnabled(false);
        smashName.setEnabled(false);
        rank.setEnabled(false);
        submit.setVisibility(View.GONE);

        Player player = getIntent().getExtras().getParcelable("player");

        firstName.setText(player.getFirstName());
        lastName.setText(player.getLastName());
        smashName.setText(player.getSmashName());
        rank.setText(Integer.toString(player.getRank()));

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
