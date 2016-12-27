package com.example.piyush0.questionoftheday.api;

import com.example.piyush0.questionoftheday.models.Question;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by piyush0 on 27/12/16.
 */

public interface QuestionApi {

    @GET("archive")
    Call<ArrayList<Question>> listQuestion();
}
