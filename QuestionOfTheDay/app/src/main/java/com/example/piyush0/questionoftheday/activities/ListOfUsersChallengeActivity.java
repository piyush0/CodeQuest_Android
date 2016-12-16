package com.example.piyush0.questionoftheday.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.piyush0.questionoftheday.R;
import com.example.piyush0.questionoftheday.dummy_utils.Users;
import com.example.piyush0.questionoftheday.models.User;
import com.example.piyush0.questionoftheday.utils.FontsOverride;

import java.util.ArrayList;

import cn.refactor.library.SmoothCheckBox;

public class ListOfUsersChallengeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<User> users;
    String selectedTopic;
    Integer numOfQuestionsSelected;
    Button btn_challenge;
    ArrayList<String> usersChallenged;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_users);
        FontsOverride.applyFontForToolbarTitle(this, FontsOverride.FONT_PROXIMA_NOVA);

        getIntentExtras();
        fetchUsers();
        initViews();
        setListenerToButtonChallenge();

    }

    private void setListenerToButtonChallenge() {
        btn_challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < users.size(); i++) {
                    View cv = recyclerView.getChildAt(i);
                    SmoothCheckBox cCheckBox = (SmoothCheckBox) cv.findViewById(R.id.list_item_user_challenge_checkbox);
                    TextView tvName = (TextView) cv.findViewById(R.id.user_list_tv_name);
                    if (cCheckBox.isChecked()) {
                        usersChallenged.add(tvName.getText().toString());
                    }
                }
                sendIntent();
            }
        });
    }

    private void sendIntent() {

        Intent intent = new Intent(ListOfUsersChallengeActivity.this, WaitingForApprovalActivity.class);
        intent.putExtra("selectedTopic", selectedTopic);
        intent.putExtra("numOfQuestionsSelected", numOfQuestionsSelected);
        intent.putExtra("usersChallenged", usersChallenged);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        usersChallenged = new ArrayList<>();
    }

    private void fetchUsers() {
        users = Users.getUsers();
    }

    private void initViews() {

        btn_challenge = (Button) findViewById(R.id.activity_list_of_users_btn_challenege);
        recyclerView = (RecyclerView) findViewById(R.id.activity_challenge_list_of_users);
        recyclerView.setAdapter(new UserAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getIntentExtras() {
        Intent intent = getIntent();
        selectedTopic = intent.getStringExtra("selectedTopic");
        numOfQuestionsSelected = intent.getIntExtra("numOfQuestionsSelected", 0); // 0 is default value.
    }

    private class UserViewHolder extends RecyclerView.ViewHolder {
        LinearLayout outerLayout;
        SmoothCheckBox checkBox;
        ImageView user_image;
        TextView tv_name;
        TextView tv_score;

        UserViewHolder(View itemView) {
            super(itemView);

        }
    }

    public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {

        @Override
        public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater li = getLayoutInflater();


            View convertView = null;

            convertView = li.inflate(R.layout.list_item_user, null);


            UserViewHolder userViewHolder = new UserViewHolder(convertView);
            userViewHolder.tv_name = (TextView) convertView.findViewById(R.id.user_list_tv_name);

            userViewHolder.tv_score = (TextView) convertView.findViewById(R.id.user_list_tv_score);
            userViewHolder.user_image = (ImageView) convertView.findViewById(R.id.user_list_iv_userimage);
            userViewHolder.checkBox = (SmoothCheckBox) convertView.findViewById(R.id.list_item_user_challenge_checkbox);
            userViewHolder.outerLayout = (LinearLayout) convertView.findViewById(R.id.list_item_user_outerLayout);

            return userViewHolder;
        }

        @Override
        public void onBindViewHolder(final UserViewHolder holder, int position) {


            User user = users.get(position);

            holder.tv_name.setText(user.getName());
            holder.tv_score.setText(String.valueOf(user.getScore()));
            String image_url = user.getImage_url();

            holder.outerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.checkBox.setChecked(!holder.checkBox.isChecked(), true);

                }
            });
            //TODO: Set image from url using picasso

        }

        @Override
        public int getItemCount() {
            return users.size();
        }
    }


}