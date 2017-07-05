package com.codepath.apps.twitter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.TwitterApplication;
import com.codepath.apps.twitter.TwitterClient;
import com.codepath.apps.twitter.models.TwitterUser;
import com.codepath.oauth.OAuthLoginActionBarActivity;

public class LoginActivity extends OAuthLoginActionBarActivity<TwitterClient> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}


	//  adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	//showing timeline
	private void displayTimeline() {
		Intent i = new Intent(LoginActivity.this, TimelineActivity.class);
		startActivity(i);
	}

	// displays homepage upon the success of login with OAuth
	@Override
	public void onLoginSuccess() {
		getClient().getAuthenticatedUser(new TwitterClient.TwitterUserResponseHandler() {
			@Override
			public void onSuccess(TwitterUser user) {
				TwitterApplication.setAuthenticatedUserId(user.getId());
				displayTimeline();
			}

			@Override
			public void onFailure(Throwable error) {
				displayTimeline();
			}
		});
	}


	// shows error dialog if OAuth fails
	@Override
	public void onLoginFailure(Exception e) {
		e.printStackTrace();
	}

	// Click handler method for the Login Button which starts the OAuth flow
	public void loginToRest(View view) {
		getClient().connect();
	}

}
