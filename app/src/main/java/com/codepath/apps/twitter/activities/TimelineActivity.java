package com.codepath.apps.twitter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.constants.Extras;
import com.codepath.apps.twitter.fragments.HomeTimelineFragment;
import com.codepath.apps.twitter.fragments.MentionsTimelineFragment;
import com.codepath.apps.twitter.fragments.TweetListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimelineActivity extends BaseActivity { // implements tweets listener
    private TweetsPagerAdapter tPager;

    @BindView(R.id.viewpager) ViewPager viewPager;
    @BindView(R.id.tabs) PagerSlidingTabStrip tabStrip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);

        tPager = new TweetsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tPager);
        tabStrip.setViewPager(viewPager);
    }

//hide and show progress bar in this file
    //listener------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- itemselected listener context
    //listener.onrefresh(true or false) <<<-----in HomeTimelineFragment


    public void showLatestHomeTimelineTweets() {
        viewPager.setCurrentItem(tPager.HOME_TIMELINE_POSITION);
        tPager.htf.populateTimeline();
    }

    @Override
    protected String getTag() {return "TIMELINE";}

    //intent to go to the profile activity
    @Override
    protected void showAuthenticatedUserProfile() {
        Intent i = new Intent(TimelineActivity.this, ProfileActivity.class);
        i.putExtra(Extras.USER_ID, authenticatedUser.getId());
        startActivity(i);
    }

    //intent to go to search activity
    @Override
    protected void showSearchResults(String query) {
        Intent i = new Intent(this, SearchActivity.class);
        i.putExtra(Extras.QUERY, query);
        startActivity(i);
    }

    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        private String[] tabTitles = {"Home", "Mentions"};
        private HomeTimelineFragment htf;
         MentionsTimelineFragment mtf;
        private final int HOME_TIMELINE_POSITION = 0;
        private final int MENTIONS_TIMELINE_POSITION = 1;


        public TweetsPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }




        @Override
        public Fragment getItem(int position) {
            if (position == HOME_TIMELINE_POSITION) { return new HomeTimelineFragment();
            } else if (position == MENTIONS_TIMELINE_POSITION) { return new MentionsTimelineFragment();
            } else { return null; }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TweetListFragment tweetListFragment = (TweetListFragment) super.instantiateItem(container, position);
            switch (position) {
                case HOME_TIMELINE_POSITION:
                    htf = (HomeTimelineFragment) tweetListFragment;
                    break;
                case MENTIONS_TIMELINE_POSITION:
                    mtf = (MentionsTimelineFragment) tweetListFragment;
                    break;
            }
            return tweetListFragment;
        }



        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }

}
