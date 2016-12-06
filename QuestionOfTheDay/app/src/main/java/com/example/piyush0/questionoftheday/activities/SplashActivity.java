package com.example.piyush0.questionoftheday.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.piyush0.questionoftheday.R;
import com.example.piyush0.questionoftheday.utils.FontsOverride;

public class SplashActivity extends AppCompatActivity {


    EditText et_email, et_password;
    Button btn_submit, btn_forgotPass, btn_signUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FontsOverride.applyFontForToolbarTitle(this, FontsOverride.FONT_PROXIMA_NOVA);

        init();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void init(){
        et_email = (EditText) findViewById(R.id.splash_acitivity_et_email);
        et_password = (EditText) findViewById(R.id.splash_activity_et_password);
        btn_submit = (Button) findViewById(R.id.splash_activity_btn_submit);
        btn_forgotPass = (Button) findViewById(R.id.splash_activity_btn_forgotPass);
        btn_signUp = (Button) findViewById(R.id.splash_activity_btn_signUp);

    }
}
