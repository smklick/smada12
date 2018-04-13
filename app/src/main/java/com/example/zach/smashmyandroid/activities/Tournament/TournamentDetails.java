package com.example.zach.smashmyandroid.activities.Tournament;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.zach.smashmyandroid.local.models.Player;
import com.example.zach.smashmyandroid.local.models.Tournament;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
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

    ArrayList<Match> matchList = new ArrayList<>();
    List<Player> playerList = new ArrayList<>();
    ArrayAdapter adapter;

    private CompositeDisposable compositeDisposable;
    private SmaDatabase smaDb;
    private MatchRepository matchRepository;
    private PlayerRepository playerRepository;

    private Tournament tournament;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_details);

        smaDb = SmaDatabase.getInstance(this);
        matchRepository = MatchRepository.getInstance(MatchDataSource.getInstance(smaDb.matchDao()));

        tournament = getIntent().getExtras().getParcelable("tournament");

        compositeDisposable = new CompositeDisposable();

        tournamentName = findViewById(R.id.tournamentName);
        tournamentName.setText(tournament.getName().toString());

        lvMatches = findViewById(R.id.listMatches);
        addMatch = findViewById(R.id.fab);

        adapter = new ArrayAdapter<Match>(this, 0, matchList) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Match m = matchList.get(position);
                final Player[] loser = new Player[1];
                final Player[] winner = new Player[1];
                Observable.just(smaDb).subscribeOn(Schedulers.io()).subscribe(smaDb -> winner[0] = playerRepository.loadUserById(m.getWinnerId()));
                Observable.just(smaDb).subscribeOn(Schedulers.io()).subscribe(smaDb -> loser[0] = playerRepository.loadUserById(m.getLoserId()));


                if (convertView == null) {
                    convertView = getLayoutInflater()
                            .inflate(R.layout.match_list_item, null, false);
                }
                TextView winnerName = convertView.findViewById(R.id.winnerName);
                TextView loserName = convertView.findViewById(R.id.loserName);

                winnerName.setText(winner[0].getFirstName() + " " + winner[0].getLastName());
                loserName.setText(loser[0].getFirstName() + " " + loser[0].getLastName());

                return convertView;
            }
        };

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
        Disposable matchDisposable = matchRepository.getMatchesByTournament(tournament.getId())
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

        Disposable playerDisposable = playerRepository.loadAllUsers()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Player>>() {
                    @Override
                    public void accept(List<Player> players) throws Exception {
                        onGetPlayersSuccess(players);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(TournamentDetails.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        compositeDisposable.add(matchDisposable);
        compositeDisposable.add(playerDisposable);
    }

    private void onGetPlayersSuccess(List<Player> players) {
        playerList.clear();
        playerList.addAll(players);
        adapter.notifyDataSetChanged();
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
