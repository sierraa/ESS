package com.example.sierra.evensimplersyndication;

/**
 * Created by Sierra on 5/16/2016.
 */
public class PostModel {
    private String url;
    private int user_id; // user who posted this
    private String description ;
    private String[] interests;

    public PostModel(String url, int user_id, String description, String[] interests) {
        this.url = url;
        this.user_id = user_id;
        this.description = description;
        this.interests = interests;
    }

    public String getUrl() {
        return url;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getDescription() {
        return description;
    }

    public String[] getInterests() {
        return interests;
    }

}
