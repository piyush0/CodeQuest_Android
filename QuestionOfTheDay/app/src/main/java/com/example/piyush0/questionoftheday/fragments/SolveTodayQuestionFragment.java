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
import com.example.piyush0.questionoftheday.utils.Refresh;

import cn.refactor.library.SmoothCheckBox;

/**
 * A simple {@link Fragment} subclass.
 */
public class SolveTodayQuestionFragment extends Fragment {

    Context context;

    public static final String TAG = "TodayQuesFrag";
    public static final String SHARED_PREF_NAME = "TodaySolved";

    TextView tv_question;
    RecyclerView recyclerViewOptions;
    Button submit;
    Handler handler;


    TextView tv_clock_seconds, tv_clock_minutes;
    TextView tv_attemptsRemaining;

    Question todaysQuestion;
    Long timeTaken;
    int attempts;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    FragmentManager fragmentManager;


    boolean isCorrectlySolved;


    public SolveTodayQuestionFragment() {
        // Required empty public constructor
    }

    public static SolveTodayQuestionFragment newInstance() {
        SolveTodayQuestionFragment fragment = new SolveTodayQuestionFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        context = getActivity().getBaseContext();

        View view = inflater.inflate(R.layout.fragment_solve_today_question, null);

        initViews(view);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attempts = sharedPreferences.getInt("attempts", 0);
                attempts++;
                editor.putInt("attempts", attempts);

                tv_attemptsRemaining.setText(String.valueOf(3 - attempts));

                isCorrectlySolved = CheckAnswer.isCorrect(recyclerViewOptions,todaysQuestion);


                if (isCorrectlySolved) {

                    editor.putBoolean("isCorrect", true);

                } else {

                    editor.putBoolean("isCorrect", false);
                }

                editor.commit();

                Refresh.refresh(sharedPreferences, fragmentManager, getContext());
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        stopTimeCountingService();
        editor = sharedPreferences.edit();
        Long zero = 0L;
        timeTaken = sharedPreferences.getLong("timeForTodayQues", zero);
        handler = new Handler();
        handler.post(runnable);
    }

    public void stopTimeCountingService() {
        Intent intent = new Intent(getContext(), TimeCountingService.class);
        context.stopService(intent);
    }

    public void initViews(View view) {
        fragmentManager = getActivity().getSupportFragmentManager();
        tv_question = (TextView) view.findViewById(R.id.fragment_question_tv_statement);
        tv_clock_minutes = (TextView) view.findViewById(R.id.fragment_solve_today_question_minute);
        tv_clock_seconds = (TextView) view.findViewById(R.id.fragment_solve_today_question_second);
        tv_attemptsRemaining = (TextView) view.findViewById(R.id.fragment_solve_today_question_attempts);
        recyclerViewOptions = (RecyclerView) view.findViewById(R.id.fragment_question_options_list);
        recyclerViewOptions.setAdapter(new OptionAdapter());
        recyclerViewOptions.setLayoutManager(new LinearLayoutManager(context));
        submit = (Button) view.findViewById(R.id.fragment_question_btn_submit);
        sharedPreferences = context.getSharedPreferences(MainActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        attempts = sharedPreferences.getInt("attempts", 0);
        tv_attemptsRemaining.setText(String.valueOf(3 - attempts));
        todaysQuestion = DummyQuestion.getDummyQuestion();
        //TODO: Get appropriate question

    }

    public class OptionViewHolder extends RecyclerView.ViewHolder {

        SmoothCheckBox checkbox;
        TextView textView;

        public OptionViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class OptionAdapter extends RecyclerView.Adapter<SolveTodayQuestionFragment.OptionViewHolder> {

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
                    holder.checkbox.setChecked(!holder.checkbox.isChecked(),true);
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
