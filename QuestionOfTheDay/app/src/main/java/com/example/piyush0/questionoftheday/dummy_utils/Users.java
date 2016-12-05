package com.example.piyush0.questionoftheday.dummy_utils;

import com.example.piyush0.questionoftheday.models.User;

import java.util.ArrayList;

/**
 * Created by piyush0 on 05/12/16.
 */

public class Users {

    public static ArrayList<User> getUsers(){
        ArrayList<User> users = new ArrayList<>();

        users.add(new User("Piyush",50));
        users.add(new User("Aayush",30));
        users.add(new User("Ram",70));
        users.add(new User("Akshay",100));
        users.add(new User("Parth",35));


        return users;
    }
}
