package com.example.piyush0.questionoftheday.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.piyush0.questionoftheday.R;
import com.example.piyush0.questionoftheday.fragments.SolveQuestionFragment;
import com.example.piyush0.questionoftheday.services.TimeCountingForGameService;
import com.example.piyush0.questionoftheday.dummy_utils.DummyQuestion;
import com.example.piyush0.questionoftheday.models.Question;
import com.example.piyush0.questionoftheday.utils.CheckAnswer;
import com.example.piyush0.questionoftheday.utils.FontsOverride;
import com.example.piyush0.questionoftheday.utils.InitOptionsSelectedArray;

import java.util.ArrayList;

import cn.refactor.library.SmoothCheckBox;

public class GameActivity extends AppCompatActivity implements SolveQuestionFragment.OnBooleanArrayPass {

    public static final String TAG = "GameActivity";

    private String selectedTopic;
    private Integer numOfQuestionsSelected;
    private ArrayList<String> usersChallenged;

    private ArrayList<Question> questions;
    private TextView tv_clock_minutes, tv_clock_seconds;
    private Button btn_next;

    private long timeForGame;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Handler handler;

    private int counter;
    private int numCorrect;

    private ArrayList<Boolean> optionsSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        FontsOverride.applyFontForToolbarTitle(this, FontsOverride.FONT_PROXIMA_NOVA);

        optionsSelected = InitOptionsSelectedArray.init(optionsSelected);

        getQuestions();
        getIntentExtras();
        initViews();
        setListenerOnButton();
    }

    private void initViews() {

        //TODO: Load the correct question using IDs.
        getSupportFragmentManager().
                beginTransaction().
                replace(R.id.activity_game_frag_container, SolveQuestionFragment.newInstance(0, false, false, "GameActivity")).
                commit();


        tv_clock_minutes = (TextView) findViewById(R.id.activity_game_clock_minutes);
        tv_clock_seconds = (TextView) findViewById(R.id.activity_game_clock_seconds);


        btn_next = (Button) findViewById(R.id.actvity_game_btn_next);
        btn_next.setText("Next");

    }

    private void getQuestions() {
        questions = DummyQuestion.getDummyQuestions();
        //TODO: Get Questions based on number of questions and topic.
    }

    private void setListenerOnButton() {
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isCorrectlySolved = CheckAnswer.isCorrect(optionsSelected, questions.get(counter));
                Log.d(TAG, "onClick: " + isCorrectlySolved);
                optionsSelected = InitOptionsSelectedArray.init(optionsSelected);

                if (isCorrectlySolved) {
                    numCorrect++;
                }

                counter++;

                if (counter == questions.size() - 1) {
                    btn_next.setText("Submit");
                }

                if (counter == questions.size()) { /*Game ended*/
                    Log.d(TAG, "onClick: " + timeForGame);
                    Log.d(TAG, "onClick: " + numCorrect);
                    stopClock();
                    clearGameSharedPrefs();

                    Toast.makeText(GameActivity.this, "Go to My Challenges to see the Results.", Toast.LENGTH_SHORT).show();
                    clearLocalVars();
                    /*TODO: Make API call. You have the following vars:
                    numCorrect, timeForGame*/

                } else {
                    loadNextQuestion();
                }
            }
        });
    }

    private void loadNextQuestion() {
        //TODO: Get next question based on IDs.
        getSupportFragmentManager().
                beginTransaction().replace(R.id.activity_game_frag_container, SolveQuestionFragment.newInstance(0, false, false, "GameActivity")).
                commit();
    }

    private void clearGameSharedPrefs() {
        editor.putLong("timeForGame", 0L);
        editor.putInt("numOfCorrect", 0);
        editor.putInt("counter", 0);
        editor.commit();
    }

    private void clearLocalVars() {
        timeForGame = 0L;
        numCorrect = 0;
        counter = 0;
    }

    private void stopClock() {
        handler.removeCallbacks(runnable);
    }

    private void getIntentExtras() {
        Intent intent = getIntent();
        selectedTopic = intent.getStringExtra("selectedTopic");
        numOfQuestionsSelected = intent.getIntExtra("numOfQuestionsSelected", 0);
        usersChallenged = intent.getStringArrayListExtra("usersChallenged");
    }


    @Override
    public void onResume() {
        super.onResume();

        getSharedPrefs();
        stopTimeCountingService();
        resumeClock();

    }

    //TODO: On back press

    private void resumeClock() {
        handler = new Handler();
        handler.post(runnable);
    }

    private void getSharedPrefs() {
        sharedPreferences = getSharedPreferences(WaitingForApprovalActivity.SHARED_PREF_FOR_GAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        counter = sharedPreferences.getInt("counter", 0);
        numCorrect = sharedPreferences.getInt("numOfCorrect", 0);
        Long zero = 0L;
        timeForGame = sharedPreferences.getLong("timeForGame", zero);
    }

    @Override
    public void onPause() {
        saveLocalvarsToSharedPrefs();
        pauseClock();
        startTimeCountingService();
        super.onPause();
    }

    private void startTimeCountingService() {

        Intent intent = new Intent(this, TimeCountingForGameService.class);
        intent.putExtra("timeForGame", timeForGame);
        startService(intent);
    }

    private void pauseClock() {
        handler.removeCallbacks(runnable);
    }

    private void saveLocalvarsToSharedPrefs() {
        editor.putLong("timeForGame", timeForGame);
        editor.putInt("numOfCorrect", numCorrect);
        editor.putInt("counter", counter);
        editor.commit();
    }

    private void stopTimeCountingService() {
        Intent intent = new Intent(this, TimeCountingForGameService.class);
        stopService(intent);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            TimePair time = beautifyTime(timeForGame);

            tv_clock_minutes.setText(getMinutesString(time));
            tv_clock_seconds.setText(getSecondsString(time));
            timeForGame = timeForGame + 1000;

            handler.postDelayed(this, 1000);
        }
    };


    private String getSecondsString(TimePair time) {
        String secondsString = "";
        if (time.getSeconds() < 10) {
            secondsString = "0" + String.valueOf(time.getSeconds());
        } else {
            secondsString = String.valueOf(time.getSeconds());
        }

        return secondsString;
    }

    private String getMinutesString(TimePair time) {
        String minutesString = "";
        if (time.getMinutes() < 10) {
            minutesString = "0" + String.valueOf(time.getMinutes()) + ": ";
        } else {
            minutesString = String.valueOf(time.getMinutes()) + ": ";
        }

        return minutesString;
    }

    @Override
    public void onBooleanArrayPass(ArrayList<Boolean> optionsSelected) {
        this.optionsSelected = optionsSelected;
        Log.d(TAG, "onBooleanArrayPass: " + this.optionsSelected);
    }

    private TimePair beautifyTime(long miliseconds) {

        TimePair timePair = new TimePair();
        long minutes = (miliseconds / 1000) / 60;
        long seconds = (miliseconds / 1000) % 60;

        timePair.setMinutes(minutes);
        timePair.setSeconds(seconds);
        return timePair;
    }

    private class TimePair {
        long minutes;
        long seconds;

        long getMinutes() {
            return minutes;
        }

        void setMinutes(long minutes) {
            this.minutes = minutes;
        }

        long getSeconds() {
            return seconds;
        }

        void setSeconds(long seconds) {
            this.seconds = seconds;
        }
    }

}