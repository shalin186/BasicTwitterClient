package com.codepath.apps.basictwitter.utilities;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;

import com.codepath.apps.basictwitter.TwitterApplication;
import com.codepath.apps.basictwitter.activities.ComposeTweetActivity;
import com.codepath.apps.basictwitter.activities.TimelineActivity;
import com.codepath.apps.basictwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TwitterApiHelper {

	private Context context;
	private User currentUser;
	
	public TwitterApiHelper(Context context){
		this.context = context;
	}
	
	public void setCurrentUser(){
		DebugInfo.stackTrace();
		TwitterClient client = TwitterApplication.getRestClient();
		
		if (NetworkingUtilities.isNetworkAvailable(context) == false) {
			DebugInfo.showToast(context, "Netowrk Connection Problem");
			return;
		}
		
		client.getCurrentUserCredentials(new JsonHttpResponseHandler() {

			public void onSuccess(JSONObject jsonObj) {
				DebugInfo.stackTrace();
				
				User.setCurrentUser(jsonObj);
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

	public void composeTweet(String replyUserName) {
		DebugInfo.stackTrace();
		
		final String userName = replyUserName;
		TwitterClient client = TwitterApplication.getRestClient();
		
		if (NetworkingUtilities.isNetworkAvailable(context) == false) {
			DebugInfo.showToast(context, "Netowrk Connection Problem");
			return;
		}

		client.getCurrentUserCredentials(new JsonHttpResponseHandler() {

			public void onSuccess(JSONObject jsonObj) {
				DebugInfo.stackTrace();
				// Get current user info
				currentUser = User.fromJson(jsonObj);
				// Launch Compose Activity for result
				Intent composeTweetIntent = new Intent((TimelineActivity)context, ComposeTweetActivity.class);
				composeTweetIntent.putExtra("user", currentUser);
				composeTweetIntent.putExtra("replyUserName", userName);
				((TimelineActivity) context).startActivityForResult(composeTweetIntent, IntentCode.RESULT_OK);
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
}
