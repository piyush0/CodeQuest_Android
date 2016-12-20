package com.example.piyush0.questionoftheday.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.piyush0.questionoftheday.R;
import com.example.piyush0.questionoftheday.services.TimeCountingService;
import com.example.piyush0.questionoftheday.activities.MainActivity;
import com.example.piyush0.questionoftheday.dummy_utils.DummyQuestion;
import com.example.piyush0.questionoftheday.models.Question;
import com.example.piyush0.questionoftheday.utils.CheckAnswer;
import com.example.piyush0.questionoftheday.utils.InitOptionsSelectedArray;
import com.example.piyush0.questionoftheday.utils.Refresh;

import java.util.ArrayList;

import cn.refactor.library.SmoothCheckBox;

/**
 * A simple {@link Fragment} subclass.
 */
public class SolveTodayQuestionFragment extends Fragment {

    public static final String SHARED_PREF_NAME = "TodaySolved";

    private Context context;

    private TextView tv_question, tv_attemptsRemaining, tv_clock_seconds, tv_clock_minutes;
    private RecyclerView recyclerViewOptions;
    private Button btn_submit;

    private Handler handler; /*This is being used to calculate the time*/

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private FragmentManager fragmentManager;

    private Question todaysQuestion;

    private boolean isCorrectlySolved;
    private Long timeTaken;
    private int attempts;
    private ArrayList<Boolean> optionsSelected;

    public SolveTodayQuestionFragment() {
    }

    public static SolveTodayQuestionFragment newInstance() {
        return new SolveTodayQuestionFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        initContext();
        View view = inflater.inflate(R.layout.fragment_solve_today_question, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();
        initViews(view);
        optionsSelected = InitOptionsSelectedArray.init(optionsSelected);
        setClickListenerOnBtn();

        return view;
    }

    private void setClickListenerOnBtn() {
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attempts = sharedPreferences.getInt("attempts", 0);
                attempts++;
                editor.putInt("attempts", attempts);

                tv_attemptsRemaining.setText(String.valueOf(3 - attempts));

                isCorrectlySolved = CheckAnswer.isCorrect(optionsSelected, todaysQuestion);


                if (isCorrectlySolved) {

                    editor.putBoolean("isCorrect", true);

                } else {

                    editor.putBoolean("isCorrect", false);
                }

                editor.commit();

                Refresh.refresh(sharedPreferences, fragmentManager, getContext());
            }
        });
    }

    private void initContext() {
        context = getActivity().getBaseContext();
    }

    @Override
    public void onResume() {
        super.onResume();
        initSharedPrefs();
        stopTimeCountingService();
        initClock();
    }

    private void initClock() {
        handler = new Handler();
        handler.post(runnable);
    }

    private void initSharedPrefs() {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Long zero = 0L;
        timeTaken = sharedPreferences.getLong("timeForTodayQues", zero);
    }

    private void stopTimeCountingService() {
        Intent intent = new Intent(getContext(), TimeCountingService.class);
        context.stopService(intent);
    }

    private void initViews(View view) {

        tv_question = (TextView) view.findViewById(R.id.fragment_question_tv_statement);
        tv_clock_minutes = (TextView) view.findViewById(R.id.fragment_solve_today_question_minute);
        tv_clock_seconds = (TextView) view.findViewById(R.id.fragment_solve_today_question_second);
        tv_attemptsRemaining = (TextView) view.findViewById(R.id.fragment_solve_today_question_attempts);
        recyclerViewOptions = (RecyclerView) view.findViewById(R.id.fragment_question_options_list);
        recyclerViewOptions.setAdapter(new OptionAdapter());
        recyclerViewOptions.setLayoutManager(new LinearLayoutManager(context));
        btn_submit = (Button) view.findViewById(R.id.fragment_question_btn_submit);
        sharedPreferences = context.getSharedPreferences(MainActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        attempts = sharedPreferences.getInt("attempts", 0);
        tv_attemptsRemaining.setText(String.valueOf(3 - attempts));
        todaysQuestion = DummyQuestion.getDummyQuestion();
        //TODO: Get appropriate question

    }

    private class OptionViewHolder extends RecyclerView.ViewHolder {

        SmoothCheckBox checkbox;
        TextView textView;

        OptionViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class OptionAdapter extends RecyclerView.Adapter<SolveTodayQuestionFragment.OptionViewHolder> {

        @Override
        public SolveTodayQuestionFragment.OptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View convertView = li.inflate(R.layout.list_item_today_options, null);

            SolveTodayQuestionFragment.OptionViewHolder optionViewHolder = new SolveTodayQuestionFragment.OptionViewHolder(convertView);
            optionViewHolder.checkbox = (SmoothCheckBox) convertView.findViewById(R.id.list_item_option_checkbox);
            optionViewHolder.textView = (TextView) convertView.findViewById(R.id.list_item_option_textView);
            return optionViewHolder;
        }

        @Override
        public void onBindViewHolder(final OptionViewHolder holder, int position) {
            holder.checkbox.setChecked(false);
            holder.textView.setText(todaysQuestion.getOptions().get(position).getOption_statement());
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.checkbox.setChecked(!holder.checkbox.isChecked(), true);

                    if (holder.checkbox.isChecked()) {
                        optionsSelected.set(holder.getAdapterPosition(), true);
                    } else {
                        optionsSelected.set(holder.getAdapterPosition(), false);
                    }
                }
            });
        }


        @Override
        public int getItemCount() {
            return todaysQuestion.getOptions().size();
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            TimePair time = beautifyTime(timeTaken);

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
            timeTaken = timeTaken + 1000;
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    public void onPause() {
        editor.putLong("timeForTodayQues", timeTaken);
        editor.commit();
        handler.removeCallbacks(runnable);
        Intent intent = new Intent(getContext(), TimeCountingService.class);
        intent.putExtra("timeForTodayQues", timeTaken);
        context.startService(intent);
        super.onPause();
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
