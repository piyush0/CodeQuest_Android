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
import com.example.piyush0.questionoftheday.TimeCountingForGameService;
import com.example.piyush0.questionoftheday.dummy_utils.DummyQuestion;
import com.example.piyush0.questionoftheday.models.Question;
import com.example.piyush0.questionoftheday.utils.CheckAnswer;
import com.example.piyush0.questionoftheday.utils.FontsOverride;

import java.util.ArrayList;

import cn.refactor.library.SmoothCheckBox;

public class GameActivity extends AppCompatActivity {

    public static final String TAG = "GameActivity";

    String selectedTopic;
    Integer numOfQuestionsSelected;
    ArrayList<String> usersChallenged;
    ArrayList<Question> questions;
    GameAdapter gameAdapter;


    TextView tv_quesStatement, tv_clock_minutes, tv_clock_seconds;
    RecyclerView list_options;
    Button btn_next;

    long timeForGame;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Handler handler;

    int counter;
    int numCorrect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        FontsOverride.applyFontForToolbarTitle(this, FontsOverride.FONT_PROXIMA_NOVA);


        Intent intent = getIntent();

        getQuestions();
        selectedTopic = intent.getStringExtra("selectedTopic");
        numOfQuestionsSelected = intent.getIntExtra("numOfQuestionsSelected", 0);
        usersChallenged = intent.getStringArrayListExtra("usersChallenged");

        initViews();

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                boolean isCorrectlySolved = CheckAnswer.isCorrect(list_options, questions.get(counter));


                if (isCorrectlySolved) {
                    numCorrect++;
                }

                counter++;

                if (counter == questions.size() - 1) {
                    btn_next.setText("Submit");
                }

                if (counter == questions.size()) {

                    handler.removeCallbacks(runnable);
                    editor.putLong("timeForGame", 0L);
                    editor.putInt("numOfCorrect", 0);
                    editor.putInt("counter", 0);
                    editor.commit();
                    Toast.makeText(GameActivity.this, "Total correctly solved" + numCorrect + " Time: " + timeForGame, Toast.LENGTH_SHORT).show();
                    timeForGame = 0L;
                    numCorrect = 0;
                    counter = 0;
                    Log.d(TAG, "onClick: " + sharedPreferences.getInt("counter", 1000));

                } else {
                    tv_quesStatement.setText(questions.get(counter).getStatement());
                    gameAdapter.notifyDataSetChanged();
                }
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        sharedPreferences = getSharedPreferences(WaitingForApprovalActivity.SHARED_PREF_FOR_GAME, MODE_PRIVATE);
        counter = sharedPreferences.getInt("counter", 0);
        Log.d(TAG, "onResume: " + counter);
        numCorrect = sharedPreferences.getInt("numOfCorrect", 0);
        Long zero = 0L;
        timeForGame = sharedPreferences.getLong("timeForGame", zero);
        stopTimeCountingService();
        editor = sharedPreferences.edit();

        handler = new Handler();
        handler.post(runnable);
        tv_quesStatement.setText(questions.get(sharedPreferences.getInt("counter", 0)).getStatement());
    }

    @Override
    public void onPause() {
        editor.putLong("timeForGame", timeForGame);
        editor.putInt("numOfCorrect", numCorrect);
        editor.putInt("counter", counter);
        editor.commit();

        Log.d(TAG, "onPause: " + sharedPreferences.getInt("counter", 1000));
        handler.removeCallbacks(runnable);
        Intent intent = new Intent(this, TimeCountingForGameService.class);
        intent.putExtra("timeForGame", timeForGame);
        startService(intent);
        super.onPause();
    }

    public void stopTimeCountingService() {
        Intent intent = new Intent(this, TimeCountingForGameService.class);
        stopService(intent);
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            TimePair time = beautifyTime(timeForGame);

            String minutesString = "";
            if (time.getMinutes() < 10) {
                minutesString = "0" + String.valueOf(time.getMinutes()) + ": ";
            } else {
                minutesString = String.valueOf(time.getMinutes()) + ": ";
            }

            String secondsString = "";
            if (time.getSeconds() < 10) {
                secondsString = "0" + String.valueOf(time.getSeconds());
            } else {
                secondsString = String.valueOf(time.getSeconds());
            }

            tv_clock_minutes.setText(minutesString);
            tv_clock_seconds.setText(secondsString);
            timeForGame = timeForGame + 1000;
            handler.postDelayed(this, 1000);
        }
    };


    public void getQuestions() {
        questions = DummyQuestion.getDummyQuestions();
        //TODO: Get Questions based on number of questions.
    }

    public void initViews() {
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

    public class GameViewHolder extends RecyclerView.ViewHolder {

        SmoothCheckBox option;
        TextView textView;

        public GameViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class GameAdapter extends RecyclerView.Adapter<GameViewHolder> {

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
                    holder.option.setChecked(!holder.option.isChecked());
                }
            });
        }

        @Override
        public int getItemCount() {

            return questions.get(counter).getOptions().size();
        }
    }

    public TimePair beautifyTime(long miliseconds) {

        TimePair timePair = new TimePair();
        long minutes = (miliseconds / 1000) / 60;
        long seconds = (miliseconds / 1000) % 60;

        timePair.setMinutes(minutes);
        timePair.setSeconds(seconds);
        return timePair;
    }

    public class TimePair {
        long minutes;
        long seconds;

        public long getMinutes() {
            return minutes;
        }

        public void setMinutes(long minutes) {
            this.minutes = minutes;
        }

        public long getSeconds() {
            return seconds;
        }

        public void setSeconds(long seconds) {
            this.seconds = seconds;
        }
    }

}
