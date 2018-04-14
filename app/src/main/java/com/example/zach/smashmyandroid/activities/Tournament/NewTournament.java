package com.example.zach.smashmyandroid.activities.Tournament;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zach.smashmyandroid.R;
import com.example.zach.smashmyandroid.database.SmaDatabase;
import com.example.zach.smashmyandroid.database.local.DataSource.TournamentDataSource;
import com.example.zach.smashmyandroid.database.local.Repository.TournamentRepository;
import com.example.zach.smashmyandroid.database.local.models.Tournament;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NewTournament extends AppCompatActivity {

    TextView tournamentName;
    Button submit;

    TournamentRepository tr;
    SmaDatabase smaDb;

    CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tournament);

        smaDb = SmaDatabase.getInstance(this);
        tr = TournamentRepository.getInstance(TournamentDataSource.getInstance(smaDb.tournamentDao()));
        compositeDisposable = new CompositeDisposable();

        tournamentName = findViewById(R.id.tournamentName);
        submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tournamentName.getText() != null) {
                    Tournament t = new Tournament(tournamentName.getText().toString());
                    newTournament(t);
                    finish();
                }
            }
        });

    }

    private void newTournament(final Tournament t) {
        Disposable disposable = io.reactivex.Observable.create(e -> {
            tr.insert(t);
            e.onComplete();
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(o -> Toast.makeText(NewTournament.this, "User Added", Toast.LENGTH_SHORT).show(),
                        throwable -> Toast.makeText(NewTournament.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show(),
                        () -> {
                            //loadData();
                        }
                );
        compositeDisposable.add(disposable);
    }
}
