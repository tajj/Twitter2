package com.codepath.apps.twitter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.TwitterApplication;
import com.codepath.apps.twitter.TwitterClient;
import com.codepath.apps.twitter.constants.Extras;
import com.codepath.apps.twitter.fragments.ComposeTweetFragment;
import com.codepath.apps.twitter.listeners.OnUserProfileClickListener;
import com.codepath.apps.twitter.models.TwitterUser;

public abstract class BaseActivity extends AppCompatActivity implements ComposeTweetFragment.StatusUpdateListener, OnUserProfileClickListener {
    private ComposeTweetFragment composeTweetFragment;
    protected TwitterUser authenticatedUser;
    ActionBar supportActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        supportActionBar.setIcon(R.drawable.ic_launcher_twitter_round);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        composeMenu(menu);
        profileMenu(menu);
        searchMenu(menu);
        return true;
    }


    private void composeMenu(Menu menu) {
        MenuItem composeTweet = menu.findItem(R.id.action_compose_tweet);
        composeTweet.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                composeTweetFragment = new ComposeTweetFragment();
                composeTweetFragment.show(fragmentManager, "COMPOSE_TWEET");
                composeTweetFragment.setListener(BaseActivity.this);
                return true;
            }
        });
    }

    private void profileMenu(Menu menu) {
        MenuItem miProfile = menu.findItem(R.id.action_profile);
        miProfile.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (authenticatedUser == null) {
                    TwitterApplication.getRestClient().getAuthenticatedUser(new TwitterClient.TwitterUserResponseHandler() {
                        @Override
                        public void onSuccess(TwitterUser user) {
                            authenticatedUser = user;
                            showAuthenticatedUserProfile();
                        }

                        @Override
                        public void onFailure(Throwable error) {
                            Log.e(getTag(), "Error: Cannot get user profile", error);
                        }
                    });
                } else {
                    showAuthenticatedUserProfile();
                }
                return true;
            }
        });
    }

    private void searchMenu(Menu menu) {
        MenuItem searchItem = menu.findItem(R.id.action_search_tweets);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                showSearchResults(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onUserProfileClick(TwitterUser user) {
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra(Extras.USER_ID, user.getId());
        startActivity(i);
    }

    @Override
    public void onStatusUpdated() {
        if (composeTweetFragment != null) {
            composeTweetFragment.dismiss();
        }
        showLatestHomeTimelineTweets();
    }


//this one doesn't need to be protected, all below need to be abstract bc they dont contain implementation
    public abstract void showLatestHomeTimelineTweets();

    //protected makes itis accessible within all classes in the same package and within subclasses in other packages

   protected abstract void showAuthenticatedUserProfile();

    protected abstract void showSearchResults(String query);

    protected abstract String getTag();

}
