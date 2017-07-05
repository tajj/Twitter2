package com.codepath.apps.twitter.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.constants.Extras;
import com.codepath.apps.twitter.fragments.SearchFragment;

public class SearchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        String q = getIntent().getStringExtra(Extras.QUERY);
        if (savedInstanceState == null) {
            getSearchResults(q);
        }
    }

    @Override
    protected void showAuthenticatedUserProfile() {
        Intent i = new Intent(SearchActivity.this, ProfileActivity.class);
        i.putExtra(Extras.USER_ID, authenticatedUser.getId());
        startActivity(i);
    }

    private void getSearchResults(String query) {
        SearchFragment fragmentSearch = SearchFragment.newInstance(query);
        //display fragment inside the container (dynamically)
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContainer, fragmentSearch);
        ft.commit();
    }



    @Override
    protected void showSearchResults(String query) {
        getSearchResults(query);
    }



    @Override
    public void showLatestHomeTimelineTweets() {
        startActivity(new Intent(this, TimelineActivity.class));
    }

    @Override
    protected String getTag() {
        return "SEARCH";
    }

}
