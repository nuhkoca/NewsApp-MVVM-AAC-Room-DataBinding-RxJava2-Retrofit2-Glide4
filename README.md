# NewsApp using newsapi.org

![App Logo](https://github.com/nuhkoca/NewsApp-MVVM-DataBinding-RxJava-Retrofit/blob/master/app/src/main/res/mipmap-xxxhdpi/ic_launcher_round.png)

This project is a playground to clutch MVVM pattern, Architecture Components, Room and others. This project actively uses Room Persistence Library to show feed in case no internet connection. Along with Lifecycle Components, API datum are consistently observed to able to detect whether or not there is any update or change. The app also benefits from PreferenceScreen to display news according to users' preferences. You can choose country, category, source or languages for different lists.

This repo is still under maintenance.

# What the project uses

* [MVVM Pattern](https://github.com/googlesamples/android-architecture)
* [BindingAdapter](https://developer.android.com/reference/android/databinding/BindingAdapter.html)
* [Retrofit2](https://github.com/square/retrofit)
* [RxJava2](https://github.com/ReactiveX/RxJava)
* [Glide v4](https://github.com/bumptech/glide)
* [GlideModule](http://bumptech.github.io/glide/doc/generatedapi.html#availability)
* [Timber](https://github.com/JakeWharton/timber)
* [OkHttp](https://github.com/square/okhttp)
* [DataBinding](https://developer.android.com/topic/libraries/data-binding/index.html)
* [Room Persistence Library](https://github.com/googlecodelabs/android-room-with-a-view)
* [PreferenceScreen v14](https://developer.android.com/reference/android/preference/Preference)
* [ViewPagerTransforms](https://github.com/ToxicBakery/ViewPagerTransforms)

# ToDo List

- [x] Top Headlines, Everything and Sources are listed.
- [x] Allowed users to switch between fragments using Bottom Navigation View and ViewPager.
- [x] Listed offline data when there is no internet connection instead of showing No Internet Connection error.

# Screenshots

## Phone

<p align="left">
<img src="https://github.com/nuhkoca/NewsApp-MVVM-DataBinding-RxJava-Retrofit/blob/master/art/offline_data.png" height="500"/>

<img src="https://github.com/nuhkoca/NewsApp-MVVM-DataBinding-RxJava-Retrofit/blob/master/art/preferences.png" height="500"/>

<img src="https://github.com/nuhkoca/NewsApp-MVVM-DataBinding-RxJava-Retrofit/blob/master/art/no_internet_screen.png" height="500"/>

<img src="https://github.com/nuhkoca/NewsApp-MVVM-DataBinding-RxJava-Retrofit/blob/master/art/no_internet_warning.png" height="500"/>
</p>


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
