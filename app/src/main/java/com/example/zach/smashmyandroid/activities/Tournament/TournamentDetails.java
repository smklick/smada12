package com.example.zach.smashmyandroid.activities.Tournament;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zach.smashmyandroid.R;
import com.example.zach.smashmyandroid.activities.Match.NewMatch;
import com.example.zach.smashmyandroid.activities.Player.PlayerManager;
import com.example.zach.smashmyandroid.database.SmaDatabase;
import com.example.zach.smashmyandroid.local.DataSource.MatchDataSource;
import com.example.zach.smashmyandroid.local.DataSource.PlayerDataSource;
import com.example.zach.smashmyandroid.local.Repository.MatchRepository;
import com.example.zach.smashmyandroid.local.Repository.PlayerRepository;
import com.example.zach.smashmyandroid.local.models.Match;
import com.example.zach.smashmyandroid.local.models.Tournament;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class TournamentDetails extends AppCompatActivity {

    private static final int NEW_MATCH = 1;

    private TextView tournamentName;
    private ListView lvMatches;
    private FloatingActionButton addMatch;

    List<Match> matchList = new ArrayList<>();
    ArrayAdapter adapter;

    private CompositeDisposable compositeDisposable;
    private MatchRepository matchRepository;
    private PlayerRepository playerRepository;

    private Tournament tournament;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_details);

        tournament = getIntent().getExtras().getParcelable("tournament");

        compositeDisposable = new CompositeDisposable();

        tournamentName = findViewById(R.id.tournamentName);
        tournamentName.setText(tournament.getName().toString());

        lvMatches = findViewById(R.id.listMatches);
        addMatch = findViewById(R.id.fab);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, matchList);
        registerForContextMenu(lvMatches);
        lvMatches.setAdapter(adapter);

        SmaDatabase smaDb = SmaDatabase.getInstance(this);
        matchRepository = MatchRepository.getInstance(MatchDataSource.getInstance(smaDb.matchDao()));
        playerRepository = PlayerRepository.getInstance(PlayerDataSource.getInstance(smaDb.playerDao()));
        loadData();

        addMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TournamentDetails.this, NewMatch.class).putExtra("tournament", tournament);
                startActivityForResult(i, NEW_MATCH);
            }
        });

        lvMatches.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Match m = (Match) lvMatches.getItemAtPosition(position);
                // Pass intent to next activity.
            }
        });
    }

    private void loadData() {
        Disposable disposable = matchRepository.getMatchesByTournament(tournament.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Match>>() {
                    @Override
                    public void accept(List<Match> matches) throws Exception {
                        onGetTournamentMatchesSuccess(matches);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(TournamentDetails.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void onGetTournamentMatchesSuccess(List<Match> matches) {
        matchList.clear();
        matchList.addAll(matches);
        adapter.notifyDataSetChanged();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == NEW_MATCH) {
            if(resultCode == RESULT_OK) {
                final Match m = data.getParcelableExtra("match");

                Toast.makeText(TournamentDetails.this, "id: " + m.getId() + " winnerId: " + m.getWinnerId() + " loserId: " + m.getLoserId(), Toast.LENGTH_SHORT).show();

                createMatch(m);
                loadData();
            }
        }
    }

    private void createMatch(final Match m) {
        Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                matchRepository.insert(m);
                e.onComplete();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer() {
                               @Override
                               public void accept(Object o) throws Exception {
                                   Toast.makeText(TournamentDetails.this, "Match Added", Toast.LENGTH_SHORT).show();
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Toast.makeText(TournamentDetails.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           },
                        new Action() {
                            @Override
                            public void run() throws Exception {
                                loadData();
                            }
                        }
                );
        loadData();
        compositeDisposable.add(disposable);
    }
}
