package com.example.piyush0.questionoftheday.models;

import com.example.piyush0.questionoftheday.utils.MD5Util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Created by piyush0 on 05/12/16.
 */

public class User {

    String name;
    String image_url;
    String email;
    ArrayList<String> tags;
    Integer score;

    public User(String name, Integer score) {
        this.gravatarSupport();
        this.name = name;
        this.score = score;
    }

    public User(String email, String image_url, String name) {
        this.gravatarSupport();
        this.email = email;
        this.image_url = image_url;
        this.name = name;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        this.gravatarSupport();
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public void gravatarSupport(){

        String hash = MD5Util.md5Hex(email);
        image_url = "https://www.gravatar.com/avatar/" + hash;

    }


}
