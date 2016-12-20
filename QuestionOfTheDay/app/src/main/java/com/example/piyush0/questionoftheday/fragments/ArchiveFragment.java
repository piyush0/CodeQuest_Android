package com.example.piyush0.questionoftheday.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.piyush0.questionoftheday.R;
import com.example.piyush0.questionoftheday.dummy_utils.DummyQuestion;
import com.example.piyush0.questionoftheday.models.Question;

import java.util.ArrayList;

public class ArchiveFragment extends Fragment{

    public static final String TAG = "ArchiveFrag";

    private RecyclerView questionsRecyclerView;
    private ArchiveAdapter archiveAdapter;

    private ArrayList<Question> questions;

    private Context context;

    public ArchiveFragment() {
        // Required empty public constructor
    }

    public static ArchiveFragment newInstance() {
        return new ArchiveFragment();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.filter_menu_archive, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_filter) {
            showDialog();
            return true;
        } else if (id == R.id.action_refersh) {
            //TODO: refresh the questions list.
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDialog() {
        FragmentManager fragmentManager = getFragmentManager();
        FilterDialogFragment filterDiagFrag = new FilterDialogFragment();
        filterDiagFrag.show(fragmentManager, "filter");
        filterDiagFrag.setOnSubmitListener(new FilterDialogFragment.OnSubmitListener() {
            @Override
            public void filtersSelected(ArrayList<String> filters, String selectedSort) {
                getQuestion(filters, selectedSort);
            }

        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        initContext();
        setHasOptionsMenu(true); /*This sets the menu.*/
        View view = inflater.inflate(R.layout.fragment_archive, container, false);
        getDefaultQuestionsWithoutFilter();
        initRecyclerView(view);
        return view;
    }

    private void initContext() {
        context = getActivity().getBaseContext();
    }

    private void getDefaultQuestionsWithoutFilter() {
        //TODO: Get Question without filter.
        questions = DummyQuestion.getDummyQuestions();
    }

    private void getQuestion(ArrayList<String> filter, String selectedSort) {
        //TODO: get questions from filter.

        Log.d(TAG, "getQuestion: " + filter);
        Log.d(TAG, "getQuestion: " + selectedSort);
        archiveAdapter.notifyDataSetChanged();
    }

    private void initRecyclerView(View view) {
        questionsRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_archive_list_questions);
        archiveAdapter = new ArchiveAdapter();
        questionsRecyclerView.setAdapter(archiveAdapter);
        questionsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    private class ArchiveViewHolder extends RecyclerView.ViewHolder {

        TextView tv_question_statement;
        CardView cardView;

        ArchiveViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class ArchiveAdapter extends RecyclerView.Adapter<ArchiveViewHolder> {

        public ArchiveViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = li.inflate(R.layout.list_item_questions_archive, null);

            ArchiveViewHolder archiveViewHolder = new ArchiveViewHolder(view);
            archiveViewHolder.tv_question_statement = (TextView) view.findViewById(R.id.item_list_archive_question_statement);
            archiveViewHolder.cardView = (CardView) view.findViewById(R.id.item_list_archive_card);

            return archiveViewHolder;
        }

        public void onBindViewHolder(ArchiveViewHolder holder, final int position) {


            holder.tv_question_statement.setText(questions.get(position).getStatement());

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.
                            beginTransaction().
                            replace(R.id.content_main, SolveQuestionFragment.newInstance(0, true, true, "ArchiveFragment")).
                            commit();

                    //TODO: Set correct id in newInstance parameter.
                }
            });
        }

        public int getItemCount() {

            return questions.size();
        }
    }
}
