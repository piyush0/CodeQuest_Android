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
import android.widget.TextView;
import android.widget.Toast;

import com.example.piyush0.questionoftheday.R;
import com.example.piyush0.questionoftheday.services.TimeCountingForGameService;
import com.example.piyush0.questionoftheday.dummy_utils.DummyQuestion;
import com.example.piyush0.questionoftheday.models.Question;
import com.example.piyush0.questionoftheday.utils.CheckAnswer;
import com.example.piyush0.questionoftheday.utils.FontsOverride;
import com.example.piyush0.questionoftheday.utils.InitOptionsSelectedArray;

import java.util.ArrayList;

import cn.refactor.library.SmoothCheckBox;

public class GameActivity extends AppCompatActivity {

    public static final String TAG = "GameActivity";

    private String selectedTopic;
    private Integer numOfQuestionsSelected;
    private ArrayList<String> usersChallenged;

    private ArrayList<Question> questions;
    private GameAdapter gameAdapter;

    private TextView tv_quesStatement, tv_clock_minutes, tv_clock_seconds;
    private RecyclerView list_options;
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
        tv_clock_minutes = (TextView) findViewById(R.id.activity_game_clock_minutes);
        tv_clock_seconds = (TextView) findViewById(R.id.activity_game_clock_seconds);
        tv_quesStatement = (TextView) findViewById(R.id.fragment_question_tv_statement);

        list_options = (RecyclerView) findViewById(R.id.fragment_question_options_list);

        btn_next = (Button) findViewById(R.id.fragment_question_btn_submit);
        btn_next.setText("Next");

        gameAdapter = new GameAdapter();
        list_options.setAdapter(gameAdapter);
        list_options.setLayoutManager(new LinearLayoutManager(this));
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
                optionsSelected = InitOptionsSelectedArray.init(optionsSelected);

                if (isCorrectlySolved) {
                    numCorrect++;
                }

                counter++;

                if (counter == questions.size() - 1) {
                    btn_next.setText("Submit");
                }

                if (counter == questions.size()) { /*Game ended*/

                    stopClock();
                    clearGameSharedPrefs();
                    Toast.makeText(GameActivity.this, "Total correctly solved" + numCorrect + " Time: " + timeForGame, Toast.LENGTH_SHORT).show();
                    clearLocalVars();

                } else {
                    loadNextQuestion();
                }
            }
        });
    }

    private void loadNextQuestion() {
        tv_quesStatement.setText(questions.get(counter).getStatement());
        gameAdapter.notifyDataSetChanged();
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

        /* Loading the appropriate question on the textView when the user comes back. */
        tv_quesStatement.setText(questions.get(sharedPreferences.getInt("counter", 0)).getStatement());
    }

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

    private class GameViewHolder extends RecyclerView.ViewHolder {

        SmoothCheckBox option;
        TextView textView;

        GameViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class GameAdapter extends RecyclerView.Adapter<GameViewHolder> {

        @Override
        public GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = li.inflate(R.layout.list_item_today_options, null);

            GameViewHolder gameViewHolder = new GameViewHolder(view);
            gameViewHolder.option = (SmoothCheckBox) view.findViewById(R.id.list_item_option_checkbox);
            gameViewHolder.textView = (TextView) view.findViewById(R.id.list_item_option_textView);
            return gameViewHolder;
        }

        @Override
        public void onBindViewHolder(final GameViewHolder holder, int position) {
            Log.d(TAG, "onBindViewHolder: " + questions.get(counter).getOptions().get(position).getOption_statement());
            holder.option.setChecked(false);
            holder.textView.setText(questions.get(counter).getOptions().get(position).getOption_statement());
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.option.setChecked(!holder.option.isChecked(), true);
                    if (holder.option.isChecked()) {
                        optionsSelected.set(holder.getAdapterPosition(), true);
                    } else {
                        optionsSelected.set(holder.getAdapterPosition(), false);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {

            return questions.get(counter).getOptions().size();
        }
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