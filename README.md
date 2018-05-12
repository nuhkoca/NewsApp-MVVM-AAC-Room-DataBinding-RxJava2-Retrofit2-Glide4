# A modern NewsApp

![App Logo](https://github.com/nuhkoca/NewsApp-MVVM-DataBinding-RxJava-Retrofit/blob/master/app/src/main/res/mipmap-xxxhdpi/ic_launcher_round.png)

:boom: A playground with the latest techs

This project is a playground to clutch MVVM pattern, Architecture Components, Room and others. This project actively uses Room Persistence Library to show feed in case no internet connection. Along with Lifecycle Components, API datum are consistently observed to able to detect whether or not there is any update or change. The app also benefits from PreferenceScreen to display news according to users' preferences. You can choose country, category, source or languages for different lists.

**This repo is still under maintenance.**

This project is powered by **NewsAPI.org**. To get your own API key, please visit [NewsAPI.org](https://newsapi.org/)

# ToDo List

- [x] Top Headlines, Everything and Sources are listed.
- [x] Allowed users to switch between fragments using Bottom Navigation View and ViewPager.
- [x] Integrated Room Persistence Library to list offline data in the event of no internet connection.
- [x] Added Preferences Screen to display diverse news according to user's selection.
- [x] Added Day/Night Mode.
- [x] Implemented Custom News UI to list news from selected news source which takes place in Sources section.
- [x] Implemented Search feature for Everything section and thus user can search anything in the app.
- [x] Performed UI tests and passed successfully..
- [x] Implement Filter feature for Sources section and thus user can filter results.
- [x] Implement Firebase and Firestore to send push notifications.
- [ ] Implement endless scroll for more items.

# Screenshots

## Phone

<p align="left">
<img src="https://github.com/nuhkoca/NewsApp-MVVM-DataBinding-RxJava-Retrofit/blob/master/art/1.png" height="450"/>

<img src="https://github.com/nuhkoca/NewsApp-MVVM-DataBinding-RxJava-Retrofit/blob/master/art/2.png" height="450"/>

<img src="https://github.com/nuhkoca/NewsApp-MVVM-DataBinding-RxJava-Retrofit/blob/master/art/4.png" height="450"/>

<img src="https://github.com/nuhkoca/NewsApp-MVVM-DataBinding-RxJava-Retrofit/blob/master/art/5.png" height="450"/>

<img src="https://github.com/nuhkoca/NewsApp-MVVM-DataBinding-RxJava-Retrofit/blob/master/art/6.png" height="450"/>

<img src="https://github.com/nuhkoca/NewsApp-MVVM-DataBinding-RxJava-Retrofit/blob/master/art/7.png" height="450"/>

<img src="https://github.com/nuhkoca/NewsApp-MVVM-DataBinding-RxJava-Retrofit/blob/master/art/8.png" height="450"/>

<img src="https://github.com/nuhkoca/NewsApp-MVVM-DataBinding-RxJava-Retrofit/blob/master/art/9.png" height="450"/>

<img src="https://github.com/nuhkoca/NewsApp-MVVM-DataBinding-RxJava-Retrofit/blob/master/art/3.png" height="450"/>
</p>

# What the project uses

* [MVVM Pattern](https://github.com/googlesamples/android-architecture)
* [Architecture Components](https://developer.android.com/topic/libraries/architecture/)
* [BindingAdapter](https://developer.android.com/reference/android/databinding/BindingAdapter.html)
* [ConstraintLayout](https://developer.android.com/training/constraint-layout/)
* [Retrofit2](https://github.com/square/retrofit)
* [RxJava2](https://github.com/ReactiveX/RxJava)
* [Glide v4](https://github.com/bumptech/glide)
* [GlideModule](http://bumptech.github.io/glide/doc/generatedapi.html#availability)
* [Timber](https://github.com/JakeWharton/timber)
* [LeakCanary](https://github.com/square/leakcanary)
* [OkHttp](https://github.com/square/okhttp)
* [DataBinding](https://developer.android.com/topic/libraries/data-binding/index.html)
* [Room Persistence Library](https://github.com/googlecodelabs/android-room-with-a-view)
* [PreferenceScreen v14](https://developer.android.com/reference/android/preference/Preference)
* [ViewPagerTransforms](https://github.com/ToxicBakery/ViewPagerTransforms)
* [Stetho](http://facebook.github.io/stetho/)
* [Android About Page](https://github.com/medyo/android-about-page)
* [Espresso - Testing](https://github.com/googlesamples/android-testing)
* [Materialish Progress](https://github.com/pnikosis/materialish-progress)
* [Material Dialogs](https://github.com/afollestad/material-dialogs)
* [About Libraries](https://github.com/mikepenz/AboutLibraries)
* [Firebase & Firestore](https://firebase.google.com/docs/firestore/)


# License

App icon based on:

Icons made by Freepik from www.flaticon.com is licensed by CC 3.0 BY

```
MIT License

Copyright (c) 2018 Nuh Koca

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
