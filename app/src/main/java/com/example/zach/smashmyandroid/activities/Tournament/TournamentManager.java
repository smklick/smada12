package com.example.zach.smashmyandroid.activities.Tournament;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.zach.smashmyandroid.R;
import com.example.zach.smashmyandroid.database.SmaDatabase;
import com.example.zach.smashmyandroid.database.local.DataSource.TournamentDataSource;
import com.example.zach.smashmyandroid.database.local.Repository.TournamentRepository;
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

public class TournamentManager extends AppCompatActivity {

    private ListView lvTournaments;
    private FloatingActionButton addTournament;

    static final int EDIT_TOURNAMENT = 1;
    static final int NEW_TOURNAMENT = 2;
    static final int VIEW_TOURNAMENT = 3;

    List<Tournament> tournamentList = new ArrayList<>();
    ArrayAdapter adapter;

    private CompositeDisposable compositeDisposable;
    private TournamentRepository tournamentRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_manager);

        compositeDisposable = new CompositeDisposable();

        lvTournaments = (ListView) findViewById(R.id.listTournaments);
        addTournament = (FloatingActionButton) findViewById(R.id.fab);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, tournamentList);
        registerForContextMenu(lvTournaments);
        lvTournaments.setAdapter(adapter);

        SmaDatabase smaDb = SmaDatabase.getInstance(this);
        tournamentRepository = TournamentRepository.getInstance(TournamentDataSource.getInstance(smaDb.tournamentDao()));

        loadData();

        addTournament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TournamentManager.this, NewTournament.class);
                startActivityForResult(i, NEW_TOURNAMENT);
            }
        });

        lvTournaments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tournament t = (Tournament) lvTournaments.getItemAtPosition(position);
                Intent i = new Intent(TournamentManager.this, TournamentDetails.class).putExtra("tournament", t);

                startActivity(i);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == NEW_TOURNAMENT) {
            if(resultCode == RESULT_OK) {
                Tournament t = data.getParcelableExtra("tournament");
                newTournament(t);
            }
        }
    }

    private void loadData() {
        Disposable disposable = tournamentRepository.getAllTournaments()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Tournament>>() {
                    @Override
                    public void accept(List<Tournament> tournaments) throws Exception {
                        onGetAllTournamentsSuccess(tournaments);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(TournamentManager.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void onGetAllTournamentsSuccess(List<Tournament> tournaments) {
        tournamentList.clear();
        tournamentList.addAll(tournaments);
        adapter.notifyDataSetChanged();
    }

    private void newTournament(final Tournament tournament) {
        Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                tournamentRepository.insert(tournament);
                e.onComplete();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Toast.makeText(TournamentManager.this, "Tournament Added", Toast.LENGTH_SHORT).show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(TournamentManager.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                new Action() {
                    @Override
                    public void run() throws Exception {
                        loadData();
                    }
                })
        ;
    }
}
