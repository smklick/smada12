package com.example.zach.smashmyandroid.activities.Match;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.zach.smashmyandroid.R;
import com.example.zach.smashmyandroid.local.models.Match;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zcuts on 4/4/2018.
 */

public class MatchAdapter extends ArrayAdapter<Match> {

    private ArrayList<Match> dataSet;
    Context mContext;

    private static class ViewHolder {
        TextView winner;
        TextView loser;
    }

    public MatchAdapter(ArrayList<Match> data, Context context) {
        super(context, android.R.layout.simple_list_item_1);
        this.dataSet = data;
        this.mContext = context;

    }

    public MatchAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public MatchAdapter(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public MatchAdapter(@NonNull Context context, int resource, @NonNull Match[] objects) {
        super(context, resource, objects);
    }

    public MatchAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull Match[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public MatchAdapter(@NonNull Context context, int resource, @NonNull List<Match> objects) {
        super(context, resource, objects);
    }

    public MatchAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<Match> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Match match = getItem(position);
        ViewHolder viewHolder;

        final View result;

        if(convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.winner = (TextView) convertView.findViewById(R.id.winner);
            viewHolder.loser = (TextView) convertView.findViewById(R.id.loser);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.winner.setText(match.getWinnerId());
        viewHolder.loser.setText(match.getLoserId());

        // Return the completed view to render on screen
        return convertView;

    }
}
