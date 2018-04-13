
package com.example.zach.smashmyandroid.activities.Player;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
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
import com.example.zach.smashmyandroid.database.SmaDatabase;
import com.example.zach.smashmyandroid.local.Repository.PlayerRepository;
import com.example.zach.smashmyandroid.local.DataSource.PlayerDataSource;
import com.example.zach.smashmyandroid.local.models.Player;

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

public class PlayerManager extends AppCompatActivity {

    private ListView lvPlayers;
    private FloatingActionButton addPlayer;

    static final int EDIT_USER = 1;
    static final int NEW_USER = 2;
    static final int VIEW_USER = 3;

    ArrayList<Player> playersList = new ArrayList<>();
    ArrayAdapter adapter;

    private CompositeDisposable compositeDisposable;
    private PlayerRepository playerRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_manager);

        compositeDisposable = new CompositeDisposable();

        lvPlayers = (ListView) findViewById(R.id.listPlayers);
        addPlayer = (FloatingActionButton) findViewById(R.id.fab);

        adapter = new ArrayAdapter<Player>(this, 0, playersList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Player p = playersList.get(position);

                if (convertView == null) {
                    convertView = getLayoutInflater()
                            .inflate(R.layout.player_list_item, null, false);
                }
                TextView playerName = convertView.findViewById(R.id.playerName);
                TextView smashName = convertView.findViewById(R.id.smashName);

                playerName.setText(p.getFirstName() + " " + p.getLastName());
                smashName.setText(p.getSmashName());

                return convertView;
            }
        };
        registerForContextMenu(lvPlayers);
        lvPlayers.setAdapter(adapter);

        SmaDatabase playerDatabase = SmaDatabase.getInstance(this);
        playerRepository = PlayerRepository.getInstance(PlayerDataSource.getInstance(playerDatabase.playerDao()));

        loadData();

        // Launch new user creation activity
        addPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(PlayerManager.this, NewPlayer.class);
                startActivityForResult(i, NEW_USER);

            }
        });

        // Single press on a list item retrieves the object from the list and makes a Toast message with First/Last name
        lvPlayers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Create player object from list data
                Player p = (Player) lvPlayers.getItemAtPosition(position);

                // Create a PlayerData parcel
                //PlayerData data = new PlayerData(p.getFirstName(), p.getLastName(), p.getSmashName(), p.getRank());

                // Add PlayerData parcel to new intent
                Intent i = new Intent(PlayerManager.this, PlayerProfile.class).putExtra("player", p);

                // Start new activity with intent containing player data
                startActivityForResult(i, VIEW_USER);

            }
        });
    }

    //Handlers for various activities that can be launched from this activity.
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == EDIT_USER) {
            if(resultCode == RESULT_OK) {
                Player p = data.getParcelableExtra("player");
                Toast.makeText(this, "Activity Result Recieved: id: " + p.getId() + " " + p.getFirstName() + " " + p.getLastName(), Toast.LENGTH_SHORT).show();
                updatePlayer(p);
                loadData();
            }
        } else if (requestCode == NEW_USER) {
            if(resultCode == RESULT_OK) {
               final Player p = data.getParcelableExtra("player");
                newPlayer(p);

            }
        }
    }

    // Adds a new Player object to the players table
    private void newPlayer(final Player player) {
        Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                playerRepository.insertUser(player);
                e.onComplete();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer() {
                               @Override
                               public void accept(Object o) throws Exception {
                                   Toast.makeText(PlayerManager.this, "User Added", Toast.LENGTH_SHORT).show();
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Toast.makeText(PlayerManager.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           },
                        new Action() {
                            @Override
                            public void run() throws Exception {
                                loadData();
                            }
                        }
                );
        compositeDisposable.add(disposable);
    }

    // Retrieves all users from memory
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
                        Toast.makeText(PlayerManager.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void onGetAllPlayersSuccess(List<Player> players) {
        playersList.clear();
        playersList.addAll(players);
        adapter.notifyDataSetChanged();
    }

    // Create a menu in the header of the activity.
    // See app.res.menu.main_menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // Clicking the clear button deletes all users from the list
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_clear:

                // Add a confirmation message before deletion
                new AlertDialog.Builder(PlayerManager.this)
                        .setTitle("Delete All Players")
                        .setMessage("Are you sure?")
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteAllUsers();
                            }
                        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // Method for deleting all users from list
    private void deleteAllUsers() {
        Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                playerRepository.clearTable();
                e.onComplete();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer() {
                               @Override
                               public void accept(Object o) throws Exception {
                                   Toast.makeText(PlayerManager.this, "User Added", Toast.LENGTH_SHORT).show();
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Toast.makeText(PlayerManager.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           },
                        new Action() {
                            @Override
                            public void run() throws Exception {
                                loadData();
                            }
                        }
                );
        compositeDisposable.add(disposable);
    }

    // Long press on list item creates an options menu with UPDATE or DELETE options
    @Override
    public void onCreateContextMenu(ContextMenu menu,View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle("Select Action:");

        menu.add(Menu.NONE, 0, menu.NONE, "UPDATE");
        menu.add(Menu.NONE, 1, menu.NONE, "DELETE");
    }

    // Selecting UPDATE or DELETE fires the appropriate event
    // UPDATE currently only allows changing of first name
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Player player = playersList.get(info.position);
        switch (item.getItemId()) {
            case 0: // Update User
            {

                // Add PlayerData parcel to new intent
                Intent i = new Intent(PlayerManager.this, PlayerDetails.class).putExtra("player", player);

                // Start new activity with intent containing player data
                startActivityForResult(i, EDIT_USER);
            }
            break;
            case 1: // Delete User
                {
                new AlertDialog.Builder(PlayerManager.this)
                        .setMessage("Do you want to delete "+ player.getFirstName() + " " + player.getLastName())
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deletePlayer(player.getId());
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

    // Remove row form Player table where id matches given id
    private void deletePlayer(final int id) {
        Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                playerRepository.deleteUser(id);
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
                                   Toast.makeText(PlayerManager.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           },
                        new Action() {
                            @Override
                            public void run() throws Exception {
                                loadData();
                            }
                        }
                );
        compositeDisposable.add(disposable);
    }

    // Replaces the Player object in memory with the new one
    private void updatePlayer(final Player player) {
        Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                playerRepository.updatePlayer(player);
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
                                   Toast.makeText(PlayerManager.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           },
                        new Action() {
                            @Override
                            public void run() throws Exception {
                                loadData();
                            }
                        }
                );
        compositeDisposable.add(disposable);
    }

    // Clean up
    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}

