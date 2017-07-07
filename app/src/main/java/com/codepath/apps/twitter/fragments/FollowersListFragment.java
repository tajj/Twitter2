package com.codepath.apps.twitter.fragments;


import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.twitter.TwitterApplication;
import com.codepath.apps.twitter.TwitterClient;
import com.codepath.apps.twitter.constants.Extras;
import com.codepath.apps.twitter.models.TwitterUser;
import com.codepath.apps.twitter.models.UserListResults;

import java.util.List;

public class FollowersListFragment extends UserListFragment {
    private Long nextCursor;
    private TwitterClient client;
    private Long userId;


    public static FollowersListFragment newInstance(Long userId) {
        FollowersListFragment fragment = new FollowersListFragment();
        Bundle args = new Bundle();
        args.putLong(Extras.USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getArguments().getLong(Extras.USER_ID);
        client = TwitterApplication.getRestClient();

        populateWithUsers();
    }

    @Override
    public void populateWithUsers() {
        populateWithUsers(userId);
    }

    public void populateWithUsers(Long userId) {
        this.userId = userId;
        client.getFollowersList(userId, new TwitterClient.UserListResponseHandler() {
            @Override
            public void onSuccess(UserListResults userListResults) {
                nextCursor = userListResults.getNextCursor();

                clear();
                addAll(userListResults.getUsers());

                showLatest();
            }

            @Override
            public void onFailure(Throwable error) {
                logError(error);
            }
        });
    }

    @Override
    public void populateWithMoreUsers() {
        client.getFollowersList(userId, nextCursor, new TwitterClient.UserListResponseHandler() {
            @Override
            public void onSuccess(UserListResults userListResults) {
                nextCursor = userListResults.getNextCursor();
                List<TwitterUser> users = userListResults.getUsers();
                addAll(users.isEmpty() ? users : users.subList(1, users.size())); // the question mark is like a for loop
            }

            @Override
            public void onFailure(Throwable error) {
                logError(error);
            }
        });
    }

    private void logError(Throwable error) {
        Log.d("FOLLOWERS_LIST", "Failed to retrieve following list", error);
    }
}
