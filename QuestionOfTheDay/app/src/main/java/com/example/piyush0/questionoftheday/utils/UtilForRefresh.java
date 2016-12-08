package com.example.piyush0.questionoftheday.utils;

import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;

import com.example.piyush0.questionoftheday.R;
import com.example.piyush0.questionoftheday.fragments.OOPSFragment;
import com.example.piyush0.questionoftheday.fragments.SolveTodayQuestionFragment;
import com.example.piyush0.questionoftheday.fragments.TipFragment;
import com.example.piyush0.questionoftheday.fragments.YouHaveANewQuesFragment;

/**
 * Created by piyush0 on 07/12/16.
 */

public class UtilForRefresh {

    public static void refresh(SharedPreferences sharedPreferences, FragmentManager fragmentManager) {


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
            //TODO:
        }
    }

    public static boolean youHaveTheSameQuestion() {
        //TODO:
        return false;

    }

    public static void download(){
        //TODO:
    }

    public static void clearSharedPref(){
        //TODO
    }


    /*
    TODO:
    clear shared prefs ->
    timeForTodaysQues
    isOpened
    attempts
    isCorrect
     */
}
