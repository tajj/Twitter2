package com.codepath.apps.twitter.fragments;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.twitter.TwitterApplication;
import com.codepath.apps.twitter.TwitterClient;
import com.codepath.apps.twitter.models.Tweet;

import java.util.List;

public class HomeTimelineFragment extends TweetListFragment {
    private TwitterClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();

        populateTimeline();
    }

    //same as for most fragments
    @Override
    public void populateTimeline() {
        client.getHomeTimeline(new TwitterClient.TimelineResponseHandler() {
            @Override
            public void onSuccess(List<Tweet> tweets) {
                clear();
                addAll(tweets);
                showLatest();
            }

            @Override
            public void onFailure(Throwable error) {
                logError(error);
            }
        });
    }

    private void logError(Throwable error) {Log.d("HOME_TIMELINE", "Failed to retrieve tweets", error);}


    @Override
    public void populateWithOlderTweets(Long oldestTweetId) {
        client.getOlderHomeTimeline(new TwitterClient.TimelineResponseHandler() {
            @Override
            public void onSuccess(List<Tweet> tweets) {
                addAll(tweets.isEmpty() ? tweets : tweets.subList(1, tweets.size()));
            }

            @Override
            public void onFailure(Throwable error) {
                logError(error);
            }
        }, oldestTweetId);
    }


}
