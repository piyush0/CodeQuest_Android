package com.example.piyush0.questionoftheday.models;

import io.realm.RealmObject;

/**
 * Created by piyush0 on 05/12/16.
 */

public class Option extends RealmObject {

    private String option_statement;
    private boolean isCorrect;

    public Option(String option_statement, boolean isCorrect) {
        this.option_statement = option_statement;
        this.isCorrect = isCorrect;
    }

    public Option() {
    }

    public String getOption_statement() {
        return option_statement;
    }

    public void setOption_statement(String option_statement) {
        this.option_statement = option_statement;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
}
