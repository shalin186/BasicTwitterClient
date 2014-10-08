package com.codepath.apps.basictwitter.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.text.format.DateUtils;

public class RelativeTimeStamp {

	// getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
	public static String getRelativeTimeAgo(String rawJsonDate) {
		// DebugInfo.stackTrace();
		
		String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		SimpleDateFormat sf = new SimpleDateFormat(twitterFormat,
				Locale.ENGLISH);
		sf.setLenient(true);

		String relativeDate = "";
		try {
			long dateMillis = sf.parse(rawJsonDate).getTime();
			relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
					System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS)
					.toString();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return relativeDate;
	}
	
	public static String getRelativeTimeInShortFormat(String rawJsonDate){
		// DebugInfo.stackTrace();
		
		String relativeTime = getRelativeTimeAgo(rawJsonDate);
		
		if(relativeTime.equals("Yesterday")){
			relativeTime = "1 day ago";
		}
		
		String relativeTimeInt = relativeTime.substring(0,
				relativeTime.indexOf(" "));
		String relativeTimeStr = relativeTime.substring(
				relativeTime.indexOf(" ") + 1, relativeTime.indexOf(" ") + 2);
		
		String relativeTimeInShortFormat = relativeTimeInt + relativeTimeStr;
		
		return relativeTimeInShortFormat;
	}
}
