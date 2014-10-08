package com.codepath.apps.basictwitter.fragments;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.basictwitter.utilities.DebugInfo;
import com.codepath.apps.basictwitter.utilities.EndlessScrollListener;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchTimelineFragment extends TweetsTimelineFragment {

	private String searchText;

	// Creates a new fragment with given arguments
	public static SearchTimelineFragment newInstance(String searchText) {
		SearchTimelineFragment searchTimelineFragment = new SearchTimelineFragment();
		Bundle args = new Bundle();
		args.putString("searchText", searchText);
		searchTimelineFragment.setArguments(args);
		return searchTimelineFragment;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		searchText = getArguments().getString("searchText", "");
		PopulateTimeline(searchText);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = super.onCreateView(inflater, container, savedInstanceState);

		setOnClickListeners();

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	public void PopulateTimeline(String searchText) {
		super.PopulateTimeline();
		DebugInfo.stackTrace();

		client.getSearchTimeline(bLoadMore, bRefresh, searchText,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject jsonObj) {
						DebugInfo.stackTrace();
						JSONArray jsonArray = jsonObj.optJSONArray("statuses");

						loadTweetAdapter(jsonArray);
						removeProgressIndicators();
					}

					public void onFailure(Throwable e, String s) {
						DebugInfo.stackTrace();
						DebugInfo.logError(e.toString());
						DebugInfo.logError(s.toString());
					}

					public void onFailure(Throwable e, JSONObject arg1) {
						DebugInfo.stackTrace();
						DebugInfo.logError(e.toString());
					}

					protected void handleFailureMessage(Throwable arg0,
							String failureResonse) {
						DebugInfo.stackTrace();
						DebugInfo.logError("failureResonse");
					}
				});
	}

	private void setOnClickListeners() {
		DebugInfo.stackTrace();

		// Infinite Pagination
		lvTweets.setOnScrollListener(new EndlessScrollListener() {

			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				PageNumber = page;
				bRefresh = false;
				bLoadMore = true;
				llProgressBar.setVisibility(View.VISIBLE);
				PopulateTimeline(searchText);
			}
		});

		// Swipe to refresh listener
		swipeContainer.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				bRefresh = true;
				bLoadMore = false;
				PopulateTimeline(searchText);
			}
		});
	}
}
