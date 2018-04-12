package com.example.zach.smashmyandroid.activities.Tournament;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zach.smashmyandroid.R;
import com.example.zach.smashmyandroid.database.SmaDatabase;
import com.example.zach.smashmyandroid.local.DataSource.TournamentDataSource;
import com.example.zach.smashmyandroid.local.Repository.TournamentRepository;
import com.example.zach.smashmyandroid.local.models.Player;
import com.example.zach.smashmyandroid.local.models.Tournament;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FragmentTournamentList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PLAYERS = "playersList";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private ArrayList<Tournament> tournamentList;
    private ArrayAdapter adapter;
    private ListView lvTournaments;
    private SmaDatabase smaDb;
    private TournamentRepository tournamentRepository;
    private CompositeDisposable compositeDisposable;

    public FragmentTournamentList() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FragmentTournamentList newInstance(ArrayList<Tournament> tournaments) {
        FragmentTournamentList fragment = new FragmentTournamentList();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        compositeDisposable = new CompositeDisposable();

        smaDb = SmaDatabase.getInstance(getActivity());
        tournamentRepository = TournamentRepository.getINSTANCE(TournamentDataSource.getInstance(smaDb.tournamentDao()));


        if (getArguments() != null) {
            tournamentList = getArguments().getParcelableArrayList("tournamentList");
        }

        adapter = new ArrayAdapter<Tournament>(getActivity(), 0, tournamentList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Tournament t = tournamentList.get(position);

                if (convertView == null) {
                    convertView = getLayoutInflater()
                            .inflate(R.layout.tournament_list_item, null, false);
                }
                TextView tournamentName = convertView.findViewById(R.id.tournamentName);

                tournamentName.setText(t.getName());


                return convertView;
            }
        };

        loadData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.player_list, container, false);

        lvTournaments = rootView.findViewById(R.id.listPlayers);
        lvTournaments.setAdapter(adapter);


        lvTournaments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Create player object from list data
                Tournament t = (Tournament) lvTournaments.getItemAtPosition(position);

                // Create a PlayerData parcel
                //PlayerData data = new PlayerData(p.getFirstName(), p.getLastName(), p.getSmashName(), p.getRank());

                // Add PlayerData parcel to new intent
                Intent i = new Intent(getActivity(), TournamentDetails.class).putExtra("tournament", t);

                startActivity(i);

                // Start new activity with intent containing player data
                //startActivityForResult(i, VIEW_USER);

            }
        });

        return rootView;

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
                        Toast.makeText(getActivity(), "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void onGetAllTournamentsSuccess(List<Tournament> tournaments) {
        tournamentList.clear();
        tournamentList.addAll(tournaments);
        adapter.notifyDataSetChanged();
    }
}
