package com.example.zach.smashmyandroid.activities.Player;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zach.smashmyandroid.R;
import com.example.zach.smashmyandroid.database.SmaDatabase;
import com.example.zach.smashmyandroid.database.local.DataSource.PlayerDataSource;
import com.example.zach.smashmyandroid.database.local.Repository.PlayerRepository;
import com.example.zach.smashmyandroid.database.local.models.Player;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class PlayerDetails extends AppCompatActivity {

    private EditText firstName;
    private EditText lastName;
    private EditText smashName;
    private EditText rank;
    private Button submit;
    private TextView playerId;
    private SmaDatabase smaDb;
    private PlayerRepository playerRepository;
    private CompositeDisposable compositeDisposable;

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

        //submit.setVisibility(View.GONE);

        compositeDisposable = new CompositeDisposable();

        smaDb = SmaDatabase.getInstance(PlayerDetails.this);
        playerRepository = PlayerRepository.getInstance(PlayerDataSource.getInstance(smaDb.playerDao()));

        final Player player = getIntent().getExtras().getParcelable("player");

        firstName.setText(player.getFirstName());
        lastName.setText(player.getLastName());
        smashName.setText(player.getSmashName());
        rank.setText(Integer.toString(player.getRank()));
        playerId.setText(Integer.toString(player.getId()));

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!firstName.getText().equals("") && !lastName.getText().equals("")
                        && !rank.getText().equals("") && !playerId.getText().equals("")) {
                    player.setFirstName(firstName.getText().toString());
                    player.setLastName(lastName.getText().toString());
                    player.setSmashName(smashName.getText().toString());
                    player.setRank(Integer.parseInt(rank.getText().toString()));

                    updatePlayer(player);
                }


                finish();
            }
        });

    }

    private void updatePlayer(Player player) {
        Disposable disposable = io.reactivex.Observable.create(e -> {
            playerRepository.insertOrReplaceUsers(player);
            e.onComplete();
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer() {
                               @Override
                               public void accept(Object o) throws Exception {
                                   Toast.makeText(PlayerDetails.this, "Player Updated" , Toast.LENGTH_SHORT).show();
                               }
                           }, throwable -> Toast.makeText(PlayerDetails.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show(),
                        new Action() {
                            @Override
                            public void run() throws Exception {

                            }
                        }
                );
        compositeDisposable.add(disposable);
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
