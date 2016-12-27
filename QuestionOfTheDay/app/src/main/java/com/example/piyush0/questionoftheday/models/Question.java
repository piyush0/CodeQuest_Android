package com.example.piyush0.questionoftheday.models;

import java.util.ArrayList;
import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by piyush0 on 05/12/16.
 */

public class Question extends RealmObject{

    private String statement;
    private RealmList<Option> options;
    private RealmList<Topic> tags;
    private Integer questionID;
    private Date date_added;

    public Question(String statement, RealmList<Option> options, RealmList<Topic> tags, Date date) {
        this.statement = statement;
        this.options = options;
        this.tags = tags;
        this.date_added = date;
    }

    public Question() {
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

    public RealmList<Option> getOptions() {
        return options;
    }

    public void setOptions(RealmList<Option> options) {
        this.options = options;
    }

    public RealmList<Topic> getTags() {
        return tags;
    }

    public void setTags(RealmList<Topic> tags) {
        this.tags = tags;
    }
}
