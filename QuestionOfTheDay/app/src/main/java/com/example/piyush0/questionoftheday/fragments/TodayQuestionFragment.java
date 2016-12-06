package com.example.piyush0.questionoftheday.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.piyush0.questionoftheday.R;
import com.example.piyush0.questionoftheday.dummy_utils.DummyQuestion;
import com.example.piyush0.questionoftheday.models.Question;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TodayQuestionFragment extends Fragment {

    Context context;


    TextView tv_question;
    RecyclerView recyclerViewOptions;
    Button submit;
    LinearLayout linearLayout_MakeDisappear;
    TextView tip;
    TextView oops;

    Question todaysQuestion;



    Boolean isSolved;
    Boolean isCorrectlySolved;

    public TodayQuestionFragment() {
        // Required empty public constructor
    }

    public static TodayQuestionFragment newInstance() {
        TodayQuestionFragment fragment = new TodayQuestionFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        context = getActivity().getBaseContext();

        View view = inflater.inflate(R.layout.fragment_today_question, container, false);

        initViews(view);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSolved = true;

                for(int i = 0 ; i<todaysQuestion.getOptions().size(); i++){
                    View cv = recyclerViewOptions.getChildAt(i);
                    CheckBox currentCheckBox = (CheckBox) cv.findViewById(R.id.list_item_option_checkbox);
                    isCorrectlySolved = true;
                    if(currentCheckBox.isChecked() != todaysQuestion.getOptions().get(i).isCorrect()){
                        isCorrectlySolved = false;
                        break;
                    }
                }

                linearLayout_MakeDisappear.setVisibility(View.GONE);


                if(isCorrectlySolved){
                    tip.setVisibility(View.VISIBLE);
                }
                else{
                    oops.setVisibility(View.VISIBLE);
                }

            }
        });

        return view;
    }

    public void initViews(View view){
        tv_question = (TextView) view.findViewById(R.id.fragment_question_tv_statement);
        recyclerViewOptions = (RecyclerView) view.findViewById(R.id.fragment_question_options_list);
        recyclerViewOptions.setAdapter(new OptionAdapter());
        recyclerViewOptions.setLayoutManager(new LinearLayoutManager(context));
        submit = (Button) view.findViewById(R.id.fragment_question_btn_submit);
        linearLayout_MakeDisappear = (LinearLayout) view.findViewById(R.id.fragment_today_question_layout);
        tip = (TextView) view.findViewById(R.id.fragment_today_tip);
        oops = (TextView) view.findViewById(R.id.fragment_today_oops);
        tip.setVisibility(View.GONE);
        oops.setVisibility(View.GONE);

        todaysQuestion = DummyQuestion.getDummyQuestion();

    }

    public class OptionViewHolder extends RecyclerView.ViewHolder{

        CheckBox checkbox;
        public OptionViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class OptionAdapter extends RecyclerView.Adapter<TodayQuestionFragment.OptionViewHolder>{

        @Override
        public TodayQuestionFragment.OptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View convertView = li.inflate(R.layout.list_item_today_options, null);

            TodayQuestionFragment.OptionViewHolder optionViewHolder = new TodayQuestionFragment.OptionViewHolder(convertView);
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
