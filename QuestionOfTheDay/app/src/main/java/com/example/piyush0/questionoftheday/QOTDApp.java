package com.example.piyush0.questionoftheday;

import android.app.Application;

import com.example.piyush0.questionoftheday.utils.FontsOverride;

/**
 * Created by piyush0 on 06/12/16.
 */

public class QOTDApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/" + FontsOverride.FONT_PROXIMA_NOVA);
    }
}
