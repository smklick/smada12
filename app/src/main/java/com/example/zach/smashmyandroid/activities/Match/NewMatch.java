package com.example.zach.smashmyandroid.activities.Match;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zach.smashmyandroid.R;
import com.example.zach.smashmyandroid.database.SmaDatabase;
import com.example.zach.smashmyandroid.database.local.DataSource.MatchDataSource;
import com.example.zach.smashmyandroid.database.local.DataSource.PlayerDataSource;
import com.example.zach.smashmyandroid.database.local.Repository.MatchRepository;
import com.example.zach.smashmyandroid.database.local.Repository.PlayerRepository;
import com.example.zach.smashmyandroid.database.local.models.Match;
import com.example.zach.smashmyandroid.database.local.models.Player;
import com.example.zach.smashmyandroid.database.local.models.Tournament;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class NewMatch extends AppCompatActivity {

    List<Player> playerList = new ArrayList<>();
    ArrayAdapter<Player> adapter;


    private CompositeDisposable compositeDisposable;
    private PlayerRepository playerRepository;
    private MatchRepository matchRepository;
    private Spinner winnerDropdown;
    private Spinner loserDropdown;
    private Button submit;
    private TextView tournamentId;
    private int winnerIndex = -1;
    private int loserIndex = -1;

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
      
        tournamentId = findViewById(R.id.tournId);
        tournamentId.setText("Tournament ID: " + tournament.getId());

        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, playerList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView spin = (TextView) super.getView(position, convertView, parent);
                spin.setTextColor(getResources().getColor(R.color.colorPrimary));
                spin.setTextSize(24);
                return spin;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                // View itemView = super.getDropDownView(position, convertView, parent);
                TextView spin = (TextView) super.getDropDownView(position, convertView, parent);
                spin.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                spin.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                //spin.setHeight(60);
                spin.setTextSize(24);
                if (position == winnerIndex) {

                    spin.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    spin.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                if (position == loserIndex) {
                    spin.setBackgroundColor(getResources().getColor(R.color.colorSecondaryDark));
                    spin.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
                return spin;
            }
        };

        winnerDropdown.setAdapter(adapter);
        loserDropdown.setAdapter(adapter);

        SmaDatabase smaDb = SmaDatabase.getInstance(this);
        playerRepository = PlayerRepository.getInstance(PlayerDataSource.getInstance(smaDb.playerDao()));

        matchRepository = MatchRepository.getInstance(MatchDataSource.getInstance(smaDb.matchDao()));

        loadData();

        winnerDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                winnerIndex = position;
                winner = (Player) winnerDropdown.getSelectedItem();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        loserDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loserIndex = position;
                loser = (Player) loserDropdown.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (winner.getId() != loser.getId()) {
                    Match m = new Match(tournament.getId(), winner.getId(), winner.getSmashName(), loser.getId(), loser.getSmashName());

                    createMatch(m);

                    finish();
                } else {
                    Toast.makeText(NewMatch.this, "Players cannot play against themselves!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void createMatch(Match m) {
        Disposable disposable = io.reactivex.Observable.create(e -> {
            matchRepository.insert(m);
            e.onComplete();
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer() {
                               @Override
                               public void accept(Object o) throws Exception {
                                   Toast.makeText(NewMatch.this, "Match Added. Tournament ID: " + m.getTournamentId() , Toast.LENGTH_SHORT).show();
                               }
                           }, throwable -> Toast.makeText(NewMatch.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show(),
                        new Action() {
                            @Override
                            public void run() throws Exception {
                                loadData();
                            }
                        }
                );
        compositeDisposable.add(disposable);
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
