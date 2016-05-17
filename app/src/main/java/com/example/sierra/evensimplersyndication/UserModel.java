package com.example.sierra.evensimplersyndication;

import java.io.Serializable;

/**
 * Created by Sierra on 5/16/2016.
 */
public class UserModel implements Serializable {

    private int user_id;
    private String[] interests;
    private String name;

    public UserModel(int user_id, String name, String[] interests) {
        this.user_id = user_id;
        this.name = name;
        this.interests = interests;
    }

    public int getUser_id() { return user_id; }

    public String[] getInterests() { return interests; }

    public String getName() { return name; }

}
