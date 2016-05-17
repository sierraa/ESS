package com.example.sierra.evensimplersyndication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This Activity is to display users other than the current logged in user
 */
public class UserProfileActivity extends AppCompatActivity {
    UserModel displayedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        displayedUser = (UserModel) getIntent().getSerializableExtra("user");
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(displayedUser.getName());
        TextView name = (TextView) findViewById(R.id.userDisplayName);
        name.setText(displayedUser.getName());

        ArrayList<String> arr = new ArrayList<String>(Arrays.asList(displayedUser.getInterests()));
        ListView listView = (ListView) findViewById(R.id.userInterestsList);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, arr);
        listView.setAdapter(arrayAdapter);
    }

}
