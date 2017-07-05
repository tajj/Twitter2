# Project 4 - Simple Tweets

Simple Tweets is an android app that allows a user to view home and mentions timelines, view user profiles with user timelines, as well as compose and post a new tweet. The app utilizes [Twitter REST API](https://dev.twitter.com/rest/public).

Time spent: 9 hours spent in total

## Related Work
This project is the last in a series for [CodePath's Android Mobile Bootcamp for Engineers](http://codepath.com/androidbootcamp)

1. **[SimpleTodo](https://github.com/kristeaac/codepath-project-0-todo)** - allows building a todo list with basic todo items management functionality including adding new items, editing and deleting an existing item
2. **[Instagram Photo Viewer](https://github.com/kristeaac/codepath-project-1-instagram-photo-viewer)** - read-only media viewer for Instagram which allows a user to check out popular photos and videos 
3. **[Google Image Search](https://github.com/kristeaac/codepath-project-2-google-image-search)** - a Google Image Search app which allows a user to select search filters and paginate results infinitely
4. **[Simple Tweets](https://github.com/kristeaac/codepath-project-3-twitter-client)** - a simple Twitter client that supports viewing a Twitter timeline and composing a new tweet
5. **Extended Simple Tweets** - an extension of the previous Twitter client to use a tabbed interface and fragments


## User Stories

The following **required** functionality is completed:

* [X] The app includes **all required user stories** from Week 3 Twitter Client
* [X] User can **switch between Timeline and Mention views using tabs**
  * [X] User can view their home timeline tweets.
  * [X] User can view the recent mentions of their username.
* [X] User can navigate to **view their own profile**
  * [X] User can see picture, tagline, # of followers, # of following, and tweets on their profile.
* [X] User can **click on the profile image** in any tweet to see **another user's** profile.
 * [X] User can see picture, tagline, # of followers, # of following, and tweets of clicked user.
 * [X] Profile view includes that user's timeline
* [X] User can [infinitely paginate](http://guides.codepath.com/android/Endless-Scrolling-with-AdapterViews) any of these timelines (home, mentions, user) by scrolling to the bottom

The following **optional** features are implemented:

* [X] User can view following / followers list through the profile
* [ ] Implements robust error handling, [check if internet is available](http://guides.codepath.com/android/Sending-and-Managing-Network-Requests#checking-for-network-connectivity), handle error cases, network failures
* [ ] When a network request is sent, user sees an [indeterminate progress indicator](http://guides.codepath.com/android/Handling-ProgressBars#progress-within-actionbar)
* [X] User can **"reply" to any tweet on their home timeline**
  * [X] The user that wrote the original tweet is automatically "@" replied in compose
* [X] User can click on a tweet to be **taken to a "detail view"** of that tweet
 * [X] User can take favorite (and unfavorite) or retweet actions on a tweet
* [X] Improve the user interface and theme the app to feel twitter branded
* [X] User can **search for tweets matching a particular query** and see results

The following **bonus** features are implemented:

* [ ] User can view their direct messages (or send new ones)

The following **additional** features are implemented:

* [X] User can view favorites timeline through the profile
* [X] User can switch between tweets, followers, following and favorites in the profile using tabs

## Video Walkthrough 

Here's a walkthrough of implemented user stories:

<img src='walkthrough.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes

Describe any challenges encountered while building the app.

## Open-source libraries used

- [Android Async HTTP](https://github.com/loopj/android-async-http) - Simple asynchronous HTTP requests with JSON parsing
- [Picasso](http://square.github.io/picasso/) - Image loading and caching library for Android
- [Jackson](https://github.com/FasterXML/jackson) - JSON parser
- [PrettyTime](https://github.com/ocpsoft/prettytime/) - Social style date and time formatting
- [RoundedImageView](https://github.com/vinc3m1/RoundedImageView) - An ImageView that supports rounded corners

## License

    Copyright 2015 Kristy Caster

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.