package com.example.zach.smashmyandroid.activities.Player;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zach.smashmyandroid.R;
import com.example.zach.smashmyandroid.database.SmaDatabase;
import com.example.zach.smashmyandroid.database.local.DataSource.PlayerDataSource;
import com.example.zach.smashmyandroid.database.local.Repository.PlayerRepository;
import com.example.zach.smashmyandroid.database.local.models.Player;

import java.util.List;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class NewPlayer extends AppCompatActivity {

    private EditText firstName;
    private EditText lastName;
    private EditText smashName;
    private EditText rank;
    private Button submit;

    CompositeDisposable compositeDisposable;

    SmaDatabase smaDb;
    PlayerRepository pr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_player);

        compositeDisposable = new CompositeDisposable();

        smaDb = SmaDatabase.getInstance(NewPlayer.this);
        pr = PlayerRepository.getInstance(PlayerDataSource.getInstance(smaDb.playerDao()));

        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        smashName = (EditText) findViewById(R.id.smashName);
        rank = (EditText) findViewById(R.id.rank);
        submit = (Button) findViewById(R.id.submit);

        final Player player;

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // If all fields are populated, create a new Player object using the entered data
                // and return it to the original activity
                if(firstName.getText() != null
                        && lastName.getText() != null
                        && smashName.getText() != null
                        && rank.getText() != null) {

                    final Player p = new Player(firstName.getText().toString(), lastName.getText().toString(), smashName.getText().toString(), Integer.parseInt(rank.getText().toString()));

                    Intent i = new Intent(NewPlayer.this, PlayerManager.class).putExtra("player", p);
                    newPlayer(p);
                    setResult(RESULT_OK, i);

                } else {
                    Toast.makeText(NewPlayer.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_CANCELED);
                }

                finish();
            }
        });
    }

    private void newPlayer(final Player player) {
        Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                pr.insertUser(player);
                e.onComplete();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer() {
                               @Override
                               public void accept(Object o) throws Exception {
                                   Toast.makeText(NewPlayer.this, "User Added", Toast.LENGTH_SHORT).show();
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Toast.makeText(NewPlayer.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           },
                        new Action() {
                            @Override
                            public void run() throws Exception {
                                //loadData();
                            }
                        }
                );
        compositeDisposable.add(disposable);
    }

    private void loadData() {
        Disposable disposable = pr.loadAllUsers()
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
                        Toast.makeText(NewPlayer.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void onGetAllPlayersSuccess(List<Player> players) {
        compositeDisposable.clear();
    }
}
