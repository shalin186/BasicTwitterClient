package com.codepath.apps.basictwitter.adapters;

import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.TwitterApplication;
import com.codepath.apps.basictwitter.activities.ComposeTweetActivity;
import com.codepath.apps.basictwitter.activities.UserProfileActivity;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.utilities.DebugInfo;
import com.codepath.apps.basictwitter.utilities.IntentCode;
import com.codepath.apps.basictwitter.utilities.NetworkingUtilities;
import com.codepath.apps.basictwitter.utilities.RelativeTimeStamp;
import com.codepath.apps.basictwitter.utilities.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetAdapter extends ArrayAdapter<Tweet> {

	public View view;
	public Tweet tweet;
	public TwitterClient client;
	public Context context;

	public static class ViewHolder {
		ImageView ivProfileImg;
		TextView tvUserName;
		TextView tvUserScreenName;
		TextView tvBody;
		TextView tvRelativeTimeStamp;
		ImageView ivTweetImg;
		ImageView ivReply;
		LinearLayout llRetweet;
		ImageView ivRetweet;
		TextView tvRetweetCount;
		LinearLayout llFavorite;
		ImageView ivFavorite;
		TextView tvFavoriteCount;
	}

	public TweetAdapter(Context context, List<Tweet> tweets) {
		super(context, 0, tweets);
		this.client = TwitterApplication.getRestClient();
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// DebugInfo.stackTrace();

		Tweet tweet = getItem(position);

		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.tweet_single_item, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.ivProfileImg = (ImageView) convertView
					.findViewById(R.id.ivProfileImg);
			viewHolder.tvUserName = (TextView) convertView
					.findViewById(R.id.tvUserName);
			viewHolder.tvUserScreenName = (TextView) convertView
					.findViewById(R.id.tvUserScreenName);
			viewHolder.tvBody = (TextView) convertView
					.findViewById(R.id.tvBody);
			viewHolder.tvRelativeTimeStamp = (TextView) convertView
					.findViewById(R.id.tvRelativeTimeStamp);
			viewHolder.ivTweetImg = (ImageView) convertView
					.findViewById(R.id.ivTweetImage);
			viewHolder.ivReply = (ImageView) convertView
					.findViewById(R.id.ivReply);
			viewHolder.llRetweet = (LinearLayout) convertView
					.findViewById(R.id.llRetweet);
			viewHolder.ivRetweet = (ImageView) convertView
					.findViewById(R.id.ivRetweet);
			viewHolder.tvRetweetCount = (TextView) convertView
					.findViewById(R.id.tvRetweetCount);
			viewHolder.llFavorite = (LinearLayout) convertView
					.findViewById(R.id.llFavorite);
			viewHolder.ivFavorite = (ImageView) convertView
					.findViewById(R.id.ivFavorite);
			viewHolder.tvFavoriteCount = (TextView) convertView
					.findViewById(R.id.tvFavoriteCount);
			convertView.setTag(R.color.favorite_count, viewHolder);
			//convertView.setTag(viewHolder);
		} else {
			//viewHolder = (ViewHolder) convertView.getTag(R.id.tvBody);
			viewHolder = (ViewHolder) convertView.getTag(R.color.favorite_count);
		}

		convertView.setTag(R.color.retweet_count, tweet);
		
		ImageLoader imageLoader = ImageLoader.getInstance();

		viewHolder.ivProfileImg.setTag(tweet);
		viewHolder.ivProfileImg.setImageResource(android.R.color.transparent);
		imageLoader.displayImage(tweet.getUser().getProfileImageUrl(),
				viewHolder.ivProfileImg);

		viewHolder.tvUserName.setText(tweet.getUser().getUserName());

		viewHolder.tvUserScreenName.setText("@"
				+ tweet.getUser().getUserScreenName());

		viewHolder.tvBody.setText(tweet.getBody());

		viewHolder.tvRelativeTimeStamp.setText(RelativeTimeStamp
				.getRelativeTimeInShortFormat(tweet.getCreateAt()));

		viewHolder.ivTweetImg.setImageResource(android.R.color.transparent);
		imageLoader.displayImage(tweet.getTweetEntity().getImageUrl(),
				viewHolder.ivTweetImg);

		
		viewHolder.ivReply.setTag(tweet);
		
		viewHolder.llRetweet.setTag(tweet);
		if (tweet.getbRetweeted()) {
			viewHolder.ivRetweet.setImageResource(R.drawable.retweet_on);
			viewHolder.tvRetweetCount.setTextColor(getContext().getResources()
					.getColor(R.color.retweet_count));
		} else {
			viewHolder.ivRetweet.setImageResource(R.drawable.retweet);
			viewHolder.tvRetweetCount.setTextColor(Color.DKGRAY);
		}

		if (tweet.getRetweetCount() > 0) {
			viewHolder.tvRetweetCount.setVisibility(View.VISIBLE);
			viewHolder.tvRetweetCount.setText(tweet.getRetweetCount() + "");
		} else {
			viewHolder.tvRetweetCount.setVisibility(View.INVISIBLE);
		}

		viewHolder.llFavorite.setTag(tweet);
		if (tweet.getbFavorited()) {
			viewHolder.ivFavorite.setImageResource(R.drawable.favorite_on);
			viewHolder.tvFavoriteCount.setTextColor(getContext().getResources()
					.getColor(R.color.favorite_count));
		} else {
			viewHolder.ivFavorite.setImageResource(R.drawable.favorite);
			viewHolder.tvFavoriteCount.setTextColor(Color.DKGRAY);
		}

		if (tweet.getFavoriteCount() > 0) {
			viewHolder.tvFavoriteCount.setVisibility(View.VISIBLE);
			viewHolder.tvFavoriteCount.setText(tweet.getFavoriteCount() + "");
		} else {
			viewHolder.tvFavoriteCount.setVisibility(View.INVISIBLE);
		}

		setOnClickListeners(convertView, viewHolder);

		return convertView;
	}

	public void setOnClickListeners(View convertView, ViewHolder viewHolder) {

		// Event Listener to view tweet details
		convertView.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				tweet = (Tweet) v.getTag(R.color.retweet_count);
				
				DebugInfo.showToast(getContext(),
						"Tweet Deatils!!");
			}
		});
		
		// Event Listener to open user details
		viewHolder.ivProfileImg.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				view = v;
				ImageView ivProfileImg = (ImageView) view;
				tweet = (Tweet) ivProfileImg.getTag();
				
				DebugInfo.showToast(getContext(),
						"User Deatils!!");
				
				Intent UserProfileIntent = new Intent(getContext(),
						UserProfileActivity.class);
				UserProfileIntent.putExtra("user", tweet.getUser());
				getContext().startActivity(UserProfileIntent);
			}
		});
		
		// Event Listener to reply to tweet
		viewHolder.ivReply.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				view = v;
				ImageView ivReply = (ImageView) view;
				tweet = (Tweet) ivReply.getTag();
				
				DebugInfo.showToast(getContext(),
						"Tweet Reply!!");
				Intent composeTweetIntent = new Intent(getContext(),
						ComposeTweetActivity.class);
				composeTweetIntent.putExtra("replyUserName", "@"+tweet.getUser().getUserScreenName()+" ");
				((Activity)getContext()).startActivityForResult(composeTweetIntent, IntentCode.REQUEST_CODE);
				//getContext().startActivity(composeTweetIntent);
				//getContext();
				
			}
		});
		
		// Event Listener to add/remove retweet
		viewHolder.llRetweet.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				DebugInfo.stackTrace();

				view = v;
				LinearLayout llRetweet = (LinearLayout) view;
				tweet = (Tweet) llRetweet.getTag();

				if (NetworkingUtilities.isNetworkAvailable(getContext()) == false) {
					DebugInfo.showToast(getContext(),
							"Netowrk Connection Problem");
					return;
				}
				// Remove retweet if already retweeted
				if (tweet.getbRetweeted()) {
					client.postRetweet(tweet.getId(),
							new JsonHttpResponseHandler() {
								@Override
								public void onSuccess(JSONObject jsonObj) {
									long retweetCount;
									ImageView ivRetweet = (ImageView) view
											.findViewById(R.id.ivRetweet);
									TextView tvRetweetCount = (TextView) view
											.findViewById(R.id.tvRetweetCount);

									// Check if status is updated in response
									if (jsonObj.optBoolean("retweeted")) {
										retweetCount = Long
												.parseLong(tvRetweetCount
														.getText().toString());
										retweetCount--;
									} else {
										retweetCount = jsonObj
												.optLong("retweet_count");
									}

									ivRetweet
											.setImageResource(R.drawable.retweet);

									if (retweetCount == 0) {
										tvRetweetCount
												.setVisibility(View.INVISIBLE);
									} else {
										tvRetweetCount
												.setTextColor(Color.DKGRAY);
										tvRetweetCount.setText(retweetCount
												+ "");
									}

									tweet.setbRetweeted(false);
									tweet.setRetweetCount(retweetCount);
									tweet.setId(jsonObj.optLong("id"));
								}

								public void onFailure(Throwable arg0,
										JSONObject arg1) {
									DebugInfo
											.logMessage("Unable to remove retweet");
								}
								
								@Override
								protected void handleFailureMessage(
										Throwable arg0, String failureResonse) {
									DebugInfo.stackTrace();
									DebugInfo
									.logError("failureResonse");
								}

							});
				} else {
					client.postRetweet(tweet.getId(),
							new JsonHttpResponseHandler() {
								@Override
								public void onSuccess(JSONObject jsonObj) {
									long retweetCount;
									ImageView ivRetweet = (ImageView) view
											.findViewById(R.id.ivRetweet);
									TextView tvRetweetCount = (TextView) view
											.findViewById(R.id.tvRetweetCount);

									// Check if status is updated in response
									if (jsonObj.optBoolean("retweeted")) {
										retweetCount = jsonObj
												.optLong("retweet_count");
									} else {

										retweetCount = Long
												.parseLong(tvRetweetCount
														.getText().toString());
										retweetCount++;
									}

									ivRetweet
											.setImageResource(R.drawable.retweet_on);

									tvRetweetCount.setVisibility(View.VISIBLE);
									tvRetweetCount.setTextColor(getContext()
											.getResources().getColor(
													R.color.retweet_count));
									tvRetweetCount.setText(retweetCount + "");

									tweet.setbRetweeted(true);
									tweet.setRetweetCount(retweetCount);
									tweet.setId(jsonObj.optLong("id"));
								}

								public void onFailure(Throwable arg0,
										JSONObject arg1) {
									DebugInfo.showToast(getContext(),
											"Unable to add retweet");
								}

								protected void handleFailureMessage(
										Throwable arg0, String failureResonse) {
									DebugInfo.stackTrace();
									DebugInfo
									.logError("failureResonse");
								}
								
							});
				}
			}

		});

		// Event Listener to add/remove favorite
		viewHolder.llFavorite.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				DebugInfo.stackTrace();

				view = v;
				LinearLayout llFavorite = (LinearLayout) view;
				tweet = (Tweet) llFavorite.getTag();

				if (NetworkingUtilities.isNetworkAvailable(getContext()) == false) {
					DebugInfo.showToast(getContext(),
							"Netowrk Connection Problem");
					return;
				}
				// Remove favorite if already favorited
				if (tweet.getbFavorited()) {
					client.postUnFavorite(tweet.getId(),
							new JsonHttpResponseHandler() {
								@Override
								public void onSuccess(JSONObject jsonObj) {
									long favoriteCount;
									ImageView ivFavorite = (ImageView) view
											.findViewById(R.id.ivFavorite);
									TextView tvFavoriteCount = (TextView) view
											.findViewById(R.id.tvFavoriteCount);

									// Check if status is updated in response
									if (jsonObj.optBoolean("favorited")) {
										favoriteCount = Long
												.parseLong(tvFavoriteCount
														.getText().toString());
										favoriteCount--;
									} else {
										favoriteCount = jsonObj
												.optLong("favorite_count");
									}

									ivFavorite
											.setImageResource(R.drawable.favorite);

									if (favoriteCount == 0) {
										tvFavoriteCount
												.setVisibility(View.INVISIBLE);
									} else {
										tvFavoriteCount
												.setTextColor(Color.DKGRAY);
										tvFavoriteCount.setText(favoriteCount
												+ "");
									}

									tweet.setbFavorited(false);
									tweet.setFavoriteCount(favoriteCount);
								}

								public void onFailure(Throwable arg0,
										JSONObject arg1) {
									DebugInfo.showToast(getContext(),
											"Unable to remove favorite");
								}
								
								protected void handleFailureMessage(
										Throwable arg0, String failureResonse) {
									DebugInfo.stackTrace();
									DebugInfo
									.logError("failureResonse");
								}
							});
				} else {
					client.postFavorite(tweet.getId(),
							new JsonHttpResponseHandler() {
								@Override
								public void onSuccess(JSONObject jsonObj) {
									long favoriteCount;
									ImageView ivFavorite = (ImageView) view
											.findViewById(R.id.ivFavorite);
									TextView tvFavoriteCount = (TextView) view
											.findViewById(R.id.tvFavoriteCount);

									// Check if status is updated in response
									if (jsonObj.optBoolean("favorited")) {
										favoriteCount = jsonObj
												.optLong("favorite_count");
									} else {

										favoriteCount = Long
												.parseLong(tvFavoriteCount
														.getText().toString());
										favoriteCount++;
									}

									ivFavorite
											.setImageResource(R.drawable.favorite_on);

									tvFavoriteCount.setVisibility(View.VISIBLE);
									tvFavoriteCount.setTextColor(getContext()
											.getResources().getColor(
													R.color.favorite_count));
									tvFavoriteCount.setText(favoriteCount + "");

									tweet.setbFavorited(true);
									tweet.setFavoriteCount(favoriteCount);
								}

								public void onFailure(Throwable arg0,
										JSONObject arg1) {
									DebugInfo.showToast(getContext(),
											"Unable to add favorite");
								}
								protected void handleFailureMessage(
										Throwable arg0, String failureResonse) {
									DebugInfo.stackTrace();
									DebugInfo
									.logError("failureResonse");
								}
							});
				}
			}

		});
	}
}
