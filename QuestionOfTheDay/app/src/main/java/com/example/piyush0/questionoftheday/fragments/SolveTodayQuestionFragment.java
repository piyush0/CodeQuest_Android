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
import android.util.Log;
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
public class SolveTodayQuestionFragment extends Fragment implements SolveQuestionFragment.OnBooleanArrayPass {

    public static final String SHARED_PREF_NAME = "TodaySolved";
    public static final String TAG = "SolveToday";

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
        Log.d(TAG, "SolveTodayQuestionFragment: " + "Empty const");
    }

    public static SolveTodayQuestionFragment newInstance() {
        Log.d(TAG, "newInstance: ");
        return new SolveTodayQuestionFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView: ");
        getTodaysQuestion();
        initContext();
        View view = inflater.inflate(R.layout.fragment_solve_today_question, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();
        initViews(view);
        setClickListenerOnBtn();
        return view;
    }

    private void setClickListenerOnBtn() {
        Log.d(TAG, "setClickListenerOnBtn: ");
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ");
                attempts = sharedPreferences.getInt("attempts", 0);
                attempts++;
                editor.putInt("attempts", attempts);

                tv_attemptsRemaining.setText(String.valueOf(3 - attempts));
                
                Log.d(TAG, "onClick: " + optionsSelected);

                isCorrectlySolved = CheckAnswer.isCorrect(optionsSelected, todaysQuestion);

                Log.d(TAG, "onClick: " + isCorrectlySolved);
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

    @Override
    public void onBooleanArrayPass(ArrayList<Boolean> optionsSelected) {
        Log.d(TAG, "onBooleanArrayPass: ");
        this.optionsSelected = optionsSelected;
    }


    private void initContext() {
        Log.d(TAG, "initContext: ");
        context = getActivity().getBaseContext();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
        initSharedPrefs();
        stopTimeCountingService();
        initClock();
    }

    private void initClock() {
        Log.d(TAG, "initClock: ");
        handler = new Handler();
        handler.post(runnable);
    }

    private void initSharedPrefs() {
        Log.d(TAG, "initSharedPrefs: ");
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Long zero = 0L;
        timeTaken = sharedPreferences.getLong("timeForTodayQues", zero);
    }

    private void stopTimeCountingService() {
        Log.d(TAG, "stopTimeCountingService: ");
        Intent intent = new Intent(getContext(), TimeCountingService.class);
        context.stopService(intent);
    }

    private void initViews(View view) {
        Log.d(TAG, "initViews: ");
        getChildFragmentManager().
                beginTransaction().
                replace(R.id.fragment_solve_today_frag_container, SolveQuestionFragment.newInstance(0, false, true, "SolveTodayQuestionFragment")).
                commit();


        tv_clock_minutes = (TextView) view.findViewById(R.id.fragment_solve_today_question_minute);
        tv_clock_seconds = (TextView) view.findViewById(R.id.fragment_solve_today_question_second);
        tv_attemptsRemaining = (TextView) view.findViewById(R.id.fragment_solve_today_question_attempts);
        btn_submit = (Button) view.findViewById(R.id.fragment_solve_today_btn_sumbit);

        initSharedPrefsOnCreate();

        //TODO: Get appropriate question

    }

    private void getTodaysQuestion() {
        Log.d(TAG, "getTodaysQuestion: ");
        todaysQuestion = DummyQuestion.getDummyQuestion();
    }

    private void initSharedPrefsOnCreate() {
        Log.d(TAG, "initSharedPrefsOnCreate: ");
        sharedPreferences = context.getSharedPreferences(MainActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        attempts = sharedPreferences.getInt("attempts", 0);
        tv_attemptsRemaining.setText(String.valueOf(3 - attempts));
    }



    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "run: ");

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
        Log.d(TAG, "onPause: ");
        saveTimeTillNow();
        stopClock();
        startTimeCountingService();
        super.onPause();
    }

    private void saveTimeTillNow() {
        Log.d(TAG, "saveTimeTillNow: ");
        editor.putLong("timeForTodayQues", timeTaken);
        editor.commit();
    }

    private void stopClock() {
        Log.d(TAG, "stopClock: ");
        handler.removeCallbacks(runnable);
    }

    private void startTimeCountingService() {
        Log.d(TAG, "startTimeCountingService: ");
        Intent intent = new Intent(getContext(), TimeCountingService.class);
        intent.putExtra("timeForTodayQues", timeTaken);
        context.startService(intent);
    }

    private TimePair beautifyTime(long miliseconds) {
        Log.d(TAG, "beautifyTime: ");
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
