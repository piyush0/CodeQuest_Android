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
import com.piotrek.customspinner.CustomSpinner;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChallengeFragment extends Fragment {

    Context context;
    Button chooseOpponentButton;

    CustomSpinner topicsSpinner;
    CustomSpinner numOfQuestionSpinner;

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
        chooseOpponentButton.setEnabled(false);
        numOfQuestionSpinner = (CustomSpinner) view.findViewById(R.id.challenge_fragment_noOfQues_spinner);
        topicsSpinner = (CustomSpinner) view.findViewById(R.id.challenge_fragment_topic_spinner);

    }

    public void initTopicAdapter() {

        final String hintText = "Select a topic...";
        topicsSpinner.initializeStringValues((Topics.getTopics().toArray(new String[Topics.getTopics().size()])), hintText);
        topicsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getSelectedItem().toString().equals(hintText)) {
                    //Nothing to do
                } else {
                    selectedTopic = adapterView.getSelectedItem().toString();
                    if (numOfQuestionsSelected == 0) {

                    } else {
                        chooseOpponentButton.setEnabled(true);
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void initNumOfQuesAdapter() {

        final String hintText = "Select Number of Questions...";
        numOfQuestionSpinner.initializeStringValues(NumberOfOptions.getNumberOfOptions().toArray(new String[NumberOfOptions.getNumberOfOptions().size()]), hintText);
        numOfQuestionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getSelectedItem().toString().equals(hintText)) {
                    //Nothing to do
                } else {
                    numOfQuestionsSelected = Integer.valueOf(adapterView.getSelectedItem().toString());

                    if (selectedTopic == null) {

                    } else {
                        chooseOpponentButton.setEnabled(true);
                    }

                }
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
                intent.putExtra("selectedTopic", selectedTopic);
                intent.putExtra("numOfQuestionsSelected", numOfQuestionsSelected);
                startActivity(intent);
            }
        });

    }


}
