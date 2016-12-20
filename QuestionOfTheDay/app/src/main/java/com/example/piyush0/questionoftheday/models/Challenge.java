package com.example.piyush0.questionoftheday.models;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by piyush0 on 20/12/16.
 */

public class Challenge {
    ArrayList<User> usersInGame;
    boolean isPending;
    ArrayList<Double> marks;
    Date date;
    String topic;
    Integer challenge_Id;

    public Challenge(ArrayList<User> usersInGame, Date date, String topic) {
        this.usersInGame = usersInGame;
        this.date = date;
        this.topic = topic;
    }

    public String usersChallenged() {
        String rv = "";

        if (usersInGame.size() > 3) {

            for (int i = 0; i < 2; i++) {
                rv += usersInGame.get(i).getName() + ", ";
            }

            rv += usersInGame.get(2).getName() + " and " + (usersInGame.size() - 3) + " others.";
            return rv;

        } else {
            for (int i = 0; i < usersInGame.size() - 1; i++) {
                rv += usersInGame.get(i).getName() + ", ";
            }

            rv += usersInGame.get(usersInGame.size() - 1).getName() + ".";
            return rv;
        }
    }

    @Override
    public String toString() {
        String rv = topic + " Quiz";

        return rv;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<User> getUsersInGame() {
        return usersInGame;
    }

    public void setUsersInGame(ArrayList<User> usersInGame) {
        this.usersInGame = usersInGame;
    }

    public boolean isPending() {
        return isPending;
    }

    public void setPending(boolean pending) {
        isPending = pending;
    }

    public ArrayList<Double> getMarks() {
        return marks;
    }

    public void setMarks(ArrayList<Double> marks) {
        this.marks = marks;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
