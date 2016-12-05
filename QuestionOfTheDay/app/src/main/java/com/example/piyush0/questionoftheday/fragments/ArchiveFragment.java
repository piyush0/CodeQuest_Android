package com.example.piyush0.questionoftheday.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.piyush0.questionoftheday.R;
import com.example.piyush0.questionoftheday.dummy_utils.DummyQuestion;
import com.example.piyush0.questionoftheday.dummy_utils.Topics;
import com.example.piyush0.questionoftheday.models.Question;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArchiveFragment extends Fragment {

    public static final String TAG = "Archive Fragment";
    Spinner filter;
    RecyclerView recyclerView;
    String selectedFilter;
    ArrayList<Question> questions;

    static Context context;

    public ArchiveFragment() {
        // Required empty public constructor
    }

    public static ArchiveFragment newInstance(Context context) {
        ArchiveFragment fragment = new ArchiveFragment();
        ArchiveFragment.context = context;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_archive, container, false);
        questions = DummyQuestion.getDummyQuestions();
        initViews(view);
        initSpinnerAdapter();
        initRecyclerView(view);
        return view;
    }

    public void initViews(View view) {
        filter = (Spinner) view.findViewById(R.id.fragment_archive_filter_spinner);

    }

    public void initRecyclerView(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_archive_list_questions);
        recyclerView.setAdapter(new ArchiveAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(ArchiveFragment.context));
    }

    public void initSpinnerAdapter() {

        ArrayAdapter<String> topicsAdapter = new ArrayAdapter<String>(ArchiveFragment.context,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                Topics.getTopics());
        topicsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        filter.setAdapter(topicsAdapter);
        filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedFilter = adapterView.getSelectedItem().toString();
//
//
//                Log.d(TAG, "onItemSelected: " + questions.size());
//                ArrayList<Question> dummyQues = DummyQuestion.getDummyQuestions();
//                for (int j = 0; j < dummyQues.size(); j++) {
//                    ArrayList<String> dummyTags = dummyQues.get(j).getTags();
//                    boolean toAdd = false;
//                    for (int k = 0; k < dummyTags.size(); k++) {
//                        if (dummyTags.get(k).equals(selectedFilter)) {
//                            toAdd = true;
//                            break;
//                        }
//                    }
//                    if (toAdd) {
//                        questions.add(dummyQues.get(j));
//                    }
//                }
//
//                Log.d(TAG, "onItemSelected: " + selectedFilter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public class ArchiveViewHolder extends RecyclerView.ViewHolder{

        TextView tv_question_statement;
        public ArchiveViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class ArchiveAdapter extends RecyclerView.Adapter<ArchiveViewHolder>{

        public ArchiveViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = li.inflate(R.layout.list_item_questions_archive, null);

            ArchiveViewHolder archiveViewHolder = new ArchiveViewHolder(view);
            archiveViewHolder.tv_question_statement = (TextView) view.findViewById(R.id.item_list_archive_question_statement);

            return archiveViewHolder;
        }

        public void onBindViewHolder(ArchiveViewHolder holder, int position) {

            holder.tv_question_statement.setText(questions.get(position).getStatement());
        }

        public int getItemCount() {
            Log.d(TAG, "getItemCount: " + questions.size());
            return questions.size();
        }
    }

}
