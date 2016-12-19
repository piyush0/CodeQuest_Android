package com.example.piyush0.questionoftheday.fragments;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.piyush0.questionoftheday.R;
import com.example.piyush0.questionoftheday.dummy_utils.Topics;
import com.piotrek.customspinner.CustomSpinner;

import java.util.ArrayList;

import cn.refactor.library.SmoothCheckBox;

/**
 * Created by piyush0 on 09/12/16.
 */

public class FilterDialogFragment extends DialogFragment {

    private OnSubmitListener onSubmitListener;
    FragmentManager fragMan;

    public static final String DATE_SORT = "Date Added";
    public static final String DIFFICULTY_SORT = "Difficulty";

    public void setOnSubmitListener(OnSubmitListener var) {
        this.onSubmitListener = var;
    }

    RecyclerView recyclerView;
    Button btn_submit;
    ArrayList<String> topics;
    CustomSpinner sortBySpinner;
    String selectedSort;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter_dialog, null);
        init(view);
        fragMan = getActivity().getFragmentManager();
        return view;
    }

    public void init(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.filter_dialog_recycler_view);
        btn_submit = (Button) view.findViewById(R.id.filter_dialog_frag_submit);
        btn_submit.setEnabled(false);
        topics = Topics.getTopics();
        recyclerView.setAdapter(new FilterAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        sortBySpinner = (CustomSpinner) view.findViewById(R.id.filter_dialog_frag_sortBy_spinner);
        initSortAdapter();


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> filtersSelected = new ArrayList<String>();

                for (int i = 0; i < topics.size(); i++) {
                    View cv = recyclerView.getChildAt(i);
                    SmoothCheckBox checkBox = (SmoothCheckBox) cv.findViewById(R.id.list_item_filter_checkBox);

                    if (checkBox.isChecked()) {
                        TextView textView = (TextView) cv.findViewById(R.id.list_item_filter_textView);
                        filtersSelected.add(textView.getText().toString());
                    }
                }

                onSubmitListener.filtersSelected(filtersSelected, selectedSort);
                dismiss();
            }
        });
    }

    public void initSortAdapter() {

        ArrayList<String> sorts = new ArrayList<>();
        sorts.add(DATE_SORT);
        sorts.add(DIFFICULTY_SORT);
        final String hintText = "Sort By...";
        sortBySpinner.initializeStringValues(sorts.toArray(new String[sorts.size()]), hintText);
        sortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getSelectedItem().toString().equals(hintText)) {

                } else {
                    selectedSort = adapterView.getSelectedItem().toString();
                    btn_submit.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public void dismiss() {
        super.dismiss();
    }


    public class FilterViewHolder extends RecyclerView.ViewHolder {
        SmoothCheckBox checkBox;
        TextView textView;

        public FilterViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class FilterAdapter extends RecyclerView.Adapter<FilterViewHolder> {

        @Override
        public FilterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View convertView = li.inflate(R.layout.list_item_filters, null);

            FilterViewHolder filterViewHolder = new FilterViewHolder(convertView);
            filterViewHolder.checkBox = (SmoothCheckBox) convertView.findViewById(R.id.list_item_filter_checkBox);
            filterViewHolder.textView = (TextView) convertView.findViewById(R.id.list_item_filter_textView);

            return filterViewHolder;
        }

        @Override
        public void onBindViewHolder(final FilterViewHolder holder, int position) {
            holder.checkBox.setChecked(false);
            holder.textView.setText(topics.get(position));
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.checkBox.setChecked(!holder.checkBox.isChecked(), true);
                }
            });
        }

        @Override
        public int getItemCount() {
            return topics.size();
        }
    }

    public interface OnSubmitListener {
        void filtersSelected(ArrayList<String> filters, String selectedSort);
    }

}
