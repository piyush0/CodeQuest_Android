package com.example.piyush0.questionoftheday.activities;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.piyush0.questionoftheday.R;
import com.example.piyush0.questionoftheday.dummy_utils.DummyQuestion;
import com.example.piyush0.questionoftheday.models.Question;
import com.example.piyush0.questionoftheday.utils.CountUpTimer;
import com.example.piyush0.questionoftheday.utils.FontsOverride;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    public static final String TAG = "GameActivity";

    String selectedTopic;
    Integer numOfQuestionsSelected;
    ArrayList<String> usersChallenged;
    ArrayList<Question> questions;
    GameAdapter gameAdapter;
    CountUpTimer countUpTimer;

    TextView tv_quesStatement, tv_clock_minutes, tv_clock_seconds;
    RecyclerView list_options;
    Button btn_next;

    long totalTimeTakenToCompleteInMilis;
    long startTime;
    long endTime;

    int counter;
    int numCorrect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        FontsOverride.applyFontForToolbarTitle(this, FontsOverride.FONT_PROXIMA_NOVA);
        startTime = SystemClock.uptimeMillis();
        counter = 0;
        numCorrect = 0;

        Intent intent = getIntent();

        getQuestions();
        selectedTopic = intent.getStringExtra("selectedTopic");
        numOfQuestionsSelected = intent.getIntExtra("numOfQuestionsSelected", 0);
        usersChallenged = intent.getStringArrayListExtra("usersChallenged");

        initViews();
        setClock();
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                boolean isCorrectlySolved = true;
                for (int i = 0; i < questions.get(counter).getOptions().size(); i++) {
                    View cv = list_options.getChildAt(i);
                    CheckBox currentCheckBox = (CheckBox) cv.findViewById(R.id.list_item_option_checkbox);
                    isCorrectlySolved = true;
                    if (currentCheckBox.isChecked() != questions.get(counter).getOptions().get(i).isCorrect()) {
                        isCorrectlySolved = false;
                        break;
                    }
                }


                if (isCorrectlySolved) {
                    numCorrect++;
                }

                counter++;

                if (counter == questions.size() - 1) {
                    btn_next.setText("Submit");
                }

                if (counter == questions.size()) {
                    endTime = SystemClock.uptimeMillis();
                    totalTimeTakenToCompleteInMilis = endTime - startTime;
                    countUpTimer.stop();
                    Toast.makeText(GameActivity.this, "Total correctly solved" + numCorrect + " Time: " + totalTimeTakenToCompleteInMilis, Toast.LENGTH_SHORT).show();
                } else {
                    tv_quesStatement.setText(questions.get(counter).getStatement());
                    gameAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    public void getQuestions() {
        questions = DummyQuestion.getDummyQuestions();
        //TODO: Get Questions based on number of questions.
    }

    public void initViews() {
        tv_clock_minutes = (TextView) findViewById(R.id.activity_game_clock_minutes);
        tv_clock_seconds = (TextView) findViewById(R.id.activity_game_clock_seconds);
        tv_quesStatement = (TextView) findViewById(R.id.fragment_question_tv_statement);
        tv_quesStatement.setText(questions.get(0).getStatement());
        list_options = (RecyclerView) findViewById(R.id.fragment_question_options_list);
        btn_next = (Button) findViewById(R.id.fragment_question_btn_submit);
        btn_next.setText("Next");
        gameAdapter = new GameAdapter();
        list_options.setAdapter(gameAdapter);
        list_options.setLayoutManager(new LinearLayoutManager(this));
    }

    public class GameViewHolder extends RecyclerView.ViewHolder {

        CheckBox option;

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
            gameViewHolder.option = (CheckBox) view.findViewById(R.id.list_item_option_checkbox);

            return gameViewHolder;
        }

        @Override
        public void onBindViewHolder(GameViewHolder holder, int position) {
            Log.d(TAG, "onBindViewHolder: " + questions.get(counter).getOptions().get(position).getOption_statement());
            holder.option.setChecked(false);
            holder.option.setText(questions.get(counter).getOptions().get(position).getOption_statement());
        }

        @Override
        public int getItemCount() {
            Log.d(TAG, "getItemCount: " + questions.get(counter).getOptions().size());
            return questions.get(counter).getOptions().size();
        }
    }

    public void setClock() {

        countUpTimer = new CountUpTimer(1000) {
            @Override
            public void onTick(long elapsedTime) {

                TimePair time = beautifyTime(elapsedTime);

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
            }
        };
        countUpTimer.start();
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
