package com.example.zach.smashmyandroid.activities.Tournament;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zach.smashmyandroid.R;
import com.example.zach.smashmyandroid.activities.Match.NewMatch;
import com.example.zach.smashmyandroid.database.SmaDatabase;
import com.example.zach.smashmyandroid.database.local.DataSource.MatchDataSource;
import com.example.zach.smashmyandroid.database.local.Repository.MatchRepository;
import com.example.zach.smashmyandroid.database.local.models.Match;
import com.example.zach.smashmyandroid.database.local.models.Tournament;

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
    private FloatingActionButton addMatch;
    private ListView lvMatches;

    ArrayList<Match> matchList = new ArrayList<>();
    ArrayAdapter adapter;

    private CompositeDisposable compositeDisposable;
    private SmaDatabase smaDb;
    private MatchRepository matchRepository;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_details);

        tournamentName = findViewById(R.id.tournamentName);

        final Tournament tournament = getIntent().getExtras().getParcelable("tournament");

        tournamentName.setText(tournament.getName().toString());

        compositeDisposable = new CompositeDisposable();

        lvMatches = findViewById(R.id.tournamentMatchList);

        SmaDatabase smaDb = SmaDatabase.getInstance(this);
        matchRepository = MatchRepository.getInstance(MatchDataSource.getInstance(smaDb.matchDao()));

        loadData(tournament.getId());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addMatch = findViewById(R.id.fab);


        adapter = new ArrayAdapter<Match>(this, 0, matchList) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                Match m = matchList.get(position);

                if (convertView == null) {
                    convertView = getLayoutInflater()
                            .inflate(R.layout.match_list_item, null, false);
                }
                TextView winnerName = convertView.findViewById(R.id.winnerName);
                TextView loserName = convertView.findViewById(R.id.loserName);
                TextView matchId = convertView.findViewById(R.id.matchId);

                winnerName.setText(Integer.toString(m.getWinnerId()));
                loserName.setText(Integer.toString(m.getLoserId()));
                matchId.setText("Match ID: " + Integer.toString(m.getId()));
                return convertView;
            }
        };

        registerForContextMenu(lvMatches);
        lvMatches.setAdapter(adapter);
        Toast.makeText(TournamentDetails.this, "Details ID: " + tournament.getId(), Toast.LENGTH_SHORT).show();
        loadData(tournament.getId());

        addMatch.setOnClickListener(v -> {
            Intent i = new Intent(TournamentDetails.this, NewMatch.class).putExtra("tournament", tournament);
            startActivityForResult(i, NEW_MATCH);
        });

    }

    private void loadData(int id) {
        Disposable matchDisposable = matchRepository.getMatchesByTournament(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Match>>() {
                    @Override
                    public void accept(List<Match> matches) throws Exception {

                        onGetTournamentMatchesSuccess(matches);

                       // Toast.makeText(TournamentDetails.this, "Loaded matches!", Toast.LENGTH_SHORT).show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });

        compositeDisposable.add(matchDisposable);
    }

    private void onGetTournamentMatchesSuccess(List<Match> matches) {
        matchList.clear();
        matchList.addAll(matches);
        adapter.notifyDataSetChanged();
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
                                //loadData(tournament.getId());
                            }
                        }
                );
        compositeDisposable.add(disposable);
    }
}
