package com.example.piyush0.questionoftheday.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.piyush0.questionoftheday.R;
import com.example.piyush0.questionoftheday.activities.ListOfUsersChallengeActivity;
import com.example.piyush0.questionoftheday.dummy_utils.NumberOfOptions;
import com.example.piyush0.questionoftheday.dummy_utils.Topics;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChallengeFragment extends Fragment {

    public static final String TAG = "ChallengeFragment";

    Context context;
    Button chooseOpponentButton;
    Spinner topicsSpinner;
    Spinner numOfQuestionSpinner;

    String selectedTopic;
    Integer numOfQuestionsSelected;

    public ChallengeFragment() {
        // Required empty public constructor
    }

    public static ChallengeFragment newInstance() {
        ChallengeFragment fragment = new ChallengeFragment();

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity().getBaseContext();
        View view = inflater.inflate(R.layout.fragment_challenge, container, false);
        initViews(view);
        initButtonListener();
        initTopicAdapter();
        initNumOfQuesAdapter();


        return view;
    }

    public void initViews(View view) {

        chooseOpponentButton = (Button) view.findViewById(R.id.challenge_fragment_opponent_button);
        numOfQuestionSpinner = (Spinner) view.findViewById(R.id.challenge_fragment_noOfQues_spinner);
        topicsSpinner = (Spinner) view.findViewById(R.id.challenge_fragment_topic_spinner);

    }

    public void initTopicAdapter() {
        ArrayAdapter<String> topicsAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                Topics.getTopics());


        topicsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        topicsSpinner.setAdapter(topicsAdapter);
        topicsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedTopic = adapterView.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void initNumOfQuesAdapter() {
        ArrayAdapter<Integer> numOfQuesAdapter = new ArrayAdapter<Integer>(context,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                NumberOfOptions.getNumberOfOptions());


        numOfQuesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        numOfQuestionSpinner.setAdapter(numOfQuesAdapter);
        numOfQuestionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                numOfQuestionsSelected = Integer.valueOf(adapterView.getSelectedItem().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void initButtonListener() {
        chooseOpponentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ListOfUsersChallengeActivity.class);
                intent.putExtra("selectedTopic",selectedTopic);
                intent.putExtra("numOfQuestionsSelected",numOfQuestionsSelected);
                startActivity(intent);
            }
        });

    }


}
