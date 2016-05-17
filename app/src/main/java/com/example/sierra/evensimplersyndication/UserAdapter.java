package com.example.sierra.evensimplersyndication;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sierra on 5/16/2016.
 */
public class UserAdapter extends BaseAdapter {
    static final String TAG =  "UserAdapter";
    private Activity thisActivity;
    private List<UserModel> data;
    private static LayoutInflater inflater = null;
    public Resources res;

    public UserAdapter(Activity act, List<UserModel> arr, Resources resLocal) {
        thisActivity = act;
        data = arr;
        res = resLocal;

        inflater = (LayoutInflater) thisActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public static class ViewHolder {

        public TextView name;
        public TextView interests;
    }

        @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = new ViewHolder();
            View rowView;
            rowView = inflater.inflate(R.layout.tabitem, null);
            holder.name=(TextView) rowView.findViewById(R.id.userName);
            holder.interests=(TextView) rowView.findViewById(R.id.interests);
            holder.name.setText(data.get(position).getName());
            holder.interests.setText(TextUtils.join(", ", data.get(position).getInterests()));
            return rowView;
    }

}
