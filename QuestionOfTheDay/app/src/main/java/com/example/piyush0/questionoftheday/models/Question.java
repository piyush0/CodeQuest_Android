package com.example.piyush0.questionoftheday.models;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by piyush0 on 05/12/16.
 */

public class Question {

    String statement;
    ArrayList<Option> options;
    ArrayList<String> tags;
    Integer questionID;
    Date date_added;

    public Question(String statement, ArrayList<Option> options, ArrayList<String> tags, Date date) {
        this.statement = statement;
        this.options = options;
        this.tags = tags;
        this.date_added = date;
    }

    public Date getDate_added() {
        return date_added;
    }

    public void setDate_added(Date date_added) {
        this.date_added = date_added;
    }

    public Integer getQuestionID() {
        return questionID;
    }

    public void setQuestionID(Integer questionID) {
        this.questionID = questionID;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public ArrayList<Option> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<Option> options) {
        this.options = options;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }
}
