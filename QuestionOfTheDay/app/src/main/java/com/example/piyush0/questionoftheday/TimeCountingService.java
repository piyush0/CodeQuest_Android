package com.example.piyush0.questionoftheday;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;

import com.example.piyush0.questionoftheday.activities.MainActivity;

public class TimeCountingService extends Service {

    long timeTaken;
    Handler handler;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public TimeCountingService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREF_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        timeTaken = intent.getLongExtra("timeForTodayQues", 0);
        handler = new Handler();
        handler.post(runnable);

        return START_REDELIVER_INTENT;
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            timeTaken = timeTaken + 1000;

            editor.putLong("timeForTodayQues", timeTaken);
            editor.commit();

            handler.postDelayed(this, 1000);
        }
    };

    @Override
    public void onDestroy() {

        handler.removeCallbacks(runnable);
        super.onDestroy();
    }
}
