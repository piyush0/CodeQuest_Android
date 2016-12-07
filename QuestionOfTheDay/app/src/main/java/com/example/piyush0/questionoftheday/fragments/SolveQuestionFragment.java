package com.example.piyush0.questionoftheday.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.piyush0.questionoftheday.R;
import com.example.piyush0.questionoftheday.activities.MainActivity;
import com.example.piyush0.questionoftheday.dummy_utils.DummyQuestion;
import com.example.piyush0.questionoftheday.models.Question;


/**
 * A simple {@link Fragment} subclass.
 */
public class SolveQuestionFragment extends Fragment {
    public static final String TAG = "SolveQuesFrag";


    TextView tv_quesStatement;
    RecyclerView recyclerViewOptions;
    Button btn_sumbit;
    Context context;
    Question question;

    Boolean isCorrectlySolved;

    public SolveQuestionFragment() {
        // Required empty public constructor
    }

    public static SolveQuestionFragment newInstance() {
        SolveQuestionFragment fragment = new SolveQuestionFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_solve_question, null);
        initViews(view);

        btn_sumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                for (int i = 0; i < question.getOptions().size(); i++) {
                    View cv = recyclerViewOptions.getChildAt(i);
                    CheckBox currentCheckBox = (CheckBox) cv.findViewById(R.id.list_item_option_checkbox);
                    isCorrectlySolved = true;
                    if (currentCheckBox.isChecked() != question.getOptions().get(i).isCorrect()) {
                        isCorrectlySolved = false;
                        break;
                    }
                }

                if (isCorrectlySolved) {
                    Toast.makeText(context, "Correct", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Incorrect", Toast.LENGTH_SHORT).show();
                }

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_main, ArchiveFragment.newInstance()).commit();
            }

        });

        return view;
    }


    public void initViews(View view) {
        context = getActivity().getBaseContext();
        tv_quesStatement = (TextView) view.findViewById(R.id.fragment_question_tv_statement);
        recyclerViewOptions = (RecyclerView) view.findViewById(R.id.fragment_question_options_list);
        btn_sumbit = (Button) view.findViewById(R.id.fragment_question_btn_submit);
        question = DummyQuestion.getDummyQuestion();
        //TODO: Get this from the id.
        recyclerViewOptions.setAdapter(new OptionAdapter());
        recyclerViewOptions.setLayoutManager(new LinearLayoutManager(context));

        //TODO: Set on click listener to button.

    }

    public class OptionViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkbox;

        public OptionViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class OptionAdapter extends RecyclerView.Adapter<SolveQuestionFragment.OptionViewHolder> {

        @Override
        public SolveQuestionFragment.OptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View convertView = li.inflate(R.layout.list_item_today_options, null);

            SolveQuestionFragment.OptionViewHolder optionViewHolder = new SolveQuestionFragment.OptionViewHolder(convertView);
            optionViewHolder.checkbox = (CheckBox) convertView.findViewById(R.id.list_item_option_checkbox);

            return optionViewHolder;
        }

        @Override
        public void onBindViewHolder(SolveQuestionFragment.OptionViewHolder holder, int position) {
            holder.checkbox.setChecked(false);
            holder.checkbox.setText(question.getOptions().get(position).getOption_statement());
        }


        @Override
        public int getItemCount() {
            return question.getOptions().size();
        }
    }


}
