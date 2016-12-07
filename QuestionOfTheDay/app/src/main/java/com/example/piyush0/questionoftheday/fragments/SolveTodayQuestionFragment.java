package com.example.piyush0.questionoftheday.fragments;


import android.content.Context;
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
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.piyush0.questionoftheday.R;
import com.example.piyush0.questionoftheday.activities.GameActivity;
import com.example.piyush0.questionoftheday.dummy_utils.DummyQuestion;
import com.example.piyush0.questionoftheday.models.Question;
import com.example.piyush0.questionoftheday.utils.CountUpTimer;
import com.example.piyush0.questionoftheday.utils.UtilForRefresh;

import java.util.Calendar;

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

    TextView tv_clock_seconds, tv_clock_minutes;

    Question todaysQuestion;


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
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int attempts = sharedPreferences.getInt("attempts", 0);
                attempts++;
                editor.putInt("attempts", attempts);

                for (int i = 0; i < todaysQuestion.getOptions().size(); i++) {
                    View cv = recyclerViewOptions.getChildAt(i);
                    CheckBox currentCheckBox = (CheckBox) cv.findViewById(R.id.list_item_option_checkbox);
                    isCorrectlySolved = true;
                    if (currentCheckBox.isChecked() != todaysQuestion.getOptions().get(i).isCorrect()) {
                        isCorrectlySolved = false;
                        break;
                    }
                }


                if (isCorrectlySolved) {
                    editor.putBoolean("isCorrect", true);

                } else {
                    editor.putBoolean("isCorrect", false);
                }

                editor.commit();
                UtilForRefresh.refresh(sharedPreferences, fragmentManager);
            }
        });

        //TODO: Remove the callback.


        return view;
    }

    public void initViews(View view) {
        fragmentManager = getActivity().getSupportFragmentManager();
        tv_question = (TextView) view.findViewById(R.id.fragment_question_tv_statement);
        tv_clock_minutes = (TextView) view.findViewById(R.id.fragment_solve_today_question_minute);
        tv_clock_seconds = (TextView) view.findViewById(R.id.fragment_solve_today_question_second);
        recyclerViewOptions = (RecyclerView) view.findViewById(R.id.fragment_question_options_list);
        recyclerViewOptions.setAdapter(new OptionAdapter());
        recyclerViewOptions.setLayoutManager(new LinearLayoutManager(context));
        submit = (Button) view.findViewById(R.id.fragment_question_btn_submit);

        todaysQuestion = DummyQuestion.getDummyQuestion();
        //TODO: Get appropriate question

    }

    public class OptionViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkbox;

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
            optionViewHolder.checkbox = (CheckBox) convertView.findViewById(R.id.list_item_option_checkbox);

            return optionViewHolder;
        }

        @Override
        public void onBindViewHolder(OptionViewHolder holder, int position) {
            holder.checkbox.setChecked(false);
            holder.checkbox.setText(todaysQuestion.getOptions().get(position).getOption_statement());
        }


        @Override
        public int getItemCount() {
            return todaysQuestion.getOptions().size();
        }
    }




}
