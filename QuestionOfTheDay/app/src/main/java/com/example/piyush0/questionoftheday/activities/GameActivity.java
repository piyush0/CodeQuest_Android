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
import com.example.piyush0.questionoftheday.models.Option;
import com.example.piyush0.questionoftheday.services.TimeCountingForGameService;
import com.example.piyush0.questionoftheday.dummy_utils.DummyQuestion;
import com.example.piyush0.questionoftheday.models.Question;
import com.example.piyush0.questionoftheday.utils.CheckAnswer;
import com.example.piyush0.questionoftheday.utils.FontsOverride;
import com.example.piyush0.questionoftheday.utils.InitOptionsSelectedArray;
import com.example.piyush0.questionoftheday.utils.TimeUtil;

import java.sql.Time;
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

    private ArrayList<ArrayList<Integer>> optionsYouSelected;
    private ArrayList<Boolean> correctsAndIncorrects;

    private ArrayList<Boolean> optionsSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        FontsOverride.applyFontForToolbarTitle(this, FontsOverride.FONT_PROXIMA_NOVA);

        optionsSelected = InitOptionsSelectedArray.init(optionsSelected);

        optionsYouSelected = new ArrayList<>();
        correctsAndIncorrects = new ArrayList<>();

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

                ArrayList<Integer> optionsYouSelectedInt = getOptionsYouSelectedInt(optionsSelected, questions.get(counter));
                optionsYouSelected.add(optionsYouSelectedInt);
                boolean isCorrectlySolved = CheckAnswer.isCorrect(optionsSelected, questions.get(counter));


                correctsAndIncorrects.add(isCorrectlySolved);

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


                    Intent intent = new Intent(GameActivity.this, GameResultsActivity.class);
                    intent.putExtra("timeForGame", timeForGame);
                    intent.putExtra("optionsYouSelected", optionsYouSelected);
                    intent.putExtra("correctsAndIncorrects", correctsAndIncorrects);

                    clearLocalVars();

                    startActivity(intent);
                    finish();


                    /*TODO: Make API call. You have the following vars:
                    numCorrect, timeForGame*/

                } else {
                    loadNextQuestion();
                }
            }
        });
    }

    private ArrayList<Integer> getOptionsYouSelectedInt(ArrayList<Boolean> optionsSelectedBool, Question question) {

        ArrayList<Integer> retVal = new ArrayList<>();

        for (int i = 0; i < question.getOptions().size(); i++) {
            if (optionsSelectedBool.get(i)) {
                retVal.add(i);
            }
        }
        Log.d(TAG, "getOptionsYouSelectedInt: " + retVal);
        return retVal;

    }

    private void loadNextQuestion() {
        //TODO: Get next question based on IDs.
        getSupportFragmentManager().
                beginTransaction().replace(R.id.activity_game_frag_container, SolveQuestionFragment.newInstance(counter, false, false, "GameActivity")).
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

            tv_clock_minutes.setText(TimeUtil.getMinutesAndSecond(timeForGame).get(0));
            tv_clock_seconds.setText(TimeUtil.getMinutesAndSecond(timeForGame).get(1));
            timeForGame = timeForGame + 1000;

            handler.postDelayed(this, 1000);
        }
    };


    @Override
    public void onBooleanArrayPass(ArrayList<Boolean> optionsSelected) {
        this.optionsSelected = optionsSelected;
        Log.d(TAG, "onBooleanArrayPass: " + this.optionsSelected);
    }


}