package com.example.piyush0.questionoftheday.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.piyush0.questionoftheday.R;
import com.example.piyush0.questionoftheday.utils.FontsOverride;

import java.util.ArrayList;

public class WaitingForApprovalActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_lets_go;
    private Button btn_temp;
    private LinearLayout layout_loading, layout_loading_done;

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

        btn_lets_go.setOnClickListener(this);
    }

    private void onLoadingDone() {
        btn_temp.setVisibility(View.GONE);
        layout_loading_done.setVisibility(View.VISIBLE);
        layout_loading.setVisibility(View.GONE);
        btn_lets_go.setVisibility(View.VISIBLE);
    }

    private void initViews() {
        btn_lets_go = (Button) findViewById(R.id.activity_waiting_for_approval_btn_letsGo);
        layout_loading = (LinearLayout) findViewById(R.id.activity_waiting_for_approval_waiting_layout);
        layout_loading_done = (LinearLayout) findViewById(R.id.activity_waiting_for_approval_loading_done_layout);
        layout_loading_done.setVisibility(View.GONE);
        btn_lets_go.setVisibility(View.GONE);

        btn_temp = (Button) findViewById(R.id.btn_temp);
        btn_temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoadingDone();
            }
        });
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
