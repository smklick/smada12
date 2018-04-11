/*
package com.example.zach.smashmyandroid.local.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.zach.smashmyandroid.R;
import com.example.zach.smashmyandroid.activities.Player.PlayerListItemView;
import com.example.zach.smashmyandroid.local.models.Player;

import java.util.ArrayList;
import java.util.List;

*/
/**
 * Created by zcuts on 4/9/2018.
 *//*


public class PlayerAdapter extends ArrayAdapter {

    ArrayList<Player> players = new ArrayList<>();

    public PlayerAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = getLayoutInflater().inflate(R.layout.player_list_item, false);
        }

        */
/*
        View v = convertView;

        if(v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.player_list_item, null);
        }

        Player p = (Player) getItem(position);

        if(p != null) {
            TextView playerName = (TextView) v.findViewById(R.id.playerName);
            TextView smashName = (TextView) v.findViewById(R.id.smashName);
            TextView playerRank = (TextView) v.findViewById(R.id.rank);

            playerName.setText(p.getFirstName() + " " + p.getLastName());
            smashName.setText(p.getSmashName());
            playerRank.setText(p.getRank());

        }

        super.getView(position, convertView, parent);*//*

        return pv;
    }

    public void addItem(Player p) {
        players.add(p);
    }
}
*/
