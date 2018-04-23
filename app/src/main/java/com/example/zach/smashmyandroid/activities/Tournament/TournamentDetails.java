package com.example.zach.smashmyandroid.activities.Tournament;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zach.smashmyandroid.R;
import com.example.zach.smashmyandroid.activities.Match.NewMatch;
import com.example.zach.smashmyandroid.activities.Player.PlayerDetails;
import com.example.zach.smashmyandroid.activities.Player.PlayerManager;
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
    private FloatingActionButton addMatch;
    private ListView lvMatches;

    ArrayList<Match> matchList = new ArrayList<>();
    ArrayAdapter adapter;

    private CompositeDisposable compositeDisposable;
    private SmaDatabase smaDb;
    private PlayerRepository playerRepository;
    private MatchRepository matchRepository;

    private int tournamentID;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_details);

        tournamentName = findViewById(R.id.tournamentName);

        final Tournament tournament = getIntent().getExtras().getParcelable("tournament");
        tournamentID = tournament.getId();
        tournamentName.setText(tournament.getName().toString());

        compositeDisposable = new CompositeDisposable();

        lvMatches = findViewById(R.id.tournamentMatchList);

        SmaDatabase smaDb = SmaDatabase.getInstance(this);
        matchRepository = MatchRepository.getInstance(MatchDataSource.getInstance(smaDb.matchDao()));
        playerRepository = PlayerRepository.getInstance(PlayerDataSource.getInstance(smaDb.playerDao()));
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
                TextView winner = convertView.findViewById(R.id.winnerName);
                TextView loser = convertView.findViewById(R.id.loserName);
                TextView matchId = convertView.findViewById(R.id.matchId);

                matchId.setText("Match ID: " + Integer.toString(m.getId()));
                winner.setText(m.getWinnerName());
                loser.setText(m.getLoserName());
                return convertView;
            }
        };

        registerForContextMenu(lvMatches);
        lvMatches.setAdapter(adapter);
        loadData(tournament.getId());

        addMatch.setOnClickListener(v -> {
            Intent i = new Intent(TournamentDetails.this, NewMatch.class).putExtra("tournament", tournament);
            startActivityForResult(i, NEW_MATCH);
        });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle("Select Action:");

        menu.add(Menu.NONE, 0, menu.NONE, "DELETE");
    }

    // Selecting UPDATE or DELETE fires the appropriate event
    // UPDATE currently only allows changing of first name
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Match m = matchList.get(info.position);
        switch (item.getItemId()) {
            case 0: // Delete Match
            {
                new AlertDialog.Builder(TournamentDetails.this)
                        .setMessage("Do you want to delete Match "+ m.getId())
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteMatch(m);
                            }
                        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
            }
            break;
        }
        return true;
    }

    private void deleteMatch(final Match m) {
        Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                matchRepository.delete(m);
                e.onComplete();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer() {
                               @Override
                               public void accept(Object o) throws Exception {

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
                                loadData(tournamentID);
                            }
                        }
                );
        compositeDisposable.add(disposable);
    }

    private void loadData(int id) {
        Disposable matchDisposable = matchRepository.getMatchesByTournament(id)
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
