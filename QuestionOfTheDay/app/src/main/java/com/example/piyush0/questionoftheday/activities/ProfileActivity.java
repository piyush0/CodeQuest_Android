package com.example.piyush0.questionoftheday.activities;

import android.support.annotation.FloatRange;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.piyush0.questionoftheday.R;
import com.example.piyush0.questionoftheday.utils.FontsOverride;

public class ProfileActivity extends AppCompatActivity {

    ImageView imageView;
    TextView tv_username, tv_name, tv_email, tv_rating;
    RatingBar ratingBar;

    public static final String TAG = "ProfileAct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FontsOverride.applyFontForToolbarTitle(this, FontsOverride.FONT_PROXIMA_NOVA);

        init();
        fetchDetails();


    }

    public void fetchDetails(){
        //TODO
    }

   
    public void init(){

        imageView = (ImageView) findViewById(R.id.activity_profile_picture);
        tv_username = (TextView) findViewById(R.id.activity_profile_username);
        tv_name = (TextView) findViewById(R.id.activity_profile_name);
        tv_email = (TextView) findViewById(R.id.activity_profile_email);
        tv_rating = (TextView) findViewById(R.id.activity_profile_rating);
        tv_rating.setText("75");

        ratingBar = (RatingBar) findViewById(R.id.activity_profile_rating_bar);

        ratingBar.setRating(calculateStars(Integer.valueOf(tv_rating.getText().toString())));

    }

    public float calculateStars(int rating){
        float perc = Float.valueOf(rating)/100;
        Log.d(TAG, "calculateStars: " + perc);
        return perc*5;
    }
}
