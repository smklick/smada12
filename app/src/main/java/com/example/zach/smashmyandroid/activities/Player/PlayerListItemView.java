package com.example.zach.smashmyandroid.activities.Player;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zach.smashmyandroid.R;

import io.reactivex.annotations.Nullable;

/**
 * Created by zcuts on 4/9/2018.
 */

public class PlayerListItemView extends LinearLayout {

    TextView playerName;
    TextView smashName;
    TextView playerRank;

    public PlayerListItemView(Context context) {
        super(context);
    }

    public PlayerListItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.player_list_item, this, true);

        playerName = (TextView) findViewById(R.id.playerName);
        smashName = (TextView) findViewById(R.id.smashName);
        playerRank = (TextView) findViewById(R.id.rank);

    }

    public void setPlayerName(String name) {
        playerName.setText(name);
    }

    public void setSmashName(String name) {
        smashName.setText(name);
    }

    public void setRank(String rank) {
        playerRank.setText(rank);
    }
}
