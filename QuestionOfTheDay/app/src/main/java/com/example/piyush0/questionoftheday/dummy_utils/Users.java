package com.example.piyush0.questionoftheday.dummy_utils;

import com.example.piyush0.questionoftheday.models.User;

import java.util.ArrayList;

/**
 * Created by piyush0 on 05/12/16.
 */

public class Users {

    public static ArrayList<User> getUsers(){
        ArrayList<User> users = new ArrayList<>();

        users.add(new User("User1",50));
        users.add(new User("User2",30));
        users.add(new User("User3",70));
        users.add(new User("User4",100));
        users.add(new User("User5",35));


        return users;
    }
}
