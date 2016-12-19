package com.example.piyush0.questionoftheday.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.piyush0.questionoftheday.R;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFragment extends Fragment {

    ImageView imageView;
    TextView tv_username, tv_name, tv_email, tv_rating;

    SimpleRatingBar ratingBar;

    public MyProfileFragment() {
        // Required empty public constructor
    }

    public static MyProfileFragment newInstance() {
        MyProfileFragment fragment = new MyProfileFragment();

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        init(view);
        return view;
    }

    public void init(View view) {


        imageView = (ImageView) view.findViewById(R.id.activity_profile_picture);
        tv_username = (TextView) view.findViewById(R.id.activity_profile_username);
        tv_name = (TextView) view.findViewById(R.id.activity_profile_name);
        tv_email = (TextView) view.findViewById(R.id.activity_profile_email);
        tv_rating = (TextView) view.findViewById(R.id.activity_profile_rating);
        tv_rating.setText("75");

        ratingBar = (SimpleRatingBar) view.findViewById(R.id.activity_profile_rating_bar);
        ratingBar.setIndicator(true); /*This doesn't let the user change his rating*/
        ratingBarInit();


    }

    public float calculateStars(int rating) {
        float perc = Float.valueOf(rating) / 100;
        return perc * 5;
    }

    public void ratingBarInit(){
        ratingBar.setStarSize(80);
        ratingBar.setNumberOfStars(5);
        ratingBar.setStepSize(0.1f);
        ratingBar.setBorderColor(Color.BLUE);
        ratingBar.setFillColor(Color.parseColor("#ADD8E6"));
        ratingBar.setStarCornerRadius(10);
        ratingBar.setRating(calculateStars(Integer.valueOf(tv_rating.getText().toString())));


    }

}
