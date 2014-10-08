package com.codepath.apps.basictwitter.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.fragments.HomeTimelineFragment;
import com.codepath.apps.basictwitter.fragments.MentionsTimelineFragment;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.codepath.apps.basictwitter.utilities.DebugInfo;
import com.codepath.apps.basictwitter.utilities.IntentCode;
import com.codepath.apps.basictwitter.utilities.SherlockTabListener;
import com.codepath.apps.basictwitter.utilities.TwitterApiHelper;

public class TimelineActivity extends SherlockFragmentActivity implements TabListener {

	private SearchView searchView;
	
	private FragmentPagerAdapter adapterViewPager;
	private ViewPager vpPager;
	
	ActionBar actionBar;
	
	public static HomeTimelineFragment homeTimelineFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DebugInfo.stackTrace();

		setContentView(R.layout.activity_timeline);
		setCurrentUser();
		setupFragmentPager();
		setupTabsOnly();
		//setupTabs();
	}

	private void setupTabsOnly() {
		DebugInfo.stackTrace();
		
		Tab tabFirst;
		Tab tabSecond;

		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);

		tabFirst = actionBar.newTab();
		tabFirst.setText("Home");
		tabFirst.setTabListener(this);
		actionBar.addTab(tabFirst);
		actionBar.selectTab(tabFirst);
		
		tabSecond = actionBar.newTab();
		tabSecond.setText("Mentions");
		tabSecond.setTabListener(this);
		actionBar.addTab(tabSecond);
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		vpPager.setCurrentItem(tab.getPosition());
	}

	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}
	
	private void setupFragmentPager() {
		vpPager = (ViewPager) findViewById(R.id.vpPager);
		adapterViewPager = new TimelinePagerAdapter(getSupportFragmentManager());
		vpPager.setAdapter(adapterViewPager);
		
		// Attach the page change listener inside the activity
		vpPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			// This method will be invoked when a new page becomes selected.
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
			
			// This method will be invoked when the current page is scrolled
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				// Code goes here
			}
			
			// Called when the scroll state changes: 
			// SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
			@Override
			public void onPageScrollStateChanged(int state) {
				// Code goes here
			}
		});
	}

	public static class TimelinePagerAdapter extends FragmentPagerAdapter {
		private static int NUM_ITEMS = 2;

		public TimelinePagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		// Returns total number of pages
		@Override
		public int getCount() {
			return NUM_ITEMS;
		} 

		// Returns the fragment to display for that page
		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0: // Fragment # 0 - This will show FirstFragment
				homeTimelineFragment = HomeTimelineFragment.newInstance();
				return homeTimelineFragment;
			case 1: // Fragment # 1 - This will show SecondFragment
				return MentionsTimelineFragment.newInstance();
			default:
				return null;
			}
		}

		// Returns the page title for the top indicator
		@Override
		public CharSequence getPageTitle(int position) {
			return "Page " + position;
		}

	}

	private void setupTabs() {
		DebugInfo.stackTrace();
		
		Tab tabFirst;
		Tab tabSecond;

		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);

		tabFirst = actionBar
				.newTab()
				.setText("Home")
				.setTabListener(
						new SherlockTabListener<HomeTimelineFragment>(
								R.id.flContainer, this, "Home",
								HomeTimelineFragment.class));

		actionBar.addTab(tabFirst);
		actionBar.selectTab(tabFirst);

		tabSecond = actionBar
				.newTab()
				.setText("Mentions")
				.setTabListener(
						new SherlockTabListener<MentionsTimelineFragment>(
								R.id.flContainer, this, "Mentions",
								MentionsTimelineFragment.class));

		actionBar.addTab(tabSecond);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		DebugInfo.stackTrace();
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.menu_twitter_home, menu);

		MenuItem searchItem = menu.findItem(R.id.action_search);
		searchView = (SearchView) searchItem.getActionView();
		searchView.setOnQueryTextListener(new OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				// perform query here

				// Hide soft keybaord
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

				Intent searchTweetsIntent = new Intent(TimelineActivity.this,
						SearchActivity.class);
				searchTweetsIntent.putExtra("searchText", query);
				startActivity(searchTweetsIntent);

				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});
		return super.onCreateOptionsMenu(menu);
	}

	private void setCurrentUser() {
		TwitterApiHelper helper = new TwitterApiHelper(this);
		helper.setCurrentUser();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		DebugInfo.stackTrace();
		switch (item.getItemId()) {
		case R.id.miComposeTweet:
			Intent composeTweetIntent = new Intent(TimelineActivity.this,
					ComposeTweetActivity.class);
			composeTweetIntent.putExtra("replyUserName", "");
			startActivityForResult(composeTweetIntent, IntentCode.REQUEST_CODE);
			return true;

		case R.id.miUserProfile:
			Intent UserProfileIntent = new Intent(TimelineActivity.this,
					UserProfileActivity.class);
			UserProfileIntent.putExtra("user", User.getCurrentUser());
			startActivity(UserProfileIntent);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == IntentCode.RESULT_OK
				&& requestCode == IntentCode.REQUEST_CODE) {
			Tweet tweet = (Tweet) data.getParcelableExtra("tweet");
			actionBar.setSelectedNavigationItem(1);
			vpPager.setCurrentItem(1);
//			HomeTimelineFragment homeTimelineFragment = (HomeTimelineFragment) getSupportFragmentManager()
//					.findFragmentById(R.id.vpPager);
			homeTimelineFragment.displaySentTweet(tweet);
		} else if (resultCode == IntentCode.TWEET_SEND_FAIL
				&& requestCode == IntentCode.REQUEST_CODE) {
			DebugInfo.showToast(this, "Tweet sending failed");
		}

	}
}
