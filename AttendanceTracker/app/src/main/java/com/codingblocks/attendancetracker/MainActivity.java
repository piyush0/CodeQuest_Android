package com.codingblocks.attendancetracker;

import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.andtinder.model.CardModel;
import com.andtinder.model.Orientations;
import com.andtinder.view.CardContainer;
import com.andtinder.view.CardStackAdapter;
import com.andtinder.view.SimpleCardStackAdapter;
import com.codingblocks.attendancetracker.models.Batch;
import com.codingblocks.attendancetracker.models.Student;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    CardContainer cardContainer;
    CardStackAdapter customAdapter;
    String selectedBatch;

    LinearLayout background;

    ArrayList<Student> students;
    ArrayList<Student> presentStudents;
    ArrayList<Student> absentStudents;

    Handler handler;

    Spinner spinner;

    public static final String TAG = "MainAct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        background = (LinearLayout) findViewById(R.id.activity_main);
        spinner = (Spinner) findViewById(R.id.spinner_batch);
        initSpinnerAdapter();

        handler = new Handler();

        presentStudents = new ArrayList<>();
        absentStudents = new ArrayList<>();

        cardContainer = (CardContainer) findViewById(R.id.cardContainer);
        cardContainer.setOrientation(Orientations.Orientation.Disordered);

    }

    public void setupCardAdapter() {
        customAdapter = new CardStackAdapter(this) {
            @Override
            protected View getCardView(final int i, CardModel cardModel, View view, ViewGroup viewGroup) {

                final StudentViewHolder studentViewHolder;

                if (view == null) {
                    view = getLayoutInflater().inflate(R.layout.list_item_students, null);
                    studentViewHolder = new StudentViewHolder();

                    studentViewHolder.tv_name = (TextView) view.findViewById(R.id.tv_studentName);
                    studentViewHolder.tv_batch = (TextView) view.findViewById(R.id.tv_batchName);
                    studentViewHolder.iv_photo = (ImageView) view.findViewById(R.id.iv_studentPhoto);

                    view.setTag(studentViewHolder);
                } else {
                    studentViewHolder = (StudentViewHolder) view.getTag();
                }


                studentViewHolder.tv_name.setText(students.get(i).getName());
                studentViewHolder.tv_batch.setText(students.get(i).getBatch());
                //TODO: Set photo

                cardModel.setOnCardDimissedListener(new CardModel.OnCardDimissedListener() {
                    @Override
                    public void onLike() {
                        absentStudents.add(students.get(i));
                        background.setBackgroundColor(Color.RED);
                        handler.postDelayed(run,200);

                    }

                    @Override
                    public void onDislike() {
                        presentStudents.add(students.get(i));
                        background.setBackgroundColor(Color.GREEN);
                        handler.postDelayed(run,200);
                    }
                });

                return view;

            }
        };
    }

    public void initSpinnerAdapter() {

        ArrayList<String> batches = Batch.getDummyBatches();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1,batches);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedBatch = adapterView.getSelectedItem().toString();
                fetchStudent(selectedBatch);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void fetchStudent(String batch) {

        if(batch.equals("Crux")){
            students = new ArrayList<>();
        }
        else {
            students = Student.getDummyStudents();
        }
        setupCardAdapter();
        addDummyCards();
        cardContainer.setAdapter(customAdapter);
        //TODO: Get students based on batch
    }

    public void addDummyCards() {
        for (int i = 0; i < students.size(); i++) {
            customAdapter.add(new CardModel("DummyTitle", "DummyDescription", ContextCompat.getDrawable(MainActivity.this,android.R.drawable.btn_star)));
        }
    }

    private Runnable run = new Runnable() {
        @Override
        public void run() {
            background.setBackgroundColor(Color.WHITE);
        }
    };

    public class StudentViewHolder {
        TextView tv_name;
        TextView tv_batch;
        ImageView iv_photo;
    }




}
