package com.codepath.apps.basictwitter.models;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Tweet implements Parcelable {
	public static long maxId;
	public static long sinceId;
	private String body;
	private String createAt;
	private long id;
	private Boolean bFavorited;
	private Boolean bRetweeted;
	private long retweetCount;
	private long favoriteCount;
	private TweetEntity tweetEntity;
	private User user;

    public Tweet() {
		// TODO Auto-generated constructor stub
	}

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
		// DebugInfo.stackTrace();

		// Return the arraylist of tweets from the JSON Model
		// Which can be directly added to the adapter
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();

		for (int i = 0; i < jsonArray.length(); i++) {
			// Get the JSON Object from JSON Array at a given index
			// And get the tweet object out of that JSON Object
			// Add the tweet object into ArrayList
			tweets.add(Tweet.fromJSONObject(jsonArray.optJSONObject(i)));
		}

		return tweets;
	}

	public static Tweet fromJSONObject(JSONObject jsonObj) {
		// DebugInfo.stackTrace();

		Tweet tweet = new Tweet();

		// Build the tweet object out of JSON Object
		tweet.body = jsonObj.optString("text");
		tweet.createAt = jsonObj.optString("created_at");
		tweet.id = jsonObj.optLong("id", 0);
		tweet.bFavorited = jsonObj.optBoolean("favorited");
		tweet.favoriteCount = jsonObj.optLong("favorite_count");
		tweet.bRetweeted = jsonObj.optBoolean("retweeted");
		tweet.retweetCount = jsonObj.optLong("retweet_count");

		// Get the user object from "user" JSON Object
		tweet.user = User.fromJson(jsonObj.optJSONObject("user"));

		// Get the entity object from "entity" JSON Array
		tweet.tweetEntity = TweetEntity.fromJSONObject(jsonObj
				.optJSONObject("entities"));

		// Set the ids
		tweet.id = jsonObj.optLong("id");
		if (Tweet.maxId == 0) {
			Tweet.maxId = tweet.id;
		} else {
			Tweet.maxId = Math.min(tweet.id, Tweet.maxId);
		}

		Tweet.sinceId = Math.max(tweet.id, Tweet.sinceId);

		return tweet;
	}

	public String getBody() {
		return body;
	}

	public String getCreateAt() {
		return createAt;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Boolean getbFavorited() {
		return bFavorited;
	}

	public void setbFavorited(Boolean bFavorited) {
		this.bFavorited = bFavorited;
	}

	public long getFavoriteCount() {
		return favoriteCount;
	}

	public void setFavoriteCount(long favoriteCount) {
		this.favoriteCount = favoriteCount;
	}

	public Boolean getbRetweeted() {
		return bRetweeted;
	}

	public void setbRetweeted(Boolean bRetweeted) {
		this.bRetweeted = bRetweeted;
	}

	public long getRetweetCount() {
		return retweetCount;
	}

	public void setRetweetCount(long retweetCount) {
		this.retweetCount = retweetCount;
	}

	public User getUser() {
		return user;
	}

	public TweetEntity getTweetEntity() {
		return tweetEntity;
	}

    protected Tweet(Parcel in) {
        body = in.readString();
        createAt = in.readString();
        id = in.readLong();
        byte bFavoritedVal = in.readByte();
        bFavorited = bFavoritedVal == 0x02 ? null : bFavoritedVal != 0x00;
        byte bRetweetedVal = in.readByte();
        bRetweeted = bRetweetedVal == 0x02 ? null : bRetweetedVal != 0x00;
        retweetCount = in.readLong();
        favoriteCount = in.readLong();
        tweetEntity = (TweetEntity) in.readValue(TweetEntity.class.getClassLoader());
        user = (User) in.readValue(User.class.getClassLoader());
    }

	@Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(body);
        dest.writeString(createAt);
        dest.writeLong(id);
        if (bFavorited == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (bFavorited ? 0x01 : 0x00));
        }
        if (bRetweeted == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (bRetweeted ? 0x01 : 0x00));
        }
        dest.writeLong(retweetCount);
        dest.writeLong(favoriteCount);
        dest.writeValue(tweetEntity);
        dest.writeValue(user);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Tweet> CREATOR = new Parcelable.Creator<Tweet>() {
        @Override
        public Tweet createFromParcel(Parcel in) {
            return new Tweet(in);
        }

        @Override
        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };
}