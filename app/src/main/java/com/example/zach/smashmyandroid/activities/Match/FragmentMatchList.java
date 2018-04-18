package com.example.zach.smashmyandroid.activities.Match;

import android.os.Bundle;
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
import com.example.zach.smashmyandroid.database.local.DataSource.MatchDataSource;
import com.example.zach.smashmyandroid.database.local.Repository.MatchRepository;
import com.example.zach.smashmyandroid.database.local.models.Match;
import com.example.zach.smashmyandroid.database.local.models.Tournament;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FragmentMatchList extends Fragment {

    private ArrayList<Match> matchList = new ArrayList<>();
    private ListView lvMatches;
    private ArrayAdapter adapter;
    private Tournament tournament;
    private CompositeDisposable compositeDisposable;

    private MatchRepository matchRepository;
    private SmaDatabase smaDb;

    public FragmentMatchList() {
        // Required empty public constructor
    }

    public static FragmentMatchList newInstance(ArrayList<Match> matches) {
        FragmentMatchList fragment = new FragmentMatchList();
        Bundle args = new Bundle();
        args.putParcelableArrayList("matchList", matches);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        compositeDisposable = new CompositeDisposable();

        if (getArguments() != null) {
            matchList = getArguments().getParcelable("matchList");
        }

        smaDb = SmaDatabase.getInstance(getActivity());
        matchRepository = MatchRepository.getInstance(MatchDataSource.getInstance(smaDb.matchDao()));

        adapter = new ArrayAdapter<Match>(getActivity(), 0, matchList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                Match m = matchList.get(position);

                if(convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.match_list_item, null, false);
                }

                TextView winner = convertView.findViewById(R.id.winnerName);
                TextView loser = convertView.findViewById(R.id.loserName);

                winner.setText(m.getWinnerId());
                loser.setText(m.getLoserId());
                return convertView;
            }
        };
        loadData(tournament);
    }

    private void loadData(Tournament tournament) {
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
                        Toast.makeText(getActivity(), "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void onGetTournamentMatchesSuccess(List<Match> matches) {
        this.matchList.clear();
        this.matchList.addAll(matches);
        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

         ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.match_list, container, false);

        lvMatches = rootView.findViewById(R.id.matchList);
        lvMatches.setAdapter(adapter);
        registerForContextMenu(lvMatches);
        return rootView;
    }

}
