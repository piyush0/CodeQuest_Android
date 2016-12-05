package com.example.piyush0.questionoftheday;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.piyush0.questionoftheday.dummy_utils.Users;
import com.example.piyush0.questionoftheday.models.User;

import java.util.ArrayList;

public class ListOfUsersChallengeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<User> users;
    String selectedTopic;
    Integer numOfQuestionsSelected;

    public static final String TAG = "ListOfUsersActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_users);

        init();

    }

    public void init() {
        Intent intent = getIntent();
        selectedTopic = intent.getStringExtra("selectedTopic");
        numOfQuestionsSelected = intent.getIntExtra("numOfQuestionsSelected", 0); // 0 is default value.
        users = Users.getUsers();
        recyclerView = (RecyclerView) findViewById(R.id.activity_challenge_list_of_users);
        recyclerView.setAdapter(new UserAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        ImageView user_image;
        TextView tv_name;
        TextView tv_score;

        public UserViewHolder(View itemView) {
            super(itemView);

        }
    }

    public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {

        @Override
        public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater li = getLayoutInflater();


            View convertView = null;


            convertView = li.inflate(R.layout.list_item_user, null);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO: Send push notification to opponent. Start Waiting Activity.
                }
            });

            UserViewHolder userViewHolder = new UserViewHolder(convertView);
            userViewHolder.tv_name = (TextView) convertView.findViewById(R.id.user_list_tv_name);
            userViewHolder.tv_score = (TextView) convertView.findViewById(R.id.user_list_tv_score);
            userViewHolder.user_image = (ImageView) convertView.findViewById(R.id.user_list_iv_userimage);

            return userViewHolder;
        }

        @Override
        public void onBindViewHolder(UserViewHolder holder, int position) {


            User user = users.get(position);

            holder.tv_name.setText(user.getName());
            holder.tv_score.setText(String.valueOf(user.getScore()));
            String image_url = user.getImage_url();

            //TODO: Set image from url using picasso

        }

        @Override
        public int getItemCount() {
            return users.size();
        }
    }


}
