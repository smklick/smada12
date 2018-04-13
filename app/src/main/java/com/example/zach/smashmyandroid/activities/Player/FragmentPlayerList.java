package com.example.zach.smashmyandroid.activities.Player;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
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
import com.example.zach.smashmyandroid.local.DataSource.PlayerDataSource;
import com.example.zach.smashmyandroid.local.Repository.PlayerRepository;
import com.example.zach.smashmyandroid.local.models.Player;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

public class FragmentPlayerList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PLAYERS = "playersList";
    private static final String ARG_PARAM2 = "param2";
    static final int NEW_USER = 1;
    static final int EDIT_USER = 2;


    // TODO: Rename and change types of parameters
    private ArrayList<Player> playersList;
    private ArrayAdapter adapter;
    private ListView lvPlayers;
    private SmaDatabase smaDb;
    private PlayerRepository playerRepository;
    private CompositeDisposable compositeDisposable;

    public FragmentPlayerList() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FragmentPlayerList newInstance(ArrayList<Player> players) {
        FragmentPlayerList fragment = new FragmentPlayerList();
        Bundle args = new Bundle();
        args.putParcelableArrayList("playersList", players);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        compositeDisposable = new CompositeDisposable();

        smaDb = SmaDatabase.getInstance(getActivity());
        playerRepository = PlayerRepository.getInstance(PlayerDataSource.getInstance(smaDb.playerDao()));


        if (getArguments() != null) {
            playersList = getArguments().getParcelableArrayList("playersList");
        }

        adapter = new ArrayAdapter<Player>(getActivity(), 0, playersList) {
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

        loadData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.player_list, container, false);

        FloatingActionButton fab = rootView.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), NewPlayer.class);
                startActivity(i);
            }
        });

        lvPlayers = rootView.findViewById(R.id.listPlayers);
        lvPlayers.setAdapter(adapter);
        registerForContextMenu(lvPlayers);


        lvPlayers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Create player object from list data
                Player p = (Player) lvPlayers.getItemAtPosition(position);

                // Add PlayerData parcel to new intent
                Intent i = new Intent(getActivity(), PlayerProfile.class).putExtra("player", p);

                startActivityForResult(i, NEW_USER);

            }
        });

        return rootView;

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle("Select Action:");

        menu.add(Menu.NONE, 0, menu.NONE, "UPDATE");
        menu.add(Menu.NONE, 1, menu.NONE, "DELETE");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Player player = playersList.get(info.position);
        switch (item.getItemId()) {
            case 0: // Update User
            {

                // Add PlayerData parcel to new intent
                Intent i = new Intent(getActivity(), PlayerDetails.class).putExtra("player", player);

                // Start new activity with intent containing player data
                startActivityForResult(i, EDIT_USER);
            }
            break;
            case 1: // Delete User
            {
                new AlertDialog.Builder(getActivity())
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
                                   Toast.makeText(getActivity(), "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void onGetAllPlayersSuccess(List<Player> players) {
        playersList.clear();
        playersList.addAll(players);
        adapter.notifyDataSetChanged();
    }
}
