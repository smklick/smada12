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
import com.example.zach.smashmyandroid.database.SmaDatabase;
import com.example.zach.smashmyandroid.local.DataSource.MatchDataSource;
import com.example.zach.smashmyandroid.local.Repository.MatchRepository;
import com.example.zach.smashmyandroid.local.models.Match;
import com.example.zach.smashmyandroid.local.models.Tournament;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
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

        loadData();

        addMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TournamentDetails.this, NewMatch.class);
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
}
