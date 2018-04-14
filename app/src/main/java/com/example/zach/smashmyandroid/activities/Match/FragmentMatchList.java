package com.example.zach.smashmyandroid.activities.Match;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.zach.smashmyandroid.R;
import com.example.zach.smashmyandroid.database.local.models.Match;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentMatchList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentMatchList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMatchList extends Fragment {

    private ArrayList<Match> matches = new ArrayList<>();
    private ListView lvMatches;
    private OnFragmentInteractionListener mListener;
    private ArrayAdapter adapter;

    public FragmentMatchList() {
        // Required empty public constructor
    }

    public static FragmentMatchList newInstance(ArrayList<Match> matches) {
        FragmentMatchList fragment = new FragmentMatchList();
        Bundle args = new Bundle();
        args.putParcelableArrayList("matches", matches);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            matches = getArguments().getParcelableArrayList("matches");
        }

        adapter = new ArrayAdapter<Match>(getActivity(), 0, matches) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                if(convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.tournament_list_item, null, false);
                }

                return convertView;
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

         ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.match_list, container, false);

        lvMatches = rootView.findViewById(R.id.matchList);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
