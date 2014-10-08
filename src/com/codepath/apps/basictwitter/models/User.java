package com.codepath.apps.basictwitter.models;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import com.codepath.apps.basictwitter.utilities.DebugInfo;

public class User implements Parcelable {
	private static User currentUser;

	private String userName;
	private String userScreenName;
	private String profileImageUrl;
	private String profileBgImageUrl;
	private long numTweets;
	private long numFollowing;
	private long numFollowers;
	
	public User(){
		
	}
	
	public static User fromJson(JSONObject jsonObj){
		// DebugInfo.stackTrace();
		
		User user = new User();
		
		user.userName = jsonObj.optString("name");
		user.userScreenName = jsonObj.optString("screen_name");
		user.profileImageUrl = jsonObj.optString("profile_image_url");
		user.profileBgImageUrl = jsonObj.optString("profile_background_image_url");
		user.numTweets = jsonObj.optLong("statuses_count");
		user.numFollowing = jsonObj.optLong("friends_count");
		user.numFollowers = jsonObj.optLong("followers_count");
		
		return user;
	}

	public static void setCurrentUser(JSONObject jsonObj){
		DebugInfo.stackTrace();
		
		currentUser = new User();
		currentUser.userName = jsonObj.optString("name");
		currentUser.userScreenName = jsonObj.optString("screen_name");
		currentUser.profileImageUrl = jsonObj.optString("profile_image_url");
		currentUser.profileBgImageUrl = jsonObj.optString("profile_background_image_url");
		currentUser.numTweets = jsonObj.optLong("statuses_count");
		currentUser.numFollowing = jsonObj.optLong("friends_count");
		currentUser.numFollowers = jsonObj.optLong("followers_count");

	}

	public static User getCurrentUser() {
		User user = new User();
		user.userName = currentUser.userName;
		user.userScreenName = currentUser.userScreenName;
		user.profileImageUrl = currentUser.profileImageUrl;
		user.profileBgImageUrl = currentUser.profileBgImageUrl;
		user.numTweets = currentUser.numTweets;
		user.numFollowing = currentUser.numFollowing;
		user.numFollowers = currentUser.numFollowers;

		return user;
	}

	public String getUserName() {
		return userName;
	}

	public String getUserScreenName() {
		return userScreenName;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public String getprofileBgImageUrl() {
		return profileBgImageUrl;
	}

	public long getNumTweets() {
		return numTweets;
	}

	public long getNumFollowing() {
		return numFollowing;
	}

	public long getNumFollowers() {
		return numFollowers;
	}

    protected User(Parcel in) {
        userName = in.readString();
        userScreenName = in.readString();
        profileImageUrl = in.readString();
        profileBgImageUrl = in.readString();
        numTweets = in.readLong();
        numFollowing = in.readLong();
        numFollowers = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(userScreenName);
        dest.writeString(profileImageUrl);
        dest.writeString(profileBgImageUrl);
        dest.writeLong(numTweets);
        dest.writeLong(numFollowing);
        dest.writeLong(numFollowers);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}