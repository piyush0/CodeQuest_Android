package com.example.piyush0.questionoftheday.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.piyush0.questionoftheday.R;
import com.example.piyush0.questionoftheday.models.Question;

import cn.refactor.library.SmoothCheckBox;

/**
 * Created by piyush0 on 08/12/16.
 */

public class CheckAnswer {

    public static boolean isCorrect(RecyclerView recyclerView, Question question){
        boolean isCorrectlySolved = true;
        for (int i = 0; i < question.getOptions().size(); i++) {
            View cv = recyclerView.getChildAt(i);
            SmoothCheckBox currentCheckBox = (SmoothCheckBox) cv.findViewById(R.id.list_item_option_checkbox);
            isCorrectlySolved = true;
            if (currentCheckBox.isChecked() != question.getOptions().get(i).isCorrect()) {
                isCorrectlySolved = false;
                break;
            }
        }

        return isCorrectlySolved;
    }
}
