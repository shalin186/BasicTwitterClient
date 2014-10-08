package com.codepath.apps.basictwitter.activities;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.TwitterApplication;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.codepath.apps.basictwitter.utilities.DebugInfo;
import com.codepath.apps.basictwitter.utilities.IntentCode;
import com.codepath.apps.basictwitter.utilities.NetworkingUtilities;
import com.codepath.apps.basictwitter.utilities.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ComposeTweetActivity extends Activity {

	private TextView tvMyName;
	private TextView tvMyScreenName;
	private ImageView ivMyProfileImg;
	private EditText etCompose;
	private Button btnTweet;
	private TextView tvCharacterCount;
	private LinearLayout llProgressBar;
	
	private User currentUser;
	private String replyUserName;
	
	private TwitterClient client;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose_tweet);
		DebugInfo.stackTrace();
		getViews();
		receiveData();
		setViews();
		finishSetup();
		setOnClickListeners();
	}
	
	private void setOnClickListeners() {
		
		// Set the number of characters
		// Enable/Disable Tweet Button
		// Adjust colors accordingly
		etCompose.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}
			public void afterTextChanged(Editable arg0) {
				int tweetCharacters = etCompose.getText().length();
				
				tvCharacterCount.setText(tweetCharacters+"");
				if(tweetCharacters == 0 || tweetCharacters > 140){
					btnTweet.setEnabled(false);
					btnTweet.setBackgroundResource(R.drawable.roundbutton);
					if(tweetCharacters > 140){
						tvCharacterCount.setTextColor(getResources()
							.getColor(android.R.color.holo_red_dark));
						tvCharacterCount.setText("-"+(tweetCharacters-140)+"");
					}
				}
				else{
					btnTweet.setEnabled(true);
					btnTweet.setBackgroundResource(R.drawable.roundbutton_enabled);
					tvCharacterCount.setTextColor(getResources()
							.getColor(android.R.color.darker_gray));
				}
				
			}
		});
		
		// Post the tweet and close the activity
		btnTweet.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				PostTweet();
			}
		});
		
	}

	private void PostTweet() {
		
		llProgressBar.setVisibility(View.VISIBLE);
		
		if (NetworkingUtilities.isNetworkAvailable(this) == false) {
			DebugInfo.showToast(this, "Netowrk Connection Problem");
			TweetSendFailed();
		}

		client.postTweet(etCompose.getText().toString(),
				new JsonHttpResponseHandler() {

					public void onSuccess(JSONObject jsonObject) {
						Tweet sentTweet = Tweet.fromJSONObject(jsonObject);
						 
						Intent sentTweetIntent = new Intent();
						sentTweetIntent.putExtra("tweet", sentTweet);
						setResult(IntentCode.RESULT_OK, sentTweetIntent);
						llProgressBar.setVisibility(View.INVISIBLE);
						finish();
					}

					public void onFailure(Throwable e, JSONObject arg1) {
						DebugInfo.stackTrace();
						DebugInfo.logError(e.toString());
						TweetSendFailed();
					}
					protected void handleFailureMessage(Throwable arg0,
							String failureResonse) {
						DebugInfo.stackTrace();
						DebugInfo.logError("failureResonse");
						TweetSendFailed();
					}
				});
		
	}

	private void TweetSendFailed() {
		llProgressBar.setVisibility(View.INVISIBLE);
		Intent failedTweetIntent = new Intent();
		setResult(IntentCode.TWEET_SEND_FAIL, failedTweetIntent);
		finish();
	}

	private void setViews() {
		DebugInfo.stackTrace();
		
		currentUser = User.getCurrentUser();
		
		// Populate the data into the template view using the data object
		ImageLoader imageLoader = ImageLoader.getInstance();
		ivMyProfileImg.setImageResource(android.R.color.transparent);
		imageLoader.displayImage(currentUser.getProfileImageUrl(),
				ivMyProfileImg);

		tvMyName.setText(currentUser.getUserName());
		tvMyScreenName.setText("@" + currentUser.getUserScreenName());

		etCompose.setText(replyUserName);
		etCompose.requestFocus();
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		etCompose.setSelection(etCompose.getText().length());
		
		tvCharacterCount.setText((Integer.parseInt(tvCharacterCount.getText().toString()) - replyUserName.length() + 1)+" ");
		
		if(!replyUserName.isEmpty()){
			btnTweet.setEnabled(true);
			btnTweet.setBackgroundColor(getResources()
					.getColor(R.color.actionbar_background));
		}
	}

	private void finishSetup() {
		client = TwitterApplication.getRestClient();
	}

	private void receiveData() {
		DebugInfo.stackTrace();
		
		replyUserName = getIntent().getStringExtra("replyUserName");
	}

	private void getViews() {
		DebugInfo.stackTrace();
		
		tvMyName = (TextView) findViewById(R.id.tvMyName);
		tvMyScreenName = (TextView) findViewById(R.id.tvMyScreenName);
		ivMyProfileImg = (ImageView) findViewById(R.id.ivMyProfileImg);
		etCompose = (EditText) findViewById(R.id.etCompose);
		btnTweet = (Button) findViewById(R.id.btnTweet);
		tvCharacterCount = (TextView)findViewById(R.id.tvCharacterCount);
		llProgressBar = (LinearLayout) findViewById(R.id.llProgressBarComposeTweet);
	}

}
