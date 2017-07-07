package com.codepath.apps.twitter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.TwitterApplication;
import com.codepath.apps.twitter.TwitterClient;
import com.codepath.apps.twitter.models.TwitterUser;
import com.squareup.picasso.Picasso;

public class ComposeTweetFragment extends DialogFragment {
    private Long inReplyToStatusId = null;
    private String inReplyToScreenName = null;

    private static final int maxChars = 140;
    private static final String TAG = "COMPOSE_TWEET";

    private TextView tvCharsLeft;
    private TwitterClient client;
    private StatusUpdateListener listener;
    private EditText etTweetText;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_compose_tweet, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        userDeets(view);
        charsLimit(view);
        tweetBtn(view);
        return view;
    }

    private void userDeets(final View view) {
        client = TwitterApplication.getRestClient();
        client.getAuthenticatedUser(new TwitterClient.TwitterUserResponseHandler() {
            @Override
            public void onSuccess(TwitterUser user) {
                TextView tvUserScreenName = (TextView) view.findViewById(R.id.tvUserScreenName);
                TextView tvUserName = (TextView) view.findViewById(R.id.tvUserName);
                ImageView ivUserPhoto = (ImageView) view.findViewById(R.id.ivUserPhoto);

                tvUserName.setText(user.getName());
                tvUserScreenName.setText("@" + user.getScreenName());
                Picasso.with(getContext()).load(user.getProfileImageUrl()).into(ivUserPhoto);
            }

            @Override
            public void onFailure(Throwable error) {
                Log.d(TAG, "Error: Could not retrieve user's details", error);
            }
        });
    }

    private void charsLimit(View view) {
        String text = "";
        etTweetText = (EditText) view.findViewById(R.id.etTweetText);

        //adding reply if not null
        if (inReplyToScreenName != null) {
            text = "@" + inReplyToScreenName + " ";
            etTweetText.setText(text);
            etTweetText.setSelection(text.length());
        }
        //text change code
        etTweetText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxChars)});
        etTweetText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int charsLeft = maxChars - s.length();
                tvCharsLeft.setText(String.valueOf(charsLeft));
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing.
            }
        });
        int startingCount = maxChars - text.length();
        tvCharsLeft = (TextView) view.findViewById(R.id.tvCharsLeft);
        tvCharsLeft.setText(String.valueOf(startingCount));
    }

    public void tweetBtn(View view) {
        Button btnTweet = (Button) view.findViewById(R.id.btnTweet);
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = etTweetText.getText().toString();
                Log.d(TAG, "status=" + status);
                client.replyToStatus(status, inReplyToStatusId, new TwitterClient.StatusUpdateResponseHandler() {
                    @Override
                    public void onSuccess() {
                        if (listener != null) {
                            listener.onStatusUpdated();
                        }
                    }

                    @Override
                    public void onFailure(Throwable error) {
                        Log.d(TAG, "Failed to update status", error);
                    }
                });
            }
        });
    }

    public void setListener(StatusUpdateListener listener) {
        this.listener = listener;
    }

    public void setInReplyToStatusId(Long inReplyToStatusId) {
        this.inReplyToStatusId = inReplyToStatusId;
    }

    public void setInReplyToScreenName(String inReplyToScreenName) {
        this.inReplyToScreenName = inReplyToScreenName;
    }

    public interface StatusUpdateListener {

        void onStatusUpdated();

    }
}
