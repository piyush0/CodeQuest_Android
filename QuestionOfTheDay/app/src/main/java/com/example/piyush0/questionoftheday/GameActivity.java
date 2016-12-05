package com.example.piyush0.questionoftheday;

import android.content.Context;
import android.content.Intent;
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

import com.example.piyush0.questionoftheday.dummy_utils.DummyQuestion;
import com.example.piyush0.questionoftheday.models.Question;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    public static final String TAG = "GameActivity";

    String selectedTopic;
    Integer numOfQuestionsSelected;
    ArrayList<String> usersChallenged;
    ArrayList<Question> questions;
    GameAdapter gameAdapter;

    TextView tv_quesStatement;
    RecyclerView list_options;
    Button btn_next;

    int counter;
    int numCorrect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        counter = 0;
        numCorrect = 0;

        Intent intent = getIntent();

        getQuestions();
        selectedTopic = intent.getStringExtra("selectedTopic");
        numOfQuestionsSelected = intent.getIntExtra("numOfQuestionsSelected", 0);
        usersChallenged = intent.getStringArrayListExtra("usersChallenged");

        tv_quesStatement = (TextView) findViewById(R.id.activity_game_tv_ques);
        tv_quesStatement.setText(questions.get(0).getStatement());
        list_options = (RecyclerView) findViewById(R.id.activity_game_list_options);
        btn_next = (Button) findViewById(R.id.activity_game_btn_next);
        gameAdapter = new GameAdapter();
        list_options.setAdapter(gameAdapter);
        list_options.setLayoutManager(new LinearLayoutManager(this));

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

                if (counter == questions.size()) {
                    Toast.makeText(GameActivity.this, "Total correctly solved" + numCorrect, Toast.LENGTH_SHORT).show();
                } else {
                    tv_quesStatement.setText(questions.get(counter).getStatement());
                    gameAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    public void getQuestions() {
        questions = DummyQuestion.getDummyQuestions();
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
}
