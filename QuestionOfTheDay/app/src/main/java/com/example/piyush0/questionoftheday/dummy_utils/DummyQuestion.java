package com.example.piyush0.questionoftheday.dummy_utils;

import com.example.piyush0.questionoftheday.models.Option;
import com.example.piyush0.questionoftheday.models.Question;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by piyush0 on 05/12/16.
 */

public class DummyQuestion {

    public static Question getDummyQuestion(){

        ArrayList<Option> options = new ArrayList<>();

        options.add(new Option("op1",true));
        options.add(new Option("op2",false));
        options.add(new Option("op3",true));
        options.add(new Option("op4",false));


        ArrayList<String> tags = new ArrayList<>();
        tags.add("Java");
        tags.add("CPP");

        Question question = new Question("What is this?",
                options,tags,new Date(2016,12,14));

        return question;
    }

    public static ArrayList<Question> getDummyQuestions(){

        ArrayList<Question> questions = new ArrayList<>();

        ArrayList<Option> options1 = new ArrayList<>();

        options1.add(new Option("op1",true));
        options1.add(new Option("op2",false));
        options1.add(new Option("op3",true));
        options1.add(new Option("op4",false));

        ArrayList<String> tags1 = new ArrayList<>();
        tags1.add("Java");
        tags1.add("CPP");


        ArrayList<Option> options2 = new ArrayList<>();

        options2.add(new Option("op5",true));
        options2.add(new Option("op6",false));
        options2.add(new Option("op7",true));

        ArrayList<String> tags2 = new ArrayList<>();
        tags2.add("Java");
        tags2.add("Python");


        Question question1 = new Question("abcdefghijklmnopqrstuvwxyz?",
                options1,tags1, new Date(2016,3,15));

        Question question2 = new Question("What is 2?",
                options2,tags2,new Date(2015,5,24));

        questions.add(question1);
        questions.add(question2);

        Question question3 = new Question("What is 3?", new ArrayList<Option>(), new ArrayList<String>(),new Date(2015,5,23));
        Question question4 = new Question("What is 4?", new ArrayList<Option>(), new ArrayList<String>(),new Date(2015,5,22));
        Question question5 = new Question("What is 5?", new ArrayList<Option>(), new ArrayList<String>(),new Date(2015,8,24));

        questions.add(question3);
        questions.add(question4);
        questions.add(question5);
        return questions;

    }
}
