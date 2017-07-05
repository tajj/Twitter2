package com.codepath.apps.twitter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.TwitterApplication;
import com.codepath.apps.twitter.TwitterClient;
import com.codepath.apps.twitter.constants.Extras;
import com.codepath.apps.twitter.fragments.ComposeTweetFragment;
import com.codepath.apps.twitter.models.Entities;
import com.codepath.apps.twitter.models.Media;
import com.codepath.apps.twitter.models.Tweet;
import com.codepath.apps.twitter.models.TwitterUser;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TweetDetailsActivity extends BaseActivity implements ComposeTweetFragment.StatusUpdateListener {
    private static final String TAG = "TWEET_DETAILS";

    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
    private static final SimpleDateFormat STRING_FORMATTER = new SimpleDateFormat("hh:mm a - dd MMM yyyy");
    private ComposeTweetFragment composeTweetFragment;
    //private TextView tvRetweetCount;
    private Tweet tweet;
    private ImageView ivFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);


        Long id = getIntent().getLongExtra(Extras.TWEET_ID, -1L);
        TwitterApplication.getRestClient().getStatus(id, new TwitterClient.TweetResponseHandler() {
            @Override
            public void onSuccess(Tweet tweet) {
                TweetDetailsActivity.this.tweet = tweet;
                tweetDetails(tweet);
                favoriteButton(tweet);
                retweetButton(tweet);
                setupMedia(tweet);
            }

            @Override
            public void onFailure(Throwable error) {
                Log.e(TAG, "Failed to fetch tweet", error);
            }
        });
    }

    @Override
    protected void showAuthenticatedUserProfile() {
        Intent i = new Intent(TweetDetailsActivity.this, ProfileActivity.class);
        i.putExtra(Extras.USER_ID, authenticatedUser.getId());
        startActivity(i);
    }

    @Override
    protected void showSearchResults(String query) {
        Intent i = new Intent(this, SearchActivity.class);
        i.putExtra(Extras.QUERY, query);
        startActivity(i);
    }

    @Override
    protected String getTag() {
        return TAG;
    }

    private void setupMedia(Tweet tweet) {
        Entities entities = tweet.getEntities();
        if (entities != null) { //only if there are entities to get
            List<Media> mediaList = entities.getMedia();
            for (Media media : mediaList) {
                if (media.isPhoto()) { //getting image ans setting up w/ Picasso
                    ImageView ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
                    Picasso.with(this).load(media.getMediaUrl()).into(ivPhoto);
                    ivPhoto.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void retweetButton(final Tweet tweet) {
        final Long tweetId = tweet.getId();
        final ImageView ivRetweet = (ImageView) findViewById(R.id.ivRetweets);
        //setting up different images for when retweet is sent/not sent
        if (tweet.isRetweeted()) {
            ivRetweet.setImageResource(R.drawable.ic_twitter_retweet_done);
        } else {
            ivRetweet.setImageResource(R.drawable.ic_twitter_retweet_default);
            ivRetweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TwitterApplication.getRestClient().retweet(tweetId, new TwitterClient.TweetResponseHandler() {
                        @Override
                        public void onSuccess(Tweet tweet) {
                            TweetDetailsActivity.this.tweet = tweet;
                            tvRetweetCount.setText(String.valueOf(tweet.getRetweetCount()));
                            ivRetweet.setImageResource(R.drawable.ic_twitter_retweet_done);
                        }

                        @Override
                        public void onFailure(Throwable error) {
                            Toast.makeText(getApplicationContext(), "Error: unable to retweet", Toast.LENGTH_LONG);
                        }
                    });
                }
            });
        }
    }


    public void favoriteButton(final Tweet tweet) {
        final TextView tvFavoritesCount = (TextView) findViewById(R.id.tvFavoritesCount);
        ivFavorite = (ImageView) findViewById(R.id.ivFavorites);
        //setting up images for when favorited and not favorited
        tvFavoritesCount.setText(String.valueOf(tweet.getFavoritesCount()));
        if (tweet.isFavorited()) {
            ivFavorite.setImageResource(R.drawable.ic_twitter_favorite_done);
        } else {
            ivFavorite.setImageResource(R.drawable.ic_twitter_favorite_default);
        }
        ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwitterClient restClient = TwitterApplication.getRestClient();
                if (!tweet.isFavorited()) {
                    restClient.favorite(tweet.getId(), new TwitterClient.TweetResponseHandler() {

                        //counting favorites for tweets
                        @Override
                        public void onSuccess(Tweet tweet) {
                            TweetDetailsActivity.this.tweet = tweet;
                            tvFavoritesCount.setText(String.valueOf(tweet.getFavoritesCount()));
                            favoriteButton(tweet);
                        }

                        @Override
                        public void onFailure(Throwable error) {
                            Toast.makeText(getApplicationContext(), "Error: unable to favorite tweet", Toast.LENGTH_LONG);
                        }
                    });
                } else {
                    //unfavorite
                    restClient.unfavorite(tweet.getId(), new TwitterClient.TweetResponseHandler() {
                        @Override
                        public void onSuccess(Tweet tweet) {
                            TweetDetailsActivity.this.tweet = tweet;
                            tvFavoritesCount.setText(String.valueOf(tweet.getFavoritesCount()));
                            favoriteButton(tweet);
                        }

                        @Override
                        public void onFailure(Throwable error) {
                            Toast.makeText(getApplicationContext(), "Error: unable to unfavorite tweet", Toast.LENGTH_LONG);
                        }
                    });
                }
            }
        });
    }
    @BindView(R.id.tvTweetText) TextView tvTweetText;
    @BindView(R.id.ivUserPhoto) ImageView ivUserPhoto;
    @BindView(R.id.tvUserName) TextView tvUserName;
    @BindView(R.id.tvUserScreenName) TextView tvUserScreenName;
    @BindView(R.id.tvCreatedAt) TextView tvCreatedAt;
    @BindView(R.id.tvReweetCount) TextView tvRetweetCount; //misspelling
     @BindView(R.id.tvFavoritesCount) TextView tvFavoritesCount;
    @BindView(R.id.ivReply) ImageView ivReply;




    private void tweetDetails(final Tweet tweet) {
        // TextView tvTweetText = (TextView) findViewById(R.id.tvTweetText);
        //ImageView ivUserPhoto = (ImageView) findViewById(R.id.ivUserPhoto);
        //TextView tvUserName = (TextView) findViewById(R.id.tvUserName);
        //TextView tvUserScreenName = (TextView) findViewById(R.id.tvUserScreenName);
        //TextView tvCreatedAt = (TextView) findViewById(R.id.tvCreatedAt);
        //tvRetweetCount = (TextView) findViewById(R.id.tvRetweetCount);
        //TextView tvFavoritesCount = (TextView) findViewById(R.id.tvFavoritesCount);
        //ImageView ivReply = (ImageView) findViewById(R.id.ivReply);

        ButterKnife.bind(this);

        //getting info about the user
        tvTweetText.setText(Html.fromHtml(tweet.getText()), TextView.BufferType.SPANNABLE);
        TwitterUser user = tweet.getUser();
        ivUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra(Extras.USER_ID, tweet.getUser().getId());
                startActivity(intent);
            }
        });
        Picasso.with(this).load(user.getProfileImageUrl()).into(ivUserPhoto);
        tvUserName.setText(user.getName());
        tvUserScreenName.setText("@" + user.getScreenName());
        tvRetweetCount.setText(String.valueOf(tweet.getRetweetCount()));
        tvFavoritesCount.setText(String.valueOf(tweet.getFavoritesCount()));
        try {
            tvCreatedAt.setText(STRING_FORMATTER.format(FORMATTER.parse(tweet.getCreatedAt())));
        } catch (ParseException e) {
            Log.d("TWEET", "uh oh", e);
        }
        ivReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                composeTweetFragment = new ComposeTweetFragment();
                composeTweetFragment.setInReplyToStatusId(tweet.getId());
                composeTweetFragment.setInReplyToScreenName(tweet.getUser().getScreenName());
                composeTweetFragment.setListener(new ComposeTweetFragment.StatusUpdateListener() {
                    @Override
                    public void onStatusUpdated() {
                        composeTweetFragment.dismiss();
                        showLatestHomeTimelineTweets();
                    }
                });
                composeTweetFragment.show(fragmentManager, "COMPOSE_TWEET");
            }
        });
    }

    @Override
    public void onStatusUpdated() {
        if (composeTweetFragment != null) {
            composeTweetFragment.dismiss();
        }
    }

    @Override
    public void showLatestHomeTimelineTweets() {
        startActivity(new Intent(this, TimelineActivity.class));
    }

}
