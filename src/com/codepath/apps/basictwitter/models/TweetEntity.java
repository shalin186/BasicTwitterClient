package com.codepath.apps.basictwitter.models;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class TweetEntity implements Parcelable {
	private String imageUrl;

	public TweetEntity(){
		
	}
	
	public static TweetEntity fromJSONObject(JSONObject jsonObj){
		TweetEntity tweetEntity = new TweetEntity();
		
		JSONArray mediaJSONArray = jsonObj.optJSONArray("media");
		if(mediaJSONArray != null){
			tweetEntity.imageUrl = mediaJSONArray.optJSONObject(0).optString("media_url");
		}
		
		return tweetEntity;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}

    protected TweetEntity(Parcel in) {
        imageUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUrl);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TweetEntity> CREATOR = new Parcelable.Creator<TweetEntity>() {
        @Override
        public TweetEntity createFromParcel(Parcel in) {
            return new TweetEntity(in);
        }

        @Override
        public TweetEntity[] newArray(int size) {
            return new TweetEntity[size];
        }
    };
}