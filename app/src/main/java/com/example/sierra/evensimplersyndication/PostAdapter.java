package com.example.sierra.evensimplersyndication;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Sierra on 5/17/2016.
 */
public class PostAdapter extends BaseAdapter {
    static final String TAG =  "PostAdapter";
    private Activity thisActivity;
    private List<PostModel> data;
    private static LayoutInflater inflater = null;
    public Resources res;

    public PostAdapter(Activity act, List<PostModel> arr, Resources resLocal) {
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

        public TextView post;
        public TextView user;
        public TextView interests;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        View rowView;
        rowView = inflater.inflate(R.layout.post_tabitem, null);
        holder.user=(TextView) rowView.findViewById(R.id.userName);
        holder.post=(TextView) rowView.findViewById(R.id.description);
        holder.interests=(TextView) rowView.findViewById(R.id.interests);
        holder.user.setText(data.get(position).getUserName());
        holder.post.setText(data.get(position).getDescription());
        holder.interests.setText(TextUtils.join(", ", data.get(position).getInterests()));
        return rowView;
    }

}


