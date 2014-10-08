# Simple Twitter Client

This is an simple twitter client with some basic and advanced functions

Time spent: 30 hours

Completed user stories:

* [x] User can sign in to Twitter using OAuth login
* [x] User can view the tweets from their home timeline
		* [x] User should be displayed the username, name, and body for each tweet
		* [x] User should be displayed the relative timestamp for each tweet "8m", "7h"
		* [x] User can view more tweets as they scroll with infinite pagination
* [x] User can compose a new tweet
		* [x] User can click a “Tweet” Button
		* [x] User can then enter a new tweet and post this to twitter
		* [x] User is taken back to home timeline with new tweet visible in timeline
		
* [x] User can switch between Timeline and Mention views using tabs.
* [x] User can view their home timeline tweets.
* [x] User can view the recent mentions of their username. (somehow not working on my emulator, works fine on phone)
* [x] User can scroll to bottom of either of these lists and new tweets will load ("infinite scroll")
* [x] Implement tabs in a gingerbread-compatible approach
* [x] User can navigate to view their own profile
* [x] User can see picture, # of followers, # of following, and tweets on their profile.
* [x] User can click on the profile image in any tweet to see another user's profile.
* [x] User can see picture, # of followers, # of following, and tweets of clicked user.
* [x] Profile view should include that user's timeline

* [x] Robust error handling, check if internet is available, handle error cases, network failures
* [x] When a network request is sent, user sees an indeterminate progress indicator
* [x] User can "reply" to any tweet on their home timeline
* [x] The user that wrote the original tweet is automatically "@" replied in compose
* [x] User can take favorite (and unfavorite) or reweet actions on a tweet
* [x] User can search for tweets matching a particular query and see results

Libraries Used:
* [x] android-async-http: https://www.dropbox.com/s/zqggkqv60zggyrt/android-async-http-1.4.5.jar?dl=1
* [x] codepath-utils
* [x] scribe-codepath
* [x] codepath-oauth
* [x] universal-image-loader-1.8.4.jar

 
Walkthrough of all user stories:

![Video Walkthrough](TwitterClient.gif)

GIF created with [LiceCap](http://www.cockos.com/licecap/).
