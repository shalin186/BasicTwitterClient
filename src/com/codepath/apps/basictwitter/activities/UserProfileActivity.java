package com.codepath.apps.basictwitter.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.fragments.UserTweetsFragment;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.codepath.apps.basictwitter.utilities.DebugInfo;
import com.codepath.apps.basictwitter.utilities.IntentCode;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class UserProfileActivity extends SherlockFragmentActivity {

	private User user;

	private ImageView ivBackgroundPicture;
	private ImageView ivProfilePicture;
	private TextView tvProfileUserName;
	private TextView tvProfileUserScreenName;
	private TextView tvNumTweets;
	private TextView tvNumFollowing;
	private TextView tvNumFollowers;
	private RelativeLayout rlProfileDetails;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_profile);
		DebugInfo.stackTrace();

		getViews();
		receiveData();
		setViews();
		finishSetup();
		setUserTweetsFragment();
	}

	private void setUserTweetsFragment() {
		// Within the activity
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		UserTweetsFragment userTweetsFragment = UserTweetsFragment
				.newInstance(user.getUserScreenName());
		ft.replace(R.id.flUserTweetsContainer, userTweetsFragment);
		ft.commit();
	}

	private void setViews() {
		getActionBar().setTitle("Profile");
		
		ImageLoader imageLoader = ImageLoader.getInstance();
		
		
		ivBackgroundPicture.setImageResource(android.R.color.transparent);
		imageLoader.displayImage(user.getprofileBgImageUrl(),
				ivBackgroundPicture);
//		Loading the image in the layout itself
//		String uri = user.getprofileBgImageUrl();
//		imageLoader.loadImage(uri, new SimpleImageLoadingListener() {
//			@SuppressWarnings("deprecation")
//			@Override
//			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//			super.onLoadingComplete(imageUri, view, loadedImage);
//			rlProfileDetails.setBackgroundDrawable(new BitmapDrawable(loadedImage));
//			}
//			});
		
		ivProfilePicture.setImageResource(android.R.color.transparent);
		imageLoader.displayImage(user.getProfileImageUrl(),
				ivProfilePicture);
		
		tvProfileUserName.setText(user.getUserName());

		tvProfileUserScreenName.setText("@"
				+ user.getUserScreenName());
		
		tvNumTweets.setText(Html.fromHtml(user.getNumTweets()+ "<br />" + "<font color=\"#808080\">" + "tweets" + "</font>"));
		tvNumFollowing.setText(Html.fromHtml(user.getNumFollowing()+ "<br />" + "<font color=\"#808080\">" + "following" + "</font>"));
		tvNumFollowers.setText(Html.fromHtml(user.getNumFollowers()+ "<br />" + "<font color=\"#808080\">" + "followers" + "</font>"));
	}

	private void finishSetup() {
		// TODO Auto-generated method stub

	}

	private void receiveData() {
		DebugInfo.stackTrace();
		user = (User) getIntent().getParcelableExtra("user");
	}

	private void getViews() {
		ivBackgroundPicture = (ImageView) findViewById(R.id.ivBackgroundPicture);
		ivProfilePicture = (ImageView) findViewById(R.id.ivProfilePicture);
		tvProfileUserName = (TextView) findViewById(R.id.tvProfileUserName);
		tvProfileUserScreenName = (TextView) findViewById(R.id.tvProfileUserScreenName);
		tvNumTweets = (TextView) findViewById(R.id.tvNumTweets);
		tvNumFollowing = (TextView) findViewById(R.id.tvNumFollowing);
		tvNumFollowers = (TextView) findViewById(R.id.tvNumFollowers);
		rlProfileDetails = (RelativeLayout) findViewById(R.id.rlProfileDetails);

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == IntentCode.RESULT_OK
				&& requestCode == IntentCode.REQUEST_CODE) {
			Tweet tweet = (Tweet) data.getParcelableExtra("tweet");
			UserTweetsFragment userTweetsFragment = (UserTweetsFragment) getSupportFragmentManager()
					.findFragmentById(R.id.flUserTweetsContainer);
			userTweetsFragment.displaySentTweet(tweet);
		} else if (resultCode == IntentCode.TWEET_SEND_FAIL
				&& requestCode == IntentCode.REQUEST_CODE) {
			DebugInfo.showToast(this, "Tweet sending failed");
		}

	}
}
