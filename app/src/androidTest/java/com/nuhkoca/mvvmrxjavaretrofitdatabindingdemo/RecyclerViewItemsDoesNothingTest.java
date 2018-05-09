package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.ui.main.NewsActivity;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.ui.news.NewsFragment;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecyclerViewItemsDoesNothingTest {

    @Rule
    public ActivityTestRule<NewsActivity> newsActivityActivityTestRule = new ActivityTestRule<>(NewsActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void initIdling() {
        mIdlingResource = NewsFragment.getInstance().getIdlingResource();

        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void recyclerViewItemDoesNothing() {
        onView(withIndex(withId(R.id.rvNews), 0))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

    @After
    public void finalizeIdling() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }

    //Since RecyclerView gets instantiated 3 times, we need to select one of them!!!
    public static Matcher<View> withIndex(final Matcher<View> matcher, final int index) {
        return new TypeSafeMatcher<View>() {
            int currentIndex = 0;

            @Override
            public void describeTo(Description description) {
                description.appendText("with index: ");
                description.appendValue(index);
                matcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                return matcher.matches(view) && currentIndex++ == index;
            }
        };
    }
}