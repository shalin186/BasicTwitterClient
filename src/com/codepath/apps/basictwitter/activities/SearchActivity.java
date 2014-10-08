package com.codepath.apps.basictwitter.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.fragments.SearchTimelineFragment;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.utilities.DebugInfo;
import com.codepath.apps.basictwitter.utilities.IntentCode;

public class SearchActivity extends SherlockFragmentActivity {

	private String searchText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		receiveData();
		setViews();
		setUserTweetsFragment();
	}
	
	private void setUserTweetsFragment() {
		// Within the activity
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		SearchTimelineFragment searchTimelineFragment = SearchTimelineFragment.newInstance(searchText);
		ft.replace(R.id.flSearchContainer, searchTimelineFragment);
		ft.commit();
	}

	private void setViews() {
		getActionBar().setTitle("Search Results");
	}

	private void receiveData() {
		DebugInfo.stackTrace();
		searchText = (String) getIntent().getStringExtra("searchText");
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == IntentCode.RESULT_OK && requestCode == IntentCode.REQUEST_CODE) {
			Tweet tweet = (Tweet)data.getParcelableExtra("tweet");
			SearchTimelineFragment searchTimelineFragment = (SearchTimelineFragment)getSupportFragmentManager().findFragmentById(R.id.flSearchContainer);
			searchTimelineFragment.displaySentTweet(tweet);
		} else if (resultCode == IntentCode.TWEET_SEND_FAIL && requestCode == IntentCode.REQUEST_CODE) {
			DebugInfo.showToast(this, "Tweet sending failed");
		}

	}
}
