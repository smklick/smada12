package com.example.zach.smashmyandroid.activities.Match;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.zach.smashmyandroid.R;
import com.example.zach.smashmyandroid.activities.Tournament.TournamentDetails;
import com.example.zach.smashmyandroid.database.SmaDatabase;
import com.example.zach.smashmyandroid.database.local.DataSource.PlayerDataSource;
import com.example.zach.smashmyandroid.database.local.Repository.PlayerRepository;
import com.example.zach.smashmyandroid.database.local.models.Match;
import com.example.zach.smashmyandroid.database.local.models.Player;
import com.example.zach.smashmyandroid.database.local.models.Tournament;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class NewMatch extends AppCompatActivity {

    List<Player> playerList = new ArrayList<>();
    ArrayAdapter<Player> adapter;

    private CompositeDisposable compositeDisposable;
    private PlayerRepository playerRepository;
    private Spinner winnerDropdown;
    private Spinner loserDropdown;
    private Button submit;

    private Tournament tournament;

    private Player winner;
    private Player loser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_match);

        compositeDisposable = new CompositeDisposable();

        tournament = getIntent().getExtras().getParcelable("tournament");

        winnerDropdown = findViewById(R.id.winner);
        loserDropdown = findViewById(R.id.loser);

        submit = findViewById(R.id.submit);

        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, playerList);
        winnerDropdown.setAdapter(adapter);
        loserDropdown.setAdapter(adapter);

        SmaDatabase smaDb = SmaDatabase.getInstance(this);
        playerRepository = PlayerRepository.getInstance(PlayerDataSource.getInstance(smaDb.playerDao()));

        loadData();

        winnerDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                winner = (Player) winnerDropdown.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        loserDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loser = (Player) loserDropdown.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(winner.getId() != loser.getId()){
                    Match m = new Match(tournament.getId(), winner.getId(), loser.getId());

                    Intent i = new Intent(NewMatch.this, TournamentDetails.class).putExtra("match", m).putExtra("winner", winner).putExtra("loser", loser);

                    Toast.makeText(NewMatch.this, "TournamentID: " + m.getTournamentId() + " WinnerID: " + m.getWinnerId() + " LoserID: " + m.getLoserId(), Toast.LENGTH_SHORT).show();

                    setResult(RESULT_OK, i);
                    finish();
                }else {
                    Toast.makeText(NewMatch.this, "Players cannot play against themselves!", Toast.LENGTH_SHORT).show();
                }
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
                        Toast.makeText(NewMatch.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void onGetAllPlayersSuccess(List<Player> players) {
        playerList.clear();
        playerList.addAll(players);
        adapter.notifyDataSetChanged();
    }
}
