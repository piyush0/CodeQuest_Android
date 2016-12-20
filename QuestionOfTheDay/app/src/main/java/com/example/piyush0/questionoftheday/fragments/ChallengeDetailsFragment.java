package com.example.piyush0.questionoftheday.fragments;


import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.piyush0.questionoftheday.R;
import com.example.piyush0.questionoftheday.dummy_utils.DummyChallenges;
import com.example.piyush0.questionoftheday.models.Challenge;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChallengeDetailsFragment extends Fragment {

    private Challenge challenge;

    private RecyclerView rv_usersInGame;

    public ChallengeDetailsFragment() {
    }

    public static ChallengeDetailsFragment newInstance(int challengeId) {
        Bundle args = new Bundle();
        args.putInt("challengeId", challengeId);
        ChallengeDetailsFragment challengeDetailsFragment = new ChallengeDetailsFragment();
        challengeDetailsFragment.setArguments(args);
        return challengeDetailsFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_challenge_details, container, false);
        fetchChallenge();
        initViews(view);
        return view;
    }

    private void initViews(View view){
        rv_usersInGame = (RecyclerView) view.findViewById(R.id.fragment_challenge_details_rv_users_in_game);
    }

    private void fetchChallenge() {
        int challengeId = getArguments().getInt("challengeId", 0);
        challenge = DummyChallenges.getDummyChallenges().get(0);
        //TODO: fetch on the basis of challenge id.
    }

    private class UsersInGameViewHolder extends RecyclerView.ViewHolder{


        public UsersInGameViewHolder(View itemView) {
            super(itemView);
        }
    }
}
