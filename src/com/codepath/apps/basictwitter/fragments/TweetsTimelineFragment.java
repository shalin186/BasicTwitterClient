package com.codepath.apps.basictwitter.fragments;

import java.util.ArrayList;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.TwitterApplication;
import com.codepath.apps.basictwitter.adapters.TweetAdapter;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.utilities.DebugInfo;
import com.codepath.apps.basictwitter.utilities.NetworkingUtilities;
import com.codepath.apps.basictwitter.utilities.TwitterClient;

public class TweetsTimelineFragment extends SherlockFragment {

	// views
	ListView lvTweets;
	SwipeRefreshLayout swipeContainer;

	LinearLayout llProgressBar;

	// Adapter related
	private ArrayList<Tweet> tweets;
	private ArrayAdapter<Tweet> aTweets;

	TwitterClient client;

	int PageNumber = 0;
	Boolean bRefresh = false;
	Boolean bLoadMore = false;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		setupClient();
		setAdapter();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		DebugInfo.stackTrace();

		View v = inflater.inflate(R.layout.fragment_tweets_timeline, container,
				false);
		getViews(v);

		return v;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	public void PopulateTimeline() {
		DebugInfo.stackTrace();

		if (NetworkingUtilities.isNetworkAvailable(getActivity()) == false) {
			DebugInfo.showToast(getActivity(), "Netowrk Connection Problem");
			removeProgressIndicators();
		}
	}

	public void loadTweetAdapter(JSONArray jsonArray) {
		DebugInfo.stackTrace();

		if (PageNumber == 0 && bRefresh == false) {
			tweets.clear();
			aTweets.clear();
		}

		if (bRefresh == true) {
			tweets.addAll(0, Tweet.fromJSONArray(jsonArray));
		} else {
			tweets.addAll(Tweet.fromJSONArray(jsonArray));
		}
		aTweets.notifyDataSetChanged();
	}

	public void removeProgressIndicators() {
		llProgressBar.setVisibility(View.GONE);
		swipeContainer.setRefreshing(false);
	}

	private void setAdapter() {
		DebugInfo.stackTrace();

		tweets = new ArrayList<Tweet>();
		aTweets = new TweetAdapter(getActivity(), tweets);
	}

	private void setupClient() {
		DebugInfo.stackTrace();

		client = TwitterApplication.getRestClient();
	}

	private void getViews(View v) {
		DebugInfo.stackTrace();

		lvTweets = (ListView) v.findViewById(R.id.lvTweets);
		lvTweets.setAdapter(aTweets);

		swipeContainer = (SwipeRefreshLayout) v
				.findViewById(R.id.swipeContainer);
		// Configure the refreshing colors
		swipeContainer.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);

		llProgressBar = (LinearLayout) v.findViewById(R.id.llProgressBar);
		llProgressBar.setVisibility(View.VISIBLE);
	}

	public void displaySentTweet(Tweet sentTweet) {
		tweets.add(0, sentTweet);
		aTweets.notifyDataSetChanged();
	}
}
