package com.example.piyush0.questionoftheday.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentManager;

import com.example.piyush0.questionoftheday.R;
import com.example.piyush0.questionoftheday.fragments.OOPSFragment;
import com.example.piyush0.questionoftheday.fragments.SolveTodayQuestionFragment;
import com.example.piyush0.questionoftheday.fragments.TipFragment;
import com.example.piyush0.questionoftheday.fragments.YouHaveANewQuesFragment;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by piyush0 on 07/12/16.
 */

public class Refresh {

    public static void refresh(SharedPreferences sharedPreferences, FragmentManager fragmentManager, Context context) {

        boolean isDownloaded = sharedPreferences.getBoolean("isDownloaded", true);
        if (isDownloaded) {

            boolean isOpened = sharedPreferences.getBoolean("isOpened", false);

            if (isOpened) {

                int attempts = sharedPreferences.getInt("attempts", 0);
                boolean isCorrect = sharedPreferences.getBoolean("isCorrect", false);

                if (isCorrect) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_main,
                                    TipFragment.newInstance()).commit();
                } else {
                    if (attempts < 3) {
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_main,
                                        SolveTodayQuestionFragment.newInstance()).commit();
                    } else {
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_main,
                                        OOPSFragment.newInstance()).commit();
                    }
                }
            } else {
                fragmentManager.beginTransaction()
                        .replace(R.id.content_main,
                                YouHaveANewQuesFragment.newInstance()).commit();
            }

        } else {
            if(youHaveInternet(context)){
                clearSharedPref(sharedPreferences);
                download();
                sharedPreferences.edit().putBoolean("isDownloaded", true).commit();
            }
            else {
                //TODO: Please connect to the internet
            }
        }
    }

    public static boolean youHaveTheSameQuestion() {
        //TODO:
        return false;

    }

    public static void download() {
        //TODO:
    }

    public static void clearSharedPref(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Long zero = 0L;
        editor.putLong("timeForTodayQues",zero);
        editor.putBoolean("isOpened",false);
        editor.putInt("attempts",0);
        editor.putBoolean("isCorrect",false);
        editor.commit();
    }


    public static void job(SharedPreferences sharedPreferences, Context context) {

        if (youHaveAQuestion()) {

            if (youHaveInternet(context)) {
                if (youHaveTheSameQuestion()) {
                    sharedPreferences.edit().putBoolean("isDownloaded", true).commit();
                } else {
                    clearSharedPref(sharedPreferences);
                    download();
                    sharedPreferences.edit().putBoolean("isDownloaded", true).commit();
                }
            } else {
                sharedPreferences.edit().putBoolean("isDownloaded", true).commit();
            }

        } else {
            if (youHaveInternet(context)) {
                clearSharedPref(sharedPreferences);
                download();
                sharedPreferences.edit().putBoolean("isDownloaded", true).commit();
            } else {
                sharedPreferences.edit().putBoolean("isDownloaded", false).commit();
            }
        }


    }

    public static boolean youHaveAQuestion() {
        //TODO:
        return true;
    }



    public static boolean youHaveInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }

        return false;
    }
}
