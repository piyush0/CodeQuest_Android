package com.example.piyush0.questionoftheday.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.piyush0.questionoftheday.R;
import com.example.piyush0.questionoftheday.utils.FontsOverride;

import java.util.ArrayList;

public class WaitingForApprovalActivity extends AppCompatActivity {

    Button btn_temp;
    String selectedTopic;
    Integer numOfQuestionsSelected;
    ArrayList<String> usersChallenged;

    public static final String TAG = "WaitingForAppAct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_for_approval);

        FontsOverride.applyFontForToolbarTitle(this, FontsOverride.FONT_PROXIMA_NOVA);
        Intent intent = getIntent();
        btn_temp = (Button) findViewById(R.id.btn_temporary);
        selectedTopic = intent.getStringExtra("selectedTopic");
        numOfQuestionsSelected = intent.getIntExtra("numOfQuestionsSelected",0);
        usersChallenged = intent.getStringArrayListExtra("usersChallenged");


        for(int i = 0 ; i< usersChallenged.size() ; i++){
            Log.d(TAG, "onCreate: " + usersChallenged.get(i));
        }

        btn_temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WaitingForApprovalActivity.this,GameActivity.class);
                intent.putExtra("selectedTopic",selectedTopic);
                intent.putExtra("numOfQuestionsSelected",numOfQuestionsSelected);
                intent.putExtra("usersChallenged",usersChallenged);
                startActivity(intent);

            }
        });

    }
}
