package com.example.piyush0.questionoftheday;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.example.piyush0.questionoftheday.activities.MainActivity;
import com.example.piyush0.questionoftheday.activities.WaitingForApprovalActivity;

public class TimeCountingForGameService extends Service {
    long timeForGame;
    Handler handler;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public static final String TAG = "GameCounting";

    public TimeCountingForGameService() {
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

        sharedPreferences = getSharedPreferences(WaitingForApprovalActivity.SHARED_PREF_FOR_GAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        timeForGame = intent.getLongExtra("timeForGame", 0);
        Log.d(TAG, "onStartCommand: " + timeForGame);
        handler = new Handler();
        handler.post(runnable);

        return START_REDELIVER_INTENT;
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            timeForGame = timeForGame + 1000;

            editor.putLong("timeForGame", timeForGame);
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
