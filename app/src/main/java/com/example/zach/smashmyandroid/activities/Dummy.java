package com.example.zach.smashmyandroid.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.zach.smashmyandroid.R;
import com.example.zach.smashmyandroid.activities.Player.PlayerManager;
import com.example.zach.smashmyandroid.database.SmaDatabase;
import com.example.zach.smashmyandroid.local.DataSource.MatchDataSource;
import com.example.zach.smashmyandroid.local.DataSource.PlayerDataSource;
import com.example.zach.smashmyandroid.local.DataSource.TournamentDataSource;
import com.example.zach.smashmyandroid.local.Repository.MatchRepository;
import com.example.zach.smashmyandroid.local.Repository.PlayerRepository;
import com.example.zach.smashmyandroid.local.Repository.TournamentRepository;
import com.example.zach.smashmyandroid.local.models.Match;
import com.example.zach.smashmyandroid.local.models.Player;
import com.example.zach.smashmyandroid.local.models.Tournament;

import java.util.ArrayList;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/*
 * This activity is activated from the MainActivity toolbar, and simply
 * adds data to the database for testing purposes. There's probably a
 * better way of achieving this but this was a quick and dirty solution.
 */

public class Dummy extends AppCompatActivity {

    ArrayList<Player> players = new ArrayList<>();
    ArrayList<Tournament> tournaments = new ArrayList<>();
    ArrayList<Match> matches = new ArrayList<>();

    PlayerRepository pr;
    TournamentRepository tr;
    MatchRepository mr;

    SmaDatabase db;

    CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);

        compositeDisposable = new CompositeDisposable();

        db = SmaDatabase.getInstance(this);
        pr = PlayerRepository.getInstance(PlayerDataSource.getInstance(db.playerDao()));
        tr = TournamentRepository.getInstance(TournamentDataSource.getInstance(db.tournamentDao()));
        mr = MatchRepository.getInstance(MatchDataSource.getInstance(db.matchDao()));

        Player zach = new Player("Zach","Cutshall","DadMuscles", 700);
        Player jeremiah = new Player("Jeremiah","Trahern","MiLK", 700);;
        Player jacob = new Player("Jacob","Fail","Gompers", 700);;
        Player megan = new Player("Megan","Weir","Nun", 700);;
        Player carly = new Player("Carly","Beck","CARL", 700);;
        Player bill = new Player("Billy","Mushaw","Chancellor", 700);;
        Player mason = new Player("Mason","Short","mas", 700);;
        Player nick = new Player("Nick","Roberts","Zoltar", 700);;
        Player noah = new Player("Noah","Bumgardner","VorceShard", 700);;
        Player doug = new Player("Doug","Jenkins","badman", 700);;
        Player james = new Player("James","Leffert","weedGoku", 700);
        Player blake = new Player("Blake", "Mariot", "Mogg", 700);
        Player dan = new Player("Danny", "Hotson", "2muchDogg", 700);
        Player joel = new Player("Joel", "Schmole", "Dangable", 700);
        Player gabe = new Player("Gabe", "Glaser", "zapper", 700);
        Player britney = new Player("Britney", "Firking", "jamba", 700);
        Player sherene = new Player("Sherene", "Covert", "Sher", 700);
        Player vince = new Player("Vincent", "Terranova", "Lazer", 700);

        players.add(zach);
        players.add(jeremiah);
        players.add(jacob);
        players.add(megan);
        players.add(carly);
        players.add(bill);
        players.add(mason);
        players.add(nick);
        players.add(noah);
        players.add(doug);
        players.add(james);
        players.add(blake);
        players.add(dan);
        players.add(joel);
        players.add(gabe);
        players.add(britney);
        players.add(sherene);
        players.add(vince);

        for(Player p : players) {
            newPlayer(p);
        }

        Tournament sma1 = new Tournament("Smash My Ass 1");
        Tournament sma2 = new Tournament("Smash My Ass 2");
        Tournament sma3 = new Tournament("Smash My Ass 3");
        Tournament smaIW = new Tournament("Smash at Inkwell");
        Tournament smaIWF = new Tournament("Smash at Inkwell Finals");



        tournaments.add(sma1);
        tournaments.add(sma2);
        tournaments.add(sma3);
        tournaments.add(smaIW);
        tournaments.add(smaIWF);

        for(Tournament t : tournaments) {
            newTournament(t);
        }

        finish();
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
                                   Toast.makeText(Dummy.this, "User Added", Toast.LENGTH_SHORT).show();
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Toast.makeText(Dummy.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void newTournament(final Tournament t) {
        Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                tr.insert(t);
                e.onComplete();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer() {
                               @Override
                               public void accept(Object o) throws Exception {
                                   Toast.makeText(Dummy.this, "User Added", Toast.LENGTH_SHORT).show();
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Toast.makeText(Dummy.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
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
}
