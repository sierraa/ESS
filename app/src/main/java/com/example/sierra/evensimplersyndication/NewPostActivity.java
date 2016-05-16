package com.example.sierra.evensimplersyndication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class NewPostActivity extends AppCompatActivity {

    static final int POST_CHAR_LIMIT = 160;
    static final ArrayList<String> VALID_INTERESTS = new ArrayList<String>(Arrays.asList("python")); // TODO: Add more interests (but not too many more)
    static final String BASE_URL ="http://ec2-54-173-215-12.compute-1.amazonaws.com";
    EditText descField;
    EditText urlField;
    EditText interestsField;
    int USER_ID;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        setTitle("Add Post");
        queue = Volley.newRequestQueue(this);
        // need to unpack user ID
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            USER_ID = extras.getInt("USER_ID");
        }

        Button post = (Button) findViewById(R.id.addNewPost);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                descField = (EditText) findViewById(R.id.descriptionField);
                urlField = (EditText) findViewById(R.id.linkField);
                interestsField = (EditText) findViewById(R.id.tagsField);
                String description = descField.getText().toString();
                String url = urlField.getText().toString();
                String tags = interestsField.getText().toString().toLowerCase();
                // check if it's a valid URL
                String[] interests = parseTags(tags);
                if (isValidUrl(url) && descField.length() < POST_CHAR_LIMIT
                        && hasValidInterests(interests)) {
                    try {
                        addPost(USER_ID, interests, url, description);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    finish();
                } else if (!isValidUrl(url)){
                    urlField.setError(getString(R.string.error_invalid_url));
                } else if (descField.length() > POST_CHAR_LIMIT) {
                    descField.setError(getString(R.string.error_invalid_field_length));
                }
            }
        });

        Button cancel = (Button) findViewById(R.id.cancelPostButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { finish(); }
        });
    }

    private boolean isValidUrl(String url) {
        return URLUtil.isValidUrl(url);
    }

    private boolean hasValidInterests(String[] interests) {
        for (String i : interests) {
            if (!VALID_INTERESTS.contains(i)) {
                interestsField.setError(getString(R.string.error_invalid_tags));
                return false;
            }
        }
        return true;
    }

    private String[] parseTags(String tags) {
        return tags.split(",");
    }

    private void addPost(int userID, String[] tags, String Url, String description) throws JSONException {
        JSONObject jsonRequestBody = new JSONObject();
        jsonRequestBody.put("url", Url);
        jsonRequestBody.put("id", String.valueOf(userID));
        jsonRequestBody.put("description", description);
        jsonRequestBody.put("interests", new JSONArray(tags));

        String requestUrl = BASE_URL + "/addPost";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, requestUrl, jsonRequestBody, new Response.Listener<JSONObject>(){
                    @Override
                          public void onResponse(JSONObject response) {
                              System.out.println("Added post.");
                          }
                      }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.out.println("Could not add post.");
                            }
                });
        queue.add(jsObjRequest);
    }
}

