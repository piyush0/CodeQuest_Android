package com.example.piyush0.questionoftheday.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.piyush0.questionoftheday.R;
import com.example.piyush0.questionoftheday.utils.FontsOverride;

import de.codecrafters.tableview.TableView;

public class GameResultsActivity extends AppCompatActivity {

    private TextView tv_time;
    private TableView tableView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_results);


        FontsOverride.applyFontForToolbarTitle(this, FontsOverride.FONT_PROXIMA_NOVA);

        initViews();
    }

    private void initViews() {
        tv_time = (TextView) findViewById(R.id.activity_game_results_tv_time);
        tableView = (TableView) findViewById(R.id.activity_game_results_tableView);
    }
}
