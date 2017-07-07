package com.codepath.apps.twitter.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.TwitterApplication;
import com.codepath.apps.twitter.TwitterClient;
import com.codepath.apps.twitter.constants.Extras;
import com.codepath.apps.twitter.fragments.FavoritesTimelineFragment;
import com.codepath.apps.twitter.fragments.FollowersListFragment;
import com.codepath.apps.twitter.fragments.FollowingListFragment;
import com.codepath.apps.twitter.fragments.UserTimelineFragment;
import com.codepath.apps.twitter.models.TwitterUser;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProfileActivity extends BaseActivity {
    private static final String TAG = "PROFILE";

    private static final NumberFormat NUMBER_FORMATTTER = NumberFormat.getIntegerInstance();
    private TwitterClient client;
    private Long userId;
    private TwitterUser tu;


    private ProfilePagerAdapter aPager;
  //  private ViewPager vPager;

    @BindView(R.id.viewpager) ViewPager vPager;
    @BindView(R.id.tabs) PagerSlidingTabStrip tabStrip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //vPager = (ViewPager) findViewById(R.id.viewpager);
       // PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        supportActionBar.setIcon(R.drawable.ic_launcher_twitter_round);

        client = TwitterApplication.getRestClient();

        userId = getIntent().getLongExtra(Extras.USER_ID, -1);
        aPager = new ProfilePagerAdapter(getSupportFragmentManager(), getLayoutInflater());
        vPager.setAdapter(aPager);

        tabStrip.setViewPager(vPager);
       // miActionProgressItem.setVisible(true);


        //show progress bar before network request
        client.getUser(userId, new TwitterClient.TwitterUserResponseHandler() {

            @Override
            public void onSuccess(TwitterUser user) {
               // miActionProgressItem.setVisible(false);
                tu = user;
                populateUserDetails(tu);
                aPager.setupStats(user);
            }

            @Override
            public void onFailure(Throwable error) {
               // miActionProgressItem.setVisible(false);
                Log.e(TAG, "Error: Could not retrieve user's profile", error);
            }
        });
    }



    @Override
    public void showLatestHomeTimelineTweets() {
        startActivity(new Intent(this, TimelineActivity.class));
    }

    @BindView(R.id.tvUserDescription) TextView tvUserDescription;
    @BindView(R.id.ivUserPhoto) ImageView ivUserPhoto;
    @BindView(R.id.tvUserName) TextView tvUserName;
    @BindView(R.id.tvUserScreenName) TextView tvUserScreenName;
    @BindView(R.id.ivUserBackgroundImage) ImageView ivUserBackgroundImage;




    private void populateUserHeader(TwitterUser user) {
        //TextView tvUserDescription = (TextView) findViewById(R.id.tvUserDescription);
       // ImageView ivUserPhoto = (ImageView) findViewById(R.id.ivUserPhoto);
       // TextView tvUserName = (TextView) findViewById(R.id.tvUserName);
       // TextView tvUserScreenName = (TextView) findViewById(R.id.tvUserScreenName);
        final RelativeLayout rlUserHeader = (RelativeLayout) findViewById(R.id.rlUserHeader);
       // ImageView ivUserBackgroundImage = (ImageView) findViewById(R.id.ivUserBackgroundImage);

        ButterKnife.bind(this);


        ProfileActivity.this.tu = user;
        getSupportActionBar().setTitle("@"+tu.getScreenName());

        tvUserDescription.setText(user.getDescription());

        ivUserPhoto.setImageResource(0);
        Picasso.with(getApplicationContext()).load(tu.getProfileImageUrl()).into(ivUserPhoto);

        tvUserName.setText(tu.getName());
        tvUserScreenName.setText("@"+tu.getScreenName());
        //using picasso to get image instead of glide, which we used last time
        final String backgroundImageUrl = tu.getProfileBackgroundImageUrl();
        ivUserBackgroundImage.setImageResource(0);
        if (backgroundImageUrl != null && backgroundImageUrl != "") {
            Picasso.with(getApplicationContext()).load(backgroundImageUrl).into(ivUserBackgroundImage);
        } else {
            setHeaderBackgroundColor(rlUserHeader);
        }
    }

    private void setHeaderBackgroundColor(RelativeLayout rlUserHeader) {
        rlUserHeader.setBackgroundColor(Color.parseColor("#"+tu.getProfileBackgroundColor()));
    }

    private void populateUserTimeline(Long userId) {
        aPager.userTimelineFragment.populateTimeline(userId);
        vPager.setCurrentItem(aPager.USER_TIMELINE_POSITION);
    }

    @Override
    protected void showAuthenticatedUserProfile() {
        populateUserDetails(authenticatedUser);
    }

    private void populateUserDetails(TwitterUser user) {
        this.tu = user;
        Long userId = user.getId();

        populateUserHeader(user);
        populateUserTimeline(userId);

        //only populate if it isn't null
        if (aPager.followingListFragment != null) {
            aPager.followingListFragment.populateWithUsers(userId);
        }
        if (aPager.followersListFragment != null) {
            aPager.followersListFragment.populateWithUsers(userId);
        }
        if (aPager.favoritesTimelineFragment != null) {
            aPager.favoritesTimelineFragment.populateTimeline(userId);
        }
        aPager.setupStats(tu);
    }

    @Override
    protected String getTag() {
        return TAG;
    }

    @Override
    protected void showSearchResults(String query) {
        Intent i = new Intent(this, SearchActivity.class);
        i.putExtra(Extras.QUERY, query);
        startActivity(i);
    }

    @Override
    public void onUserProfileClick(TwitterUser user) {
        populateUserDetails(user);
    }

    public class ProfilePagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.ViewTabProvider {
        private final String[] tabTitles = {"Tweets", "Following", "Followers", "Favorites"};

        private final int FOLLOWING_LIST_POSITION = 1;
        private final int FOLLOWERS_LIST_POSITION = 2;
        private final int FAVORITES_LIST_POSITION = 3;

        private final int USER_TIMELINE_POSITION = 0;

        private UserTimelineFragment userTimelineFragment;
        private FollowingListFragment followingListFragment;
        private FollowersListFragment followersListFragment;
        private FavoritesTimelineFragment favoritesTimelineFragment;
        private View[] TABS;

        public ProfilePagerAdapter(FragmentManager fragmentManager, LayoutInflater inflater) {
            super(fragmentManager);
            TABS = new View[4];
            TABS[0] = inflater.inflate(R.layout.item_user_stats, null);
            TABS[1] = inflater.inflate(R.layout.item_user_stats, null);
            TABS[2] = inflater.inflate(R.layout.item_user_stats, null);
            TABS[3] = inflater.inflate(R.layout.item_user_stats, null);
        }

        private void setupStats(TwitterUser twitterUser) {
            setupView(TABS[0], tabTitles[0], twitterUser.getTweetCount());
            setupView(TABS[1], tabTitles[1], twitterUser.getFriendsCount());
            setupView(TABS[2], tabTitles[2], twitterUser.getFollowersCount());
            setupView(TABS[3], tabTitles[3], twitterUser.getUserFavoritedCount());
        }

        //@BindView(R.id.tvStatLabel) TextView tvStatLabel;
       /// @BindView(R.id.tvStatCount) TextView tvStatCount;


        private void setupView(View view, String title, int count) {
            TextView tvStatLabel = (TextView) view.findViewById(R.id.tvStatLabel);
            TextView tvStatCount = (TextView) view.findViewById(R.id.tvStatCount);
           // ButterKnife.bind(this);

            tvStatLabel.setText(title.toUpperCase());
            tvStatCount.setText(NUMBER_FORMATTTER.format(count));
        }

        //creating the user fragment
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case USER_TIMELINE_POSITION:
                    return UserTimelineFragment.newInstance(userId);
                case FOLLOWING_LIST_POSITION:
                    return FollowingListFragment.newInstance(userId);
                case FOLLOWERS_LIST_POSITION:
                    return FollowersListFragment.newInstance(userId);
                case FAVORITES_LIST_POSITION:
                    return FavoritesTimelineFragment.newInstance(userId);
                default:
                    return null;
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            switch (position) {
                case USER_TIMELINE_POSITION:
                    userTimelineFragment = (UserTimelineFragment) fragment;
                    break;
                case FOLLOWING_LIST_POSITION:
                    followingListFragment = (FollowingListFragment) fragment;
                    break;
                case FOLLOWERS_LIST_POSITION:
                    followersListFragment = (FollowersListFragment) fragment;
                    break;
                case FAVORITES_LIST_POSITION:
                    favoritesTimelineFragment = (FavoritesTimelineFragment) fragment;
                    break;
            }
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) { return tabTitles[position];
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }


        @Override
        public View getPageView(int position) {
            return TABS[position];
        }
    }

}


// PUT IN BUTTERKNIFE !!!!!!!!!!!