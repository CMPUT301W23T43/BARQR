package com.example.barqrxmls;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import java.util.Map;

/**
 * This class converts from a HashMap where the keys are userNames and the values are the
 * totalPoints for the user under that username.
 *
 * The code for this class was followed from:
 * URL: https://stackoverflow.com/questions/19466757/hashmap-to-listview
 * Website: https://stackoverflow.com
 * Author: https://stackoverflow.com/user/1981118/oleksii-k
 */
public class LeaderboardAdapter extends BaseAdapter {



    private final ArrayList leaderboard;

    /**
     *
     * @param map - the hashmap storing the play usernames with their associated totalScore
     */
    public LeaderboardAdapter(Map<Object, Object> map) {
        leaderboard = new ArrayList();
        leaderboard.addAll(map.entrySet());

    }

    @Override
    /**
     * @returns leaderboard.size() -
     */
    public int getCount() {
        return leaderboard.size();
    }

    @Override
    /**
     * @returns (Map.Entry) leaderboard.get(position) - the position where the hashmap instance
     * will appear in the listview.
     */
    public Object getItem(int position) {
        return (Map.Entry) leaderboard.get(position);
    }

    @Override
    /**
     * Unused currently.
     * @returns 0;
     */
    public long getItemId(int position) {
        return 0;
    }

    @Override
    /**
     * Returns the view with the hashmap instance set to display the username alongside the
     * totalScore for the equivalent username.
     * @returns result
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;

        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_content, parent, false);
        } else {
            result = convertView;
        }
        Map.Entry<Object, Object> item = (Map.Entry<Object, Object>) getItem(position);
        ((TextView) result.findViewById(R.id.username)).setText((CharSequence) item.getKey());
        ((TextView) result.findViewById(R.id.points)).setText((CharSequence) item.getValue().toString());

        return result;
    }
}
