package com.example.zach.smashmyandroid.activities.Tournament;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zach.smashmyandroid.R;
import com.example.zach.smashmyandroid.database.SmaDatabase;
import com.example.zach.smashmyandroid.database.local.DataSource.TournamentDataSource;
import com.example.zach.smashmyandroid.database.local.Repository.TournamentRepository;
import com.example.zach.smashmyandroid.database.local.models.Tournament;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zcuts on 4/6/2018.
 */

public class FragmentTournamentList extends Fragment {

    private ArrayList<Tournament> tournamentList;
    private ArrayAdapter adapter;
    private ListView lvTournaments;
    private FloatingActionButton fab;
    private SmaDatabase smaDb;
    private TournamentRepository tournamentRepository;
    private CompositeDisposable compositeDisposable;

    public static FragmentTournamentList newInstance(ArrayList<Tournament> tournaments) {
        FragmentTournamentList fragment = new FragmentTournamentList();
        Bundle args = new Bundle();
        args.putParcelableArrayList("tournaments", tournaments);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentTournamentList() {
        // required empty constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        compositeDisposable = new CompositeDisposable();

        smaDb = SmaDatabase.getInstance(getActivity());
        tournamentRepository = TournamentRepository.getInstance(TournamentDataSource
                .getInstance(smaDb.tournamentDao()));

        if(getArguments() != null) {
            tournamentList = getArguments().getParcelableArrayList("tournaments");
        }

        adapter = new ArrayAdapter<Tournament>(getActivity(), 0, tournamentList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                Tournament t = tournamentList.get(position);

                if(convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.tournament_list_item, null, false);
                }

                TextView tournamentName = convertView.findViewById(R.id.tournamentName);
                tournamentName.setText(t.getName());


                return convertView;
            }
        };

        loadData();

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.activity_tournament_manager, container, false);

        lvTournaments = rootView.findViewById(R.id.listTournaments);
        registerForContextMenu(lvTournaments);

        lvTournaments.setOnItemClickListener((parent, view, position, id) -> {

            Tournament t = (Tournament) lvTournaments.getItemAtPosition(position);
            Intent i = new Intent(getActivity(), TournamentDetails.class).putExtra("tournament", t);
            startActivity(i);

        });

        fab = rootView.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), NewTournament.class);
                startActivity(i);
            }
        });
        lvTournaments.setAdapter(adapter);
        return rootView;
    }
}
