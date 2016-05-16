package com.example.sierra.evensimplersyndication;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static final List<String> users = new ArrayList<>();
    static final List<String> posts = new ArrayList<>();
    static final ArrayList<String> userInterests = new ArrayList<>();
    // Model stuff
    private static final String TAG = "MainActivity";
    static String BASE_URL = "http://ec2-54-173-215-12.compute-1.amazonaws.com";
    static int USER_ID = -1;
    static RequestQueue queue;
    private static String MY_DISPLAY_NAME;
    private static String EMAIL;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    public static String getUsername() {
        return MY_DISPLAY_NAME;
    }

    public static String getEmail() {
        return EMAIL;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            USER_ID = extras.getInt("USER_ID");
            MY_DISPLAY_NAME = extras.getString("MY_DISPLAY_NAME");
            EMAIL = extras.getString("EMAIL");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        queue = Volley.newRequestQueue(this);

        setTitle("ESS");

        try {
            getUsers();
            getInterests(USER_ID);
            getPosts(USER_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        final Intent intent = new Intent(this, NewPostActivity.class);
        intent.putExtra("USER_ID", USER_ID);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add a new post", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getPosts(int userID) throws JSONException {
       //  getInterests(userID)
       // /getPostsForUserID
        String url = BASE_URL + "/getPostsForUserID";
        final JSONObject jsonRequestBody = new JSONObject();
        jsonRequestBody.put("id", USER_ID);
        JsonRequest jsObjRequest = new PostObjGetArrayRequest (
                Request.Method.POST, url, jsonRequestBody, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject o = response.getJSONObject(i);
                        Log.i(TAG, "Fetching post " + o.get("post"));
                        posts.add("" + o.get("post"));
                    }
                    Log.i(TAG, "Successfully fetched posts.");
                } catch (JSONException e) {
                    Log.e(TAG, "Getting posts failed with JSONException " + e.toString());
                }

            }} ,new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Getting users failed with error: " + error.toString());
                }
            });
        queue.add(jsObjRequest);
    }

    // Shouldn't return anything since the request is asynchronous
    private void getInterests(final int userID) throws JSONException {
        String getUrl = BASE_URL + "/getInterestsByUserID";
        final JSONObject jsonRequestBody = new JSONObject();
        jsonRequestBody.put("id", userID);
        JsonObjectRequest jsObjRequestGet = new JsonObjectRequest
                (Request.Method.POST, getUrl, jsonRequestBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray interests = response.getJSONArray("interests");
                            for (int i=0;i<interests.length();i++){
                                userInterests.add(interests.get(i).toString());
                            }
                            Log.i(TAG, interests.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Finding interests of user with ID " + userID + " failed. " +
                                "With request JSON: " + jsonRequestBody.toString()
                                + ", and error: " + error.toString());
                    }
                });
        // Add the request to the RequestQueue.
        queue.add(jsObjRequestGet);
    }

    public void getUsers() {
        String url = BASE_URL + "/getUsers";
        final JSONArray jsonRequestBody = new JSONArray();

        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, url, jsonRequestBody, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject o = response.getJSONObject(i);
                                Log.i(TAG, "Adding user " + o.get("name"));
                                users.add("" + o.get("name"));
                            }
                            Log.i(TAG, "Successfully fetched users.");
                        } catch (JSONException e) {
                            Log.e(TAG, "Getting users failed with JSONException " + e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Getting users failed with error: " + error.toString());
                    }
                });

        queue.add(jsObjRequest);
    }

    // Out here to allow android:onClick in my_profile.xml
    public void addInterest(final View view) throws JSONException {
        EditText interestsText = (EditText) findViewById(R.id.addInterest);
        assert interestsText != null;

        String url = BASE_URL + "/addInterest";

        // Parse the comma delimited input
        for (String interest : interestsText.getText().toString().split(",")) {
            final String trimmedInterest = interest.trim();
            if (!trimmedInterest.isEmpty()) {
                final JSONObject jsonRequestBody = new JSONObject();
                jsonRequestBody.put("id", USER_ID);
                jsonRequestBody.put("interest", trimmedInterest);

                JsonObjectRequest jsObjRequest = new JsonObjectRequest
                        (Request.Method.POST, url, jsonRequestBody, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i(TAG, "Added an interest");
                                userInterests.add(trimmedInterest);
                                ((EditText) findViewById(R.id.addInterest)).getText().clear();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e(TAG, "Adding interest \"" + trimmedInterest + "\" failed with error: " + error.toString());
                            }
                        });
                // Add the request to the RequestQueue.
                queue.add(jsObjRequest);
            }
        }
    }

    public static class PostStreamFragmentContainer extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            ListView listView = (ListView) rootView.findViewById(R.id.item_list);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_activated_1, MainActivity.posts);
            listView.setAdapter(arrayAdapter);

            return rootView;
        }

    }

    public static class UsersFragmentContainer extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            ListView listView = (ListView) rootView.findViewById(R.id.item_list);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_activated_1, MainActivity.users);
            listView.setAdapter(arrayAdapter);

            return rootView;
        }

    }

    public static class ProfileFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.my_profile, container, false);

            TextView textView = (TextView) rootView.findViewById(R.id.myUsername);
            textView.setText((MainActivity.getUsername() != null) ? MainActivity.getUsername() : "(no username)");

            ListView listView = (ListView) rootView.findViewById(R.id.listView);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_activated_1, MainActivity.userInterests);
            listView.setAdapter(arrayAdapter);

            return rootView;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return new PostStreamFragmentContainer();
                case 1:
                    return new UsersFragmentContainer();
                case 2:
                    return new ProfileFragment();
            }
            return PlaceholderFragment.newInstance(position); // should never get here
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "POSTS";
                case 1:
                    return "USERS";
                case 2:
                    return "PROFILE";
            }
            return null;
        }
    }

}

