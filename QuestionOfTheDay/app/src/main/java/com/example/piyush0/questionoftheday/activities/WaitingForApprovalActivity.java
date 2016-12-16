package com.example.piyush0.questionoftheday.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.piyush0.questionoftheday.R;
import com.example.piyush0.questionoftheday.utils.FontsOverride;

import java.util.ArrayList;

public class WaitingForApprovalActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_temp;

    private String selectedTopic;
    private Integer numOfQuestionsSelected;
    private ArrayList<String> usersChallenged;

    private SharedPreferences.Editor editor;

    public static final String TAG = "WaitingForAppAct";
    public static final String SHARED_PREF_FOR_GAME = "SharedPrefsForGame";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_for_approval);

        FontsOverride.applyFontForToolbarTitle(this, FontsOverride.FONT_PROXIMA_NOVA);

        initSharedPrefs();
        initViews();
        getIntentExtras();
        logUsersChallenged();

        btn_temp.setOnClickListener(this);
    }


    private void initViews() {
        btn_temp = (Button) findViewById(R.id.btn_temporary);
    }

    private void logUsersChallenged() {

        for (int i = 0; i < usersChallenged.size(); i++) {
            Log.d(TAG, "onCreate: " + usersChallenged.get(i));
        }
    }

    private void getIntentExtras() {
        Intent intent = getIntent();
        selectedTopic = intent.getStringExtra("selectedTopic");
        numOfQuestionsSelected = intent.getIntExtra("numOfQuestionsSelected", 0);
        usersChallenged = intent.getStringArrayListExtra("usersChallenged");
    }

    private void initSharedPrefs() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_FOR_GAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    @Override
    public void onClick(View v) {
        initGameSharedPrefs();
        sendIntent();
    }

    private void initGameSharedPrefs() {

        editor.putLong("timeForGame", 0L);
        editor.putInt("numOfCorrect", 0);
        editor.commit();
    }

    private void sendIntent() {
        Intent intent = new Intent(WaitingForApprovalActivity.this, GameActivity.class);
        intent.putExtra("selectedTopic", selectedTopic);
        intent.putExtra("numOfQuestionsSelected", numOfQuestionsSelected);
        intent.putExtra("usersChallenged", usersChallenged);
        startActivity(intent);
        finish();
    }
}
