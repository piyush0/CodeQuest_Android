package com.example.piyush0.questionoftheday.dummy_utils;

import com.example.piyush0.questionoftheday.models.Challenge;
import com.example.piyush0.questionoftheday.models.User;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by piyush0 on 20/12/16.
 */

public class DummyChallenges {

    public static ArrayList<Challenge> getDummyChallenges() {

        ArrayList<Challenge> challenges = new ArrayList<>();


        Challenge challenge1 = new Challenge(Users.getUsers(), new Date(2016, 2, 3), "C++");

        User user1 = new User("A", 23, "fns");
        User user2 = new User("B", 24, "fnsfdsf");

        ArrayList<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);


        Challenge challenge2 = new Challenge(users, new Date(2015, 4, 2), "JavaScript");
        challenges.add(challenge1);
        challenges.add(challenge2);

        return challenges;
    }
}
